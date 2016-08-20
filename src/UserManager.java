import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;
import java.util.Map.Entry;

//import java.net.Socket;

public class UserManager implements UserManagerGUI_IF, UserManagerCoordinator_IF {
	private String own_user_name;
	private Map<String, String> user_ledger; // mapping from username to ip
	private FileManagerUserManager_IF filemanager;
	private NetworkCoordinatorUserManager_IF net_coordinator;
	private GUIUserManager_IF gui;

	UserManager()
	{
		own_user_name = "";
		user_ledger = new HashMap<String, String>();
	}

	public void linkGui(P2PFSGui g) {
		gui = g;
	}

	public void linkNC(NetworkCoordinator n) {
		net_coordinator = n;
	}

	public void linkFM(FileManager f) {
		filemanager = f;
	}


	private void showState(){
		System.out.println("Own username = " + own_user_name);
		System.out.println("User ledger = {");
		for(Entry<String, String> e: user_ledger.entrySet() ){
			System.out.print(e.getKey() + ":" + e.getValue() + ", ");
		}
		System.out.println(" }");
	}
	
	static public String getMyIP(){
	    try {
	        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
	        while (interfaces.hasMoreElements()) {
	            NetworkInterface iface = interfaces.nextElement();
	            // filters out 127.0.0.1 and inactive interfaces
	            if (iface.isLoopback() || !iface.isUp())
	                continue;

	            Enumeration<InetAddress> addresses = iface.getInetAddresses();
	            while(addresses.hasMoreElements()) {
	                InetAddress addr = addresses.nextElement();
	                if(addr instanceof Inet4Address){
		                return addr.getHostAddress();
	                }
	            }
	        }
	    } catch (SocketException e) {
	        // Should never happen
	    }
	    return "";
	}
	
	public Collection<String> getAllIPs(){
		return user_ledger.values();
	}
	
	public Set<String> getAllUsernames(){
		return user_ledger.keySet();
	}
	
	public boolean addNetworkUser(String username, String IP) {
		if(user_ledger.containsKey(username) || (username == own_user_name) ){
			// Notify Coordinator that user name already exists
			return false;
		}
		user_ledger.put(username, IP);
		filemanager.addUser(username);
		gui.addUser(username);
		showState();
		return true;
	}
	
	public void removeNetworkUser(String username, String ip){
		showState();
		if(!user_ledger.containsKey(username)){
			// Notify Coordinator that user name does not exist
		} else if(user_ledger.get(username) != ip){
			// Notify Coordinator that this user name does not have that ip
		} else{
			user_ledger.remove(username);
			filemanager.addUser(username);
			gui.removeUser(username);
		}
	}
	
	public String getNetworkUserIP(String username){
		showState();
		if(!user_ledger.containsKey(username)){
			// Notify Coordinator that username does not exist
			return "";
		}
		return user_ledger.get(username);
	}
	
	public String getNetworkUserName(String IP){
		showState();
		if(!user_ledger.containsValue(IP)){
			// Notify Coordinator that IP does not exist
			return "";
		}
		for(Map.Entry<String, String> entry: user_ledger.entrySet()){
			if(entry.getValue() == IP){
				return entry.getKey();
			}
		}
		return "";
	}
	
	@Override
	public void joinGroup(File invitation, String username) throws NoIPFoundException{
		own_user_name = username;
		showState();
		String join_ip;
		try {
			join_ip = filemanager.getIPFromInvitationFile(invitation);
		} catch (NoSuchElementException e){
			throw new NoIPFoundException();
		}

		if(join_ip == ""){
			throw new NoIPFoundException();
		}
		net_coordinator.joinGroup(username, join_ip);
	}
	
	public void createGroup(String username){
		own_user_name = username;
		showState();
	}
	
	public String generateInvitationFile() throws NoIPFoundException{
		String ip = getMyIP();
		showState();
		if(ip == ""){
			throw new NoIPFoundException();
		}
		return filemanager.generateInvitationFile(ip);
	}
	
	public void close(){
		showState();
		net_coordinator.exit();
	}

	public String getMyUsername() {
		return own_user_name;
	}

	@Override
	public boolean isUsernameTaken(String username) {
		showState();
		if(user_ledger.containsKey(username) || (username == own_user_name) ){
			// Notify Coordinator that user name already exists
			return false;
		}
		return true;
	}
}
