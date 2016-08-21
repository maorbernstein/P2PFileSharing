import java.net.InetAddress;
import java.util.Collection;
import java.util.Set;

public interface UserManagerCoordinator_IF {
	public boolean addNetworkUser(String username, InetAddress IP);
	public void removeNetworkUser(String username, InetAddress IP);
	public String getMyUsername();
	public String getNetworkUserName(InetAddress IP);
	public InetAddress getNetworkUserIP(String username);
	public Collection<InetAddress> getAllIPs();
	public Set<String> getAllUsernames();
}
