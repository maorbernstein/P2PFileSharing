
public interface NetworkCoordinatorFileManager_IF {

	public void addFile(String filename);
	public void getFile(String username, String filename);
	public void updateFile(String filename);
	public void removeFile(String filename);

	public ErrorCode broadcastAddFile(String filename);
	public ErrorCode broadcastModifyFile(String oldFilename, String newFilename);
	public ErrorCode sendGetFile(String filename);
	
	public ErrorCode rcvAddFile();
	public ErrorCode rcvModifyFile();
	public ErrorCode rcvGetFile();

}
