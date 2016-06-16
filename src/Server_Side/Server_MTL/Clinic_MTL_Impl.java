package Server_Side.Server_MTL;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import org.omg.CORBA.ORB;

import DSMS_CORBA.DSMSPOA;
import Record_Type.DoctorRecord;
import Record_Type.NurseRecord;
import Record_Type.RecordInfo;

public class Clinic_MTL_Impl extends DSMSPOA{

	private ORB orb;
	
	public void setORB(ORB orb_val) {
	    this.orb = orb_val; 
	}
	
	@Override
	public String createDRecord(String managerId, String firstName, String lastName, String address, String phone,
			String specialization, String location) {
		if(!checkLocation(location)){
			return "Location is not right. Please input (mtl,lvl or ddo).\n";
		}
		String recordID = null;
		RecordInfo doc_recorde_with_recordID = null;
		
		Character capital_lastname = lastName.charAt(0);
		if(Config_MTL.HASH_TABLE.containsKey(capital_lastname)){
			Config_MTL.RECORD_LIST = Config_MTL.HASH_TABLE.get(capital_lastname);
		}else{
			Config_MTL.RECORD_LIST = new ArrayList<RecordInfo>();
		}
		DoctorRecord doc_recorde = new DoctorRecord(firstName, lastName, address, phone, specialization, location);
		recordID = "DR" + sendMessageToOtherServer(Config_MTL.SERVER_PORT_RECORDID_ASSIGN, "getRecordIdNumber");
		doc_recorde_with_recordID = new RecordInfo(recordID, doc_recorde);
		
		synchronized (this) {
			Config_MTL.RECORD_LIST.add(doc_recorde_with_recordID);
			Config_MTL.HASH_TABLE.put(capital_lastname, Config_MTL.RECORD_LIST);
		}
		Config_MTL.LOGGER.info("Manager: "+ managerId + " Creat Doctor Record: "+ "\n" +doc_recorde_with_recordID.toString());
		return "Doctor Record Buid Succeed !" + "\n" +doc_recorde_with_recordID.toString();
	}

	@Override
	public String createNRecord(String managerId, String firstName, String lastName, String designation, String status,
			String statusDate) {
		if(!checkDesignation(designation)){
			return "Designation is not right. Please input (junior or senior).\n";
		}
		if(!checkStatus(status)){
			return "Status is not right. Please input (active or terminated).\n";
		}
		if(!checkStatusDate(statusDate)){
			return "Status Date is not right. Please input the right format of date (yyyy/mm/dd).";
		}
		String recordID = null;
		RecordInfo nur_recorde_with_recordID = null;
		 
		Character capital_lastname = lastName.charAt(0);
		if(Config_MTL.HASH_TABLE.containsKey(capital_lastname)){
			Config_MTL.RECORD_LIST = Config_MTL.HASH_TABLE.get(capital_lastname);
		}else{
			Config_MTL.RECORD_LIST = new ArrayList<RecordInfo>();
		}
		NurseRecord nur_recorde = new NurseRecord(firstName, lastName, designation, status, statusDate);
		recordID = "NR" + sendMessageToOtherServer(Config_MTL.SERVER_PORT_RECORDID_ASSIGN, "getRecordIdNumber");
		nur_recorde_with_recordID = new RecordInfo(recordID, nur_recorde);
		
		synchronized (this) {
			Config_MTL.RECORD_LIST.add(nur_recorde_with_recordID);
			Config_MTL.HASH_TABLE.put(capital_lastname, Config_MTL.RECORD_LIST);
		}
		Config_MTL.LOGGER.info("Manager: "+ managerId + " Creat Nurse Record: "+ "\n" +nur_recorde_with_recordID.toString());
		return "Nurse Record Buid Succeed !" + "\n" +nur_recorde_with_recordID.toString();
	}

	@Override
	public String getRecordCounts(String managerId, String recordType) {
		String lvl_hash_size = sendMessageToOtherServer(Config_MTL.SERVER_PORT_LVL, recordType);
		String ddo_hash_size = sendMessageToOtherServer(Config_MTL.SERVER_PORT_DDO, recordType);
		String mtl_hash_size = Clinic_MTL_Server.getLocalHashSize(recordType);
		String result = mtl_hash_size + "\n" + lvl_hash_size + "\n" + ddo_hash_size + "\n";
		Config_MTL.LOGGER.info("Manager: "+ managerId + " search RecordCounts: "+ "\n" + result);
		return result;
	}

