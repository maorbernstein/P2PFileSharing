
public interface NetworkCoordinatorUserManager_IF {
	
	// BCASTS
	public void addUserBcast(String username);
	public void removeUserBcast(String username);
	
	// Join Network
	public void joinGroup(String username, String IP);
	public void usernameTaken(String username, String IP);
	public void addUserSingle(String username, String IP);
	public void addFileSingle(String filename, String IP);
}
