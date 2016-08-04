import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

//import java.net.Socket;

public class UserManager implements UserManagerGUI_IF, UserManagerCoordinator_IF {
	private String own_user_name;
	private Map<String, String> user_ledger; // mapping from username to ip
	private FileManagerUserManager_IF filemanager;
	private NetworkCoordinatorUserManager_IF net_coordinator;
	
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
	
	public boolean addNetworkUser(String username, String IP) {
		if(user_ledger.containsKey(username) || (username == own_user_name) ){
			// Notify Coordinator that user name already exists
			return false;
		}
		user_ledger.put(username, IP);
		filemanager.addUser(username);
		// GUI: Notify that new user has been added
		return true;
	}
	
	public void removeNetworkUser(String username, String ip){
		if(!user_ledger.containsKey(username)){
			// Notify Coordinator that user name does not exist
		} else if(user_ledger.get(username) != ip){
			// Notify Coordinator that this user name does not have that ip
		} else{
			user_ledger.remove(username);
			filemanager.addUser(username);
			// GUI: Notify that user has been removed
		}
	}
	
	public String getNetworkUserIP(String username){
		if(!user_ledger.containsKey(username)){
			// Notify Coordinator that username does not exist
			return "";
		}
		return user_ledger.get(username);
	}
	
	public String getNetworkUserName(String IP){
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
	
	public void joinGroup(File invitation, String username){
		own_user_name = username;
		String join_ip = filemanager.getIPFromInvitationFile(invitation);
		if(join_ip == ""){
			// Nofiy GUI that invitation file is invalid
		}
		net_coordinator.joinGroup(username, join_ip);
	}
	
	public void createGroup(String username){
		own_user_name = username;
	}
	
	public String generateInvitationFile(){
		String ip = getMyIP();
		if(ip == ""){
			// Nofiy GUI that my IP not found
		}
		return filemanager.generateInvitationFile(ip);
	}
	
	public void close(){
		net_coordinator.exit();
	}

	public String getMyUsername() {
		return own_user_name;
	}
}
