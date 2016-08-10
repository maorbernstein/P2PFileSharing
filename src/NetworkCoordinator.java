import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;

public class NetworkCoordinator implements Runnable, NetworkCoordinatorUserManager_IF, NetworkCoordinatorFileManager_IF {
	private static final int FILE_CHUNK_SIZE = 1024;
	private static final int HDR_LENGTH = 3;
	private static final int P2P_PORT = 5001;
	UserManagerCoordinator_IF usermanager;
	FileManagerCoordinator_IF filemanager;
	GUINetworkCoordinator_IF gui;
	
	private enum MESSAGE_TYPE {
		// BCASTS
		ADD_USER_BCAST(0), REMOVE_USER_BCAST(1), ADD_FILE_BCAST(2), UPDATE_FILE_BCAST(3), REMOVE_FILE_BCAST(4),
		// Join Network
		JOIN_GROUP(5), USERNAME_TAKEN(6), ADD_USER_SINGLE(7), ADD_FILE_SINGLE(8),
		// Get File
		GET_FILE(9), SEND_FILE(10);
		
		private final byte _type;
		
		MESSAGE_TYPE(int b){
			this._type = (byte)b;
		}
		
		byte toByte() {
			return _type;
		}
	};
	
	private void sendMsg(byte message_type, int message_length, byte[] message, String IP){
		byte[] frame = new byte[message_length + HDR_LENGTH];
		frame[0] = message_type;
		frame[1] = (byte) ( (message_length & 0xff00) >> 8 );
		frame[2] = (byte) ( (message_length & 0x00ff) >> 0 );
		for(int i = HDR_LENGTH; i < HDR_LENGTH + message_length; i++){
			frame[i] = message[i - HDR_LENGTH];
		}
		Socket s = new Socket();
		try{
			s.connect(new InetSocketAddress(IP, P2P_PORT), 10);
			OutputStream o = s.getOutputStream();
			o.write(frame);
			o.close();
			s.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private void sendBcastMsg( byte message_type, int message_length, byte[] message){
		Collection<String> ips = usermanager.getAllIPs();
		for(String ip: ips){
			sendMsg(message_type, message_length, message, ip);
		}
	}
	
	// UserManager BCASTS
	public void addUserBcast(String username) {
		byte[] message = username.getBytes();
		byte message_type = MESSAGE_TYPE.ADD_USER_BCAST.toByte();
		sendBcastMsg(message_type, message.length, message);
	}
	
	public void removeUserBcast(String username) {
		byte[] message = username.getBytes();
		byte message_type = MESSAGE_TYPE.REMOVE_USER_BCAST.toByte();
		sendBcastMsg(message_type, message.length, message);
	}
	
	// UserManager Join Network
	public void joinGroup(String username, String IP) {
		byte[] message = username.getBytes();
		byte message_type = MESSAGE_TYPE.JOIN_GROUP.toByte();
		sendMsg(message_type, message.length, message, IP);
	}
	
	public void usernameTaken(String username, String IP) {
		byte[] message = username.getBytes();
		byte message_type = MESSAGE_TYPE.USERNAME_TAKEN.toByte();
		sendMsg(message_type, message.length, message, IP);
	}
	
	public void addUserSingle(String username, String IP) {
		byte[] message = username.getBytes();
		byte message_type = MESSAGE_TYPE.ADD_USER_SINGLE.toByte();
		sendMsg(message_type, message.length, message, IP);
	}
	
	public void addFileSingle(String filename, String IP) {
		byte[] message = filename.getBytes();
		byte message_type = MESSAGE_TYPE.ADD_FILE_SINGLE.toByte();
		sendMsg(message_type, message.length, message, IP);
	}
	
	// FileManager BCASTS
	public void addFileBcast(String filename) {
		byte[] message = filename.getBytes();
		byte message_type = MESSAGE_TYPE.ADD_FILE_BCAST.toByte();
		sendBcastMsg(message_type, message.length, message);
	}
	
	public void updateFileBcast(String filename) {
		byte[] message = filename.getBytes();
		byte message_type = MESSAGE_TYPE.UPDATE_FILE_BCAST.toByte();
		sendBcastMsg(message_type, message.length, message);
	}
	
	public void removeFileBcast(String filename) {
		byte[] message = filename.getBytes();
		byte message_type = MESSAGE_TYPE.REMOVE_FILE_BCAST.toByte();
		sendBcastMsg(message_type, message.length, message);
	}
	
	// FileManager Get File
	public void getFile(String username, String filename){
		byte[] message = filename.getBytes();
		byte message_type = MESSAGE_TYPE.GET_FILE.toByte();
		sendMsg(message_type, message.length, message, usermanager.getNetworkUserIP(username));
	}
	
	public void sendFile() {
		
	}
	
	// Receive Messages
	@Override
	public void run(){
		try(
			ServerSocket listening_socket = new ServerSocket();
		){
			while(true){
				Socket new_socket = listening_socket.accept();
				InputStream in = new_socket.getInputStream();
				byte[] hdr = new byte[HDR_LENGTH];
				in.read(hdr);
				int message_length = (int)( (hdr[1] << 8) + hdr[2] );
				byte[] message = new byte[message_length];
				in.read(message);
				byte message_type = hdr[0];
				String IP = new_socket.getInetAddress().toString();
				recvMsg(message_type, message, IP);
				in.close();
				new_socket.close();
			}
		}catch(IOException e){
		}
	}
	
	private void recvMsg(byte message_type, byte[] message, String IP) {
		if (message_type == MESSAGE_TYPE.ADD_USER_BCAST.toByte()) {
			usermanager.addNetworkUser(message.toString(), IP);
			// TODO: Send owned file to message.toString()
			ArrayList<String> ownedFiles = (ArrayList<String>) filemanager.getOwnFiles();
			for (String ownedFile : ownedFiles) {
				addFileSingle(ownedFile, message.toString());
			}
		} 
		else if (message_type == MESSAGE_TYPE.REMOVE_USER_BCAST.toByte()) {
			usermanager.removeNetworkUser(message.toString(), IP);
			// TODO: Remove user's files from file ledger
			filemanager.removeAllNetworkFile(message.toString());
		} 
		else if (message_type == MESSAGE_TYPE.ADD_FILE_BCAST.toByte()) {
			filemanager.addNetworkFile(message.toString(), usermanager.getNetworkUserName(IP));
		} 
		else if (message_type == MESSAGE_TYPE.UPDATE_FILE_BCAST.toByte()) {
			filemanager.updateNetworkFile(message.toString(), usermanager.getNetworkUserName(IP));
		} 
		else if (message_type == MESSAGE_TYPE.REMOVE_FILE_BCAST.toByte()) {
			filemanager.removeNetworkFile(message.toString(), usermanager.getNetworkUserName(IP));
		} 
		else if (message_type == MESSAGE_TYPE.JOIN_GROUP.toByte()) {
			// TODO: Check if username is taken
			if (usermanager.isUsernameTaken(message.toString())) {
				usernameTaken(message.toString(), IP);
			}
			else {
				addUserBcast(message.toString());
				// TODO: Send N addUserSingle()
			}
		} 
		else if (message_type == MESSAGE_TYPE.USERNAME_TAKEN.toByte()) {
			// TODO: Notify UserManager that username is taken 
			gui.connectionStatus(true, false);
		} 
		else if (message_type == MESSAGE_TYPE.ADD_USER_SINGLE.toByte()) {
			usermanager.addNetworkUser(message.toString(), usermanager.getNetworkUserName(IP));
		} 
		else if (message_type == MESSAGE_TYPE.ADD_FILE_SINGLE.toByte()) {
			filemanager.addNetworkFile(message.toString(), usermanager.getNetworkUserName(IP));
		} 
		else if (message_type == MESSAGE_TYPE.GET_FILE.toByte()) {
			// TODO: Send file
		}
		else if (message_type == MESSAGE_TYPE.SEND_FILE.toByte()) {
			// TODO: Write to file
		}
		else {
			throw new IllegalArgumentException("Invalid Message Type");
		}
	}
}
