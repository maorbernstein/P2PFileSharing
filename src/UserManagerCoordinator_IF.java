
public interface UserManagerCoordinator_IF {
	public void addNetworkUser(String username, String IP);
	public void removeNetworkUser(String IP);
	public void getNetworkUserName(String IP);
	public void getNetworkUserIP(String username);
}
