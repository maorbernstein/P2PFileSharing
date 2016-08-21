import java.io.Closeable;

public abstract class FileTransferHandler extends Thread {
	protected FileManagerCoordinator_IF file_manager;
	protected UserManagerCoordinator_IF user_manager;
	protected GUINetworkCoordinator_IF gui;
	protected int num_chunks;
	protected String file_name;
	protected String user_name;
	protected static final int PORT = 5002;
	
	boolean compare(FileTransferHandler other){
		return ( (this.file_name == other.file_name) && (this.user_name == other.user_name) );
	}
	
	public void setInterfaces(FileManagerCoordinator_IF file_manager, UserManagerCoordinator_IF user_manager, GUINetworkCoordinator_IF gui){
		this.file_manager = file_manager;
		this.user_manager = user_manager;
		this.gui = gui;
	}
	
	public FileTransferHandler(int num_chunks, String file_name, String user_name){
		this.num_chunks = num_chunks;
		this.file_name = file_name;
		this.user_name = user_name;
	}
	
	
	public abstract void run();
	
	
}
