import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Collection;

public class NetworkCoordinator implements Runnable, NetworkCoordinatorUserManager_IF, NetworkCoordinatorFileManager_IF {
	private static final int FILE_CHUNK_SIZE = 1024;
	private static final int HDR_LENGTH = 3;
	private static final int P2P_PORT = 5001;
	UserManagerCoordinator_IF usermanager;
	FileManagerCoordinator_IF filemanager;
	
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
				short message_length = (short)( (hdr[1] << 8) + hdr[2] );
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
	
	private void recvMsg(byte message_type, byte[] message, String IP){
		if(message_type == MESSAGE_TYPE.ADD_USER_BCAST.toByte()){
			String username = new String(message);
			usermanager.addNetworkUser(username, IP);
		} else if(message_type == MESSAGE_TYPE.ADD_FILE_BCAST.toByte()){
			String filename = new String(message);
			filemanager.addNetworkFile(filename, usermanager.getNetworkUserName(IP));
		}
	}
	
	private void sendMsg(byte message_type, short message_length, byte[] message, String IP){
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
	
	private void sendBcastMsg( byte message_type, short message_length, byte[] message){
		Collection<String> ips = usermanager.getAllIPs();
		for(String ip: ips){
			sendMsg(message_type, message_length, message, ip);
		}
	}
	
	private void sendAddFile(String filename, String username){
		byte[] message = new byte[2 + filename.length() + username.length()];
		message[0] = (byte)username.length();
		message[1] = (byte)filename.length();
		int i = 2;
		for(byte b: username.getBytes()){
			message[i++] = b;
		}
		for(byte b: filename.getBytes()){
			message[i++] = b;
		}
	}
	
	public void addFile(String filename){
		byte[] message = filename.getBytes();
		byte message_type = MESSAGE_TYPE.ADD_FILE_BCAST.toByte();
		sendBcastMsg(message_type, (short)filename.length(), message);
	}
	
	public void getFile(String username, String filename){
		byte[] message = filename.getBytes();
		byte message_type = MESSAGE_TYPE.GET_FILE.toByte();
		String ip = usermanager.getNetworkUserIP(username);
		sendMsg(message_type, (short)filename.length(), message, ip);
	}
}
