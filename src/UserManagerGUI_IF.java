import java.io.File;

public interface UserManagerGUI_IF {
	public String generateInvitationFile() throws NoIPFoundException;
	public void createGroup(String username) throws NoIPFoundException;
	public void joinGroup(File invitation, String username) throws NoIPFoundException;
	public void close();
	
}
