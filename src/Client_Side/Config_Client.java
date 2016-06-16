package Client_Side;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

import DSMS_CORBA.DSMS;

/**
 * this is configuration class
 * @author jpl19
 *
 */
public class Config_Client {
	static String MANAGER_ID = null;
	static String HOST = "127.0.0.1";
	static DSMS DSMS_IMPL = null;
	static String ORB_INITIAL_PORT = "1050";
	static int SERVER_PORT_MTL = 6001;
	static int SERVER_PORT_LVL = 6002;
	static int SERVER_PORT_DDO = 6003;
	
	static Logger LOGGER = null;
	static FileHandler FH = null;
}