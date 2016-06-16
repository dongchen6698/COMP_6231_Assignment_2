package Server_Side.Server_DDO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import Record_Type.DoctorRecord;
import Record_Type.NurseRecord;
import Record_Type.RecordInfo;

public class Config_DDO {
	static enum D_LOCATION{
		mtl,lvl,ddo
	}	
	static enum N_DESIGNATION{
		junior,senior
	}
	static enum N_STATUS{
		active,terminated
	}
	static ArrayList<String> MANAGER_ACCOUNT = new ArrayList<String>(){
		{
			add("DDO10000");
			add("DDO10001");
			add("DDO10002");
		}
	};
	static Map<Character, ArrayList<RecordInfo>> HASH_TABLE = new HashMap<Character, ArrayList<RecordInfo>>(){
		{
			put('Z', new ArrayList<RecordInfo>(Arrays.asList(new RecordInfo("DR00005", new DoctorRecord("ten", "Zhang", "Montreal", "5142345678", "Surgery", "mtl")))));
			put('H', new ArrayList<RecordInfo>(Arrays.asList(new RecordInfo("NR00006", new NurseRecord("twelve", "Hu", "junior", "active", "2016/09/12")))));
		}
	};
	static ArrayList<RecordInfo> RECORD_LIST = null;
	static String HOST_NAME = "127.0.0.1";
	static String ORB_INITIAL_PORT = "1050";
	static String SERVER_NAME = "server_ddo";
	static int LOCAL_LISTENING_PORT = 6003;
	static Logger LOGGER = null;
	static FileHandler FH = null;
	
	static int SERVER_PORT_RECORDID_ASSIGN = 6000;
	static int SERVER_PORT_MTL = 6001;
	static int SERVER_PORT_LVL = 6002;
	static int SERVER_PORT_DDO = 6003;
}
