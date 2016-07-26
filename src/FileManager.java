import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Map;
import utilities.Pair;


public class FileManager implements FileManagerGUI_IF, FileManagerCoordinator_IF {
	private static final String  PUBLIC_DIRECTORY = "./Public/";
	private static final String  GOLDEN_CHEST_DIRECTORY = "./GoldenChest/";
	private ArrayList<String>              golden_chest;
	private Map<String, ArrayList<String> > file_ledger;
	private Map<Pair<String, String>, FileInputStream>     pending_sending_files; // each entry is <username, filename>, fileinputstream
	private Map<Pair<String, String>, FileOutputStream>    pending_recving_files; // each entry is <username, filename>, fileoutputstream
	
	private static void copyFiles(String indir, String outdir) throws FileNotFoundException, IOException{
		int b;
		try (
				FileInputStream in = new FileInputStream(indir); 
				FileOutputStream out = new FileOutputStream(outdir);
		) {
			while((b = in.read()) != -1){
				out.write(b);
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
	
	public void addUserFile(String filename, String directory) {
		// If golden chest already has a file with this name, we update instead of add
		if(golden_chest.contains(filename)){
			updateUserFile(filename, directory);
			return;
		}
		try {
			copyFiles(directory + "/" + filename, GOLDEN_CHEST_DIRECTORY + filename);
			golden_chest.add(filename);
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
		// NetworkCoordinator: Send a BCAST removeFile Message
		// GUI: Notify file successfully removed
	}
	
	public void updateUserFile(String filename, String directory){
		if(golden_chest.contains(filename)){
			try {
				copyFiles(directory + "/" + filename, GOLDEN_CHEST_DIRECTORY + filename);
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
}
