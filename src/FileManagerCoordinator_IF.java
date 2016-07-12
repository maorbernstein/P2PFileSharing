
public interface FileManagerCoordinator_IF {
	public void addFile(String filename, String username);
	public void updateFile(String filename);
	public void writeFileInit(String filename);
	public void writeFileChunk(String filename);
	public void writeFileDone(String filename);
	public void readFileInit(String filename);
	public void readFileChunk(String filename);
	public void readFileDone(String filename);
	
}
