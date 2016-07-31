import java.net.ServerSocket;
import java.util.Map;

public class NetworkCoordinator implements NetworkCoordinatorUserManager_IF, NetworkCoordinatorFileManager_IF {

	public ErrorCode sendInvitationFile(String email) {
		// Existing user send [invitation file] via SMTP to new user's email
		return ErrorCode.Failed;
	}
	
	public ErrorCode broadcastUserAdded() {
		// New user send [UserAdded, length, username;ip] to existing users in user_ledger
		return ErrorCode.Failed;
	}
	
	public ErrorCode rcvUserAdded() {
		// Existing user add new user's [username;ip] to user_ledger
		return ErrorCode.Failed;
	}
	
	public ErrorCode broadcastAddFile(String filename) {
		// User send [AddFile, length, filename;username] to users in user_ledger
		return ErrorCode.Failed;
	}

	public ErrorCode broadcastModifyFile(String oldFilename, String newFilename) {
		// User send [ModifyFile, length, oldFilename;newFilename] to users in user_ledger
		return ErrorCode.Failed;
	}
	
	public ErrorCode sendGetFile(String filename) {
		// User look up username associated with filename in user_ledger
		// User send [GetFile, length, filename;ip] to user with file
		return ErrorCode.Failed;
	}
	
	public ErrorCode rcvAddFile() {
		// User add new file's [filename;username] to file_ledger
		return ErrorCode.Failed;
	}
	
	public ErrorCode rcvModifyFile() {
		// User add new file's [newFilename;oldFilename.username] to file_ledger
		// User removes old file from file_ledger
		return ErrorCode.Failed;
	}
	
	public ErrorCode rcvGetFile() {
		// User send [FileTransfer, length, file] to [ip]
		return ErrorCode.Failed;
	}
	
	public ErrorCode rcvFileTransfer() {
		// User accepts incomming file transfer
		return ErrorCode.Failed;
	}
}
