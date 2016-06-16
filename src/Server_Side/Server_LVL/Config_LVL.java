package Server_Side.Server_LVL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import Record_Type.NurseRecord;
import Record_Type.RecordInfo;

public class Config_LVL {
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
			add("LVL10000");
			add("LVL10001");
			add("LVL10002");
		}
	};
	static Map<Character, ArrayList<RecordInfo>> HASH_TABLE = new HashMap<Character, ArrayList<RecordInfo>>(){
		{
			put('W', new ArrayList<RecordInfo>(Arrays.asList(new RecordInfo("NR00003", new NurseRecord("seven", "Wang", "junior", "active", "2005/09/12")))));
			put('H', new ArrayList<RecordInfo>(Arrays.asList(new RecordInfo("NR00004", new NurseRecord("eight", "Hu", "junior", "active", "2016/09/12")))));
		}
	};
	static ArrayList<RecordInfo> RECORD_LIST = null;
	static String HOST_NAME = "127.0.0.1";
	static String ORB_INITIAL_PORT = "1050";
	static String SERVER_NAME = "server_lvl";
	static int LOCAL_LISTENING_PORT = 6002;
	static Logger LOGGER = null;
	static FileHandler FH = null;
	
	static int SERVER_PORT_RECORDID_ASSIGN = 6000;
	static int SERVER_PORT_MTL = 6001;
	static int SERVER_PORT_LVL = 6002;
	static int SERVER_PORT_DDO = 6003;
}
