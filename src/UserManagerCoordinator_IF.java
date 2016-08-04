import java.util.Collection;
import java.util.Set;

public interface UserManagerCoordinator_IF {
	public void addNetworkUser(String username, String IP);
	public void removeNetworkUser(String username, String IP);
	public String getNetworkUserName(String IP);
	public String getNetworkUserIP(String username);
	public Collection<String> getAllIPs();
}
