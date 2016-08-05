import java.io.File;

public interface FileManagerUserManager_IF {
	public void addUser(String username);
	public void removeUser(String username);
	public String generateInvitationFile(String IP);
	public String getIPFromInvitationFile(File invitation);

}
