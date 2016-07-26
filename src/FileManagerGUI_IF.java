
// File Manager Interface to the GUI (handles User requests)
public interface FileManagerGUI_IF {
	public void addUserFile(String filename, String directory);
	public void getUserFile(String filename, String username);
	public void removeUserFile(String filename);
	public void updateUserFile(String filename, String directory);	
}
