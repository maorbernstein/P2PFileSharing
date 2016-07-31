
public interface NetworkCoordinatorUserManager_IF {
	public ErrorCode sendInvitationFile(String email);
	
	public ErrorCode broadcastUserAdded();
	
	public ErrorCode rcvUserAdded();
}
