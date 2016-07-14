
public interface NetworkCoordinator_IF {
	public void sendAddFile(String filename);
	public void sendAddUser(String username, String ip);
	public void sendModifyFile(String filename);
	public void sendGetFile(String filename);
	public void sendExit();
}
