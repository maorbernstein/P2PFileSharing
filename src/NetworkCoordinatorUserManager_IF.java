
public interface NetworkCoordinatorUserManager_IF {

	public void joinGroup(String username, String IP);
	public void exit();

	public ErrorCode sendInvitationFile(String email);
	
	public ErrorCode broadcastUserAdded();
	
	public ErrorCode rcvUserAdded();

}
