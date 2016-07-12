import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


import java.util.ArrayList;
import java.util.Map;

public class FileManager implements FileManagerGUI_IF {
	private static final String  GOLDEN_CHEST_DIRECTORY = "./GoldenChest/";
	private ArrayList<String>              golden_chest;
	private Map<String, ArrayList<String> > file_ledger;
	private ArrayList<String>     pending_sending_files;
	private ArrayList<String>     pending_recving_files;
	
	private static void copyFiles(FileInputStream in, FileOutputStream out) throws IOException{
		int b;
		while((b = in.read()) != -1){
			out.write(b);
		}
	}
	
	private boolean isFileInLedger(String filename){
		for(Map.Entry<String, ArrayList<String> > entry: file_ledger.entrySet()){
			if(entry.getValue().contains(filename)){
				return true;
			}
		}
		return false;
	}
	
	public void addFile(String filename, String directory) {
		FileInputStream in = null;
		FileOutputStream out = null;
		// If golden chest already has a file with this name, we update instead of add
		if(golden_chest.contains(filename)){
			updateFile(filename, directory);
			return;
		}
		// If file is in ledger (meaning another user owns a file w/ this name), notify the user
		if(isFileInLedger(filename)){
			// GUI: Notify user such an action is not allowed
		}
		try {
			in = new FileInputStream(directory + "/" + filename);
			out = new FileOutputStream(GOLDEN_CHEST_DIRECTORY + filename);
			copyFiles(in, out);
			golden_chest.add(filename);
			// GUI: Notify Success
		} catch(FileNotFoundException e) {
			// GUI: Notify File not Found
		} catch(IOException e){
			// GUI: Notify IO Exception Occurred
		} finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("What the fuck...");
			}
		}
	}
	
	public void getFile(String filename){
		for(Map.Entry<String, ArrayList<String> > entry: file_ledger.entrySet()){
			if(entry.getValue().contains(filename)){
				// NetworkCoordinator: Send a BCAST getFile Message
				return;
			}
		}
		// GUI: Notify file not found in file ledger (aka file does not exist)
	}
	
	public void removeFile(String filename){
		if(!golden_chest.remove(filename)){
			// GUI: Notify file not found in golden chest
			return;
		}
		// NetworkCoordinator: Send a BCAST removeFile Message
		// GUI: Notify file successfully removed
	}
	
	public void updateFile(String filename, String directory){
		if(golden_chest.contains(filename)){
			FileInputStream in = null;
			FileOutputStream out = null;
			try {
				in = new FileInputStream(directory + "/" + filename);
				out = new FileOutputStream(GOLDEN_CHEST_DIRECTORY + filename);
				copyFiles(in, out);
				// NetworkCoordinator: Send a BCAST updateFile Message
				// GUI: Notify successful file update
			} catch (FileNotFoundException e) {
				// GUI: File not found
			} catch (IOException e){
				// GUI: IO Exception
			}
		}
	}
}