	@Override
	public String editRecord(String managerId, String recordID, String fieldName, String newValue) {
		for(Map.Entry<Character, ArrayList<RecordInfo>> entry:Config_MTL.HASH_TABLE.entrySet()){
			for(RecordInfo record:entry.getValue()){
				if(recordID.equalsIgnoreCase(record.getRecordID())){
					if(recordID.contains("DR")||recordID.contains("dr")){
						if(fieldName.equalsIgnoreCase("Address")){
							record.getDoctorRecord().setAddress(newValue);
							Config_MTL.LOGGER.info("Manager: "+ managerId + " edit the Address of Doctor Record: "+ "\n" + record.toString());
							return "edit succeed !\n"+record.toString();
						}else if(fieldName.equalsIgnoreCase("Phone")){
							record.getDoctorRecord().setPhone(newValue);
							Config_MTL.LOGGER.info("Manager: "+ managerId + " edit the phone of Doctor Record: "+ "\n" + record.toString());
							return "edit succeed !\n"+record.toString();
						}else if (fieldName.equalsIgnoreCase("Location")){
							if(!checkLocation(newValue)){
								return "Location is not right. Please input (mtl,lvl or ddo).\n";
							}
							record.getDoctorRecord().setLocation(newValue);
							Config_MTL.LOGGER.info("Manager: "+ managerId + " edit the Location of Doctor Record: "+ "\n" + record.toString());
							return "edit succeed !\n"+record.toString();
						}
					}else if(recordID.contains("NR")||recordID.contains("nr")){
						if(fieldName.equalsIgnoreCase("Designation")){
							if(!checkDesignation(newValue)){
								return "Designation is not right. Please input (junior or senior).\n";
							}
							record.getNurseRecord().setDesignation(newValue);
							Config_MTL.LOGGER.info("Manager: "+ managerId + " edit the Designation of Nurse Record: "+ "\n" + record.toString());
							return "edit succeed !\n"+record.toString();
						}else if(fieldName.equalsIgnoreCase("Status")){
							if(!checkStatus(newValue)){
								return "Status is not right. Please input (active or terminated).\n";
							}
							record.getNurseRecord().setStatus(newValue);
							Config_MTL.LOGGER.info("Manager: "+ managerId + " edit the Status of Nurse Record: "+ "\n" + record.toString());
							return "edit succeed !\n"+record.toString();
						}else if (fieldName.equalsIgnoreCase("statusDate")){
							record.getNurseRecord().setStatusDate(newValue);
							Config_MTL.LOGGER.info("Manager: "+ managerId + " edit the Status date of Nurse Record: "+ "\n" + record.toString());
							return "edit succeed !\n"+record.toString();
						}
						
					}
				}
			}
		}
		return "edit failed";
	}

	@Override
	public String transferRecord(String managerId, String recordID, String remoteClinicServerName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Local check Location is right or not.
	 * @param location
	 * @return
	 */
	public static Boolean checkLocation(String location){
		for(Config_MTL.D_LOCATION d_location: Config_MTL.D_LOCATION.values()){
			if(location.equals(d_location.toString())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Local check Designation is right or not.
	 * @param designation
	 * @return
	 */
	public static Boolean checkDesignation(String designation){
		for(Config_MTL.N_DESIGNATION n_designation: Config_MTL.N_DESIGNATION.values()){
			if(designation.equals(n_designation.toString())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Local check Status is right or not.
	 * @param status
	 * @return
	 */
	public static Boolean checkStatus(String status){
		for(Config_MTL.N_STATUS n_status: Config_MTL.N_STATUS.values()){
			if(status.equals(n_status.toString())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Local check Status Date is right or not.
	 * @param date
	 * @return
	 */
	public static Boolean checkStatusDate(String date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		try {
			format.setLenient(false);
			format.parse(date);
		} catch (ParseException e) {
			return false;
		}
		return true;	
	}
	
	/**
	 * This function is for request other 2 server for their count of specific record type.
	 * @param server_port
	 * @param recordType
	 * @return
	 * 
	 */
	public static String sendMessageToOtherServer(int serverPort, String content){
		DatagramSocket socket = null;
		String hostname = "127.0.0.1";
		String requestcode = "002";
		byte[] message = null;
		
	    try {
	    	socket = new DatagramSocket();
	    	if(content.equals("getNumber")){
	    		message = (new String(content)).getBytes();
	    	}else{
	    		message = (new String(requestcode+content)).getBytes();
	    	}
	    	InetAddress Host = InetAddress.getByName(hostname);
	    	DatagramPacket request = new DatagramPacket(message, message.length, Host, serverPort);
	    	socket.send(request);
	    	byte[] buffer = new byte[100];
	    	DatagramPacket reply = new DatagramPacket(buffer, buffer.length); 
	    	socket.receive(reply);
	    	String result = new String(reply.getData()).trim();
	    	return result;
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    	}
		finally{
			if(socket != null){
				socket.close();
				}
			}
		return null; 
	}
}
