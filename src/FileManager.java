import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Map;
import utilities.Pair;


public class FileManager implements FileManagerGUI_IF, FileManagerCoordinator_IF, FileManagerUserManager_IF {
	private static final String  PUBLIC_DIRECTORY = "./Public/";
	private static final String  GOLDEN_CHEST_DIRECTORY = "./GoldenChest/";
	private static final String INVITATION_FILE_NAME = "invitation.txt";
	private ArrayList<String>              golden_chest;
	private Map<String, ArrayList<String> > file_ledger;
	private Map<Pair<String, String>, FileInputStream>     pending_sending_files; // each entry is <username, filename>, fileinputstream
	private Map<Pair<String, String>, FileOutputStream>    pending_recving_files; // each entry is <username, filename>, fileoutputstream
	
	FileManager(){
		File golden_chest_dir = new File(GOLDEN_CHEST_DIRECTORY);
		golden_chest_dir.mkdirs();
		File public_dir = new File(PUBLIC_DIRECTORY);
		public_dir.mkdirs();
	}
	
	private static void copyFiles(File in_file, String outdir) throws FileNotFoundException, IOException{
		int b;
		try (
				FileInputStream in = new FileInputStream(in_file); 
				FileOutputStream out = new FileOutputStream(outdir);
		) {
			while((b = in.read()) != -1){
				out.write(b);
			}			
		}
	}
	
	public String generateInvitationFile(String IP){
		try (
		PrintStream out = new PrintStream(new FileOutputStream(PUBLIC_DIRECTORY + INVITATION_FILE_NAME));
		){
			out.print(IP);
		} catch (FileNotFoundException e){
			// Should never happen since constructor should be called before...
		}
		return (PUBLIC_DIRECTORY + INVITATION_FILE_NAME);
	}
	
	public void addNetworkFile(String filename, String username){
		if(file_ledger.containsKey(username)){
			// NetworkCoordinator: Notify user name requested not found
		} else {
			ArrayList<String> file_list = file_ledger.get(username);
			if(file_list.contains(filename)){
				// Network Coordinator: Notify file by this user already exists
				updateNetworkFile(filename, username);
			} else {
				file_list.add(filename);
			}
		}
	}
	
	public void writeNetworkFileInit(String username, String filename) {
		ArrayList<String> filenames = file_ledger.get(username);
		if(filenames == null){
			// NetworkCoordinator: Notify user name requested not found (NC - Filemanager Username Mismatch)
		}
		else if(!filenames.contains(filename)){
			// NetworkCoordinator: Notify file name requested not found (NC -Filemanager FileExistence Mismatch)
		}
		else {
			try {
				FileOutputStream out = new FileOutputStream(PUBLIC_DIRECTORY + username + "/" + filename);
				pending_recving_files.put(new Pair<String, String>(username, filename), out);
				// 
			} catch (FileNotFoundException e){
				// NetworkCoordinator: Notify filename requested not found
			} catch (SecurityException e){
				// NetworkCoordinator: File already open which throws a security exception
			}
		}
	}
	
	public void writeNetworkFileChunk(String username, String filename, byte[] bytes){
		Pair<String, String> p = new Pair<String, String>(username, filename);
		FileOutputStream out = pending_recving_files.get(p);
		if(out == null){
			// Network Coordinator: writeNetworkFileInit was not called properly
		} else {
			try {
				out.write(bytes);
				out.flush();
			} catch(IOException e){
				// Network Coordinator: IO Exception occurred.
			}
		}
	}
	
	public void writeNetworkFileDone(String username, String filename){
		Pair<String, String> p = new Pair<String, String>(username, filename);
		FileOutputStream out = pending_recving_files.get(p);
		if(out == null){
			// Network Coordinator: writeNetworkFileInit was not called properly
		} else {
			try {
				out.close();
			} catch(IOException e){
				// Network Coordinator: IO Exception occurred.
			}
		}
	}
	
