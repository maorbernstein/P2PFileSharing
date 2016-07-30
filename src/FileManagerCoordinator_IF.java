
// File Manager Interface to the NetworkCoordinator (handles Network requests)
public interface FileManagerCoordinator_IF {
	public void addNetworkFile(String filename, String username);
	public void updateNetworkFile(String filename, String username);
	public void removeNetworkFile(String filename, String username);
	public void writeNetworkFileInit(String username, String filename);
	public void writeNetworkFileChunk(String username, String filename, byte[] bytes);
	public void writeNetworkFileDone(String username, String filename);
	public void readNetworkFileInit(String filename);
	public byte[] readNetworkFileChunk(String filename);
	public void readNetworkFileDone(String filename);
	
}
