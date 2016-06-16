package Server_Side.Server_MTL;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import Client_Side.ManagerClients;
import Record_Type.RecordInfo;

public class Clinic_MTL_UDP_Listening {
	public static void main(String[] args) {
		System.out.println("start listening montreal request.");
		initLogger(Config_MTL.SERVER_NAME);
		openUDPListener();
	}
	
	/**
	 * Initial the Logger function.
	 * @param server_name
	 */
	public static void initLogger(String server_name){
		try {
			String dir = "Server_Side_Log/";
			Config_MTL.LOGGER = Logger.getLogger(ManagerClients.class.getName());
			Config_MTL.LOGGER.setUseParentHandlers(false);
			Config_MTL.FH = new FileHandler(dir+server_name+"_UDP.log",true);
			Config_MTL.LOGGER.addHandler(Config_MTL.FH);
			SimpleFormatter formatter = new SimpleFormatter();
			Config_MTL.FH.setFormatter(formatter);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Open UDP listening port to check ManagerID and receive the other server request to get local hash table size.
	 * Request 001 for check ManagerID
	 * Request 002 for get local hash table size
	 */
	public static void openUDPListener(){
		DatagramSocket socket = null;
		try{
			socket = new DatagramSocket(Config_MTL.LOCAL_LISTENING_PORT);
			while(true){
				byte[] buffer = new byte[100]; 
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				socket.receive(request);
				Config_MTL.LOGGER.info("Get request: " + (new String(request.getData()).trim())+ "\n" + "Start a new thread to handle this.");
				new Connection(socket, request);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(socket != null) socket.close();
		}
	}
	
	/**
	 * New thread to handle the newly request
	 * @author AlexChen
	 *
	 */
	static class Connection extends Thread{
		DatagramSocket socket = null;
		DatagramPacket request = null;
		String result = null;
		public Connection(DatagramSocket n_socket, DatagramPacket n_request) {
			this.socket = n_socket;
			this.request = n_request;
			String requestcode = new String(request.getData()).trim().substring(0, 3);
			switch (requestcode) {
			case "001":
				Config_MTL.LOGGER.info("Request code: " + requestcode + ", " + "Check ManagerID: " + (new String(request.getData()).trim().substring(3)+ " valid or not."));
				result = checkManagerID(new String(request.getData()).trim().substring(3));
				break;
			case "002":
				Config_MTL.LOGGER.info("Request code: " + requestcode + ", " + "Search HashMap, SearchType: " + (new String(request.getData()).trim().substring(3)));
				result = getLocalHashSize(new String(request.getData()).trim().substring(3));
				break;
			}
			this.start();
		}
		
		@Override
		public void run() {
			try {
				DatagramPacket reply = new DatagramPacket(result.getBytes(),result.getBytes().length, request.getAddress(), request.getPort()); 
				socket.send(reply);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Check ManagerID and return valid or invalid.
	 * @param managerID
	 * @return
	 */
	public static String checkManagerID(String managerID){
		for(String account: Config_MTL.MANAGER_ACCOUNT){
			if(account.equalsIgnoreCase(managerID)){
				return "valid";
			}
		}
		return "invalid";
	}
	
	/**
	 * Check local hash table size and return the value.
	 * @param recordType
	 * @return
	 */
	public static synchronized String getLocalHashSize(String recordType){
		int dr_num = 0;
		int nr_num = 0;
		
		for(Map.Entry<Character, ArrayList<RecordInfo>> entry:Config_MTL.HASH_TABLE.entrySet()){
			for(RecordInfo record:entry.getValue()){
				switch(record.getRecordID().substring(0, 2)){
				case "DR":
					dr_num++;
					break;
				case "NR":
					nr_num++;
					break;
				}
			}
		}
		if(recordType.equalsIgnoreCase("dr")){
			return "MTL "+"DR: "+dr_num;
		}else if(recordType.equalsIgnoreCase("nr")){
			return "MTL "+"NR: "+nr_num;
		}else{
			return "MTL "+"ALL: "+(dr_num+nr_num);
		}
	}
}