	public void addUserFile(File f) {
		// If golden chest already has a file with this name, we update instead of add
		if(golden_chest.contains(f.getName())){
			updateUserFile(f);
			return;
		}
		try {
			copyFiles(f, GOLDEN_CHEST_DIRECTORY + f.getName());
			golden_chest.add(f.getName());
			// NetworkCoordinator: Send a BCAST message addFile
			// GUI: Notify Success
		} catch(FileNotFoundException e) {
			// GUI: Notify File not Found
		} catch(IOException e){
			// GUI: Notify IO Exception Occurred
		}
	}
	
	public void getUserFile(String filename, String username){
		ArrayList<String> filenames = file_ledger.get(username);
		if(filenames == null){
			// GUI: Notify user name requested not found (GUI - Filemanager Username Mismatch)
			return;
		}
		if(filenames.contains(filename)){
			// NetworkCoordinator: Send a getFile Message to username
		} else {
			// GUI: Notify file not found in the ledger (file does not exist)
		}
	}
	
	public void removeUserFile(String filename){
		if(!golden_chest.remove(filename)){
			// GUI: Notify file not found in golden chest
			return;
		}
		File f = new File(GOLDEN_CHEST_DIRECTORY + filename);
		f.delete();
		// NetworkCoordinator: Send a BCAST removeFile Message
		// GUI: Notify file successfully removed
	}
	
	public void updateUserFile(File f){
		if(golden_chest.contains(f.getName())){
			try {
				copyFiles(f, GOLDEN_CHEST_DIRECTORY + f.getName());
				// NetworkCoordinator: Send a BCAST updateFile Message
				// GUI: Notify successful file update
			} catch (FileNotFoundException e) {
				// GUI: File not found
			} catch (IOException e){
				// GUI: IO Exception
			}
			return;
		}
		// GUI: File not found in golden chest, call add file
	}
		
		
		public void updateNetworkFile(String filename,String username){
			if(!file_ledger.containsKey(username)){
				// Coordinator: Notify that username does not exist
			} else {
				ArrayList<String> file_list = file_ledger.get(username);
				if(file_list.contains(filename)){
					// GUI: Notify that file from user username has been updated
				} else {
					// Coordinator: Notify file name filename does not exist to user username
					addNetworkFile(filename, username);
				}
			}
		}
		
		 // public void removeNetworkFile(String filename, String username);
		
		public void removeNetworkFile(String filename, String username){
			
			 if(!file_ledger.containsKey(username)){
				// GUI: Notify user not found in file_ledger
				return;
			}else{
				ArrayList<String> file_list = file_ledger.get(username);
				if(!file_list.contains(filename)){
					// GUI: Notify file not found in ledger	
					return;
				}
				file_list.remove(filename);
			// GUI: Notify file successfully removed
		}
}
		
	// network read 
		
		public void readNetworkFileInit(String username, String filename) {
			if(!golden_chest.contains(filename)){
			 // NetworkCoordinator: Notify user name requested not found (NC - Filemanager Username Mismatch)
			}
			else {
				try {
					FileInputStream in = new FileInputStream(GOLDEN_CHEST_DIRECTORY + filename);
					pending_sending_files.put(new Pair<String, String>(username, filename), in);
					// 
				} catch (FileNotFoundException e){
					// NetworkCoordinator: Notify filename requested not found
				} catch (SecurityException e){
					// NetworkCoordinator: File already open which throws a security exception
				}
			}
		}
		
	
    public int readNetworkFileChunk(String username, String filename,byte[] bytes){
			Pair<String,String> p = new Pair<String, String>(username, filename);
			FileInputStream in = pending_sending_files.get(p);
			if(in == null){
				//  readNetworkFileChunk was not called properly
			} else {
				try {
					  return in.read(bytes);
					
				} catch(IOException e){
					// Network Coordinator: IO Exception occurred.
				}
			}
			return 0;
		}
		
		public void readNetworkFileDone(String username, String filename){
			Pair<String, String> p = new Pair<String, String>(username, filename);
			FileInputStream in = pending_sending_files.get(p);
			if(in == null){
				// Network Coordinator: writeNetworkFileInit was not called properly
			} else {
				try {
					in.close();
					pending_sending_files.remove(p);
				} catch(IOException e){
					// Network Coordinator: IO Exception occurred.
				}
			}
		}
		
		
}
		
