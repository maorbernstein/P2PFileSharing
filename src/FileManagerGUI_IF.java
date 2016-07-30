import java.io.File;

// File Manager Interface to the GUI (handles User requests)
public interface FileManagerGUI_IF {
	public void addUserFile(File f);
	public void getUserFile(String filename, String username);
	public void removeUserFile(String filename);
	public void updateUserFile(File f);	
}
