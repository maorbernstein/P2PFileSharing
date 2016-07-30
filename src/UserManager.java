import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Map;

//import java.net.Socket;

public class UserManager implements UserManagerGUI_IF, UserManagerCoordinator_IF {
	private Map<String, String> user_ledger;
	private FileManagerUserManager_IF filemanager;
	
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
	
	public String generateInvitationFile() throws NoIPFoundException{
		String ip = getMyIP();
		if(ip == ""){
			throw new NoIPFoundException();
		}
		return filemanager.generateInvitationFile(ip);
	}
}
