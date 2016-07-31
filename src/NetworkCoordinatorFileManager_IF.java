
public interface NetworkCoordinatorFileManager_IF {
	public ErrorCode broadcastAddFile(String filename);
	public ErrorCode broadcastModifyFile(String oldFilename, String newFilename);
	public ErrorCode sendGetFile(String filename);
	
	public ErrorCode rcvAddFile();
	public ErrorCode rcvModifyFile();
	public ErrorCode rcvGetFile();
}
