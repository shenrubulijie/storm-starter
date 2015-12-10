package scheduler;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import storm.scheduler.Profile;


public class Master {
	
	private static final Logger LOG = LoggerFactory
			.getLogger(Master.class);
	public static HashMap<String, Profile> profile_map;
	private static Master instance;
	
	private Master(){
		profile_map = new HashMap<String, Profile>();
		try{
			this.start();
		} catch (IOException ex) {
			
		}
	}
	
	public static Master getInstance() {
		if(instance==null) {
			instance=new Master();
		}
		return instance;
	}
	
	public void start() throws IOException{
	//public static void start() throws IOException{
		LOG.info("Cluster Stats Monitoring Server started...");
		Thread t=new Thread(new ServerThread());
		t.start();
	}

}

class ServerThread implements Runnable{

	private static final Logger LOG = LoggerFactory
			.getLogger(Master.class);
	
	public void run() {
		// TODO Auto-generated method stub
		
		int port = 6789;
		ServerSocket socket;
		try {
			socket = new ServerSocket(port, 10);
			Socket connection;
			while(true){
				connection=socket.accept();
				LOG.info("Waiting for connection...");
				LOG.info("Connection received from " + connection.getInetAddress().getHostName());
				ServerWorker worker=new ServerWorker(connection);
				worker.run();			
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}

class ServerWorker implements Runnable{
	
	private Socket connection;
	//private ObjectInputStream in;
	//private ObjectOutputStream out;
	private static final Logger LOG = LoggerFactory
			.getLogger(Master.class);

	public ServerWorker(Socket connection) throws IOException {
		// TODO Auto-generated constructor stub
		this.connection=connection;
		//this.in=new ObjectInputStream(this.connection.getInputStream());
		//this.out=new ObjectOutputStream(this.connection.getOutputStream());
	}

	
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("master in run...");
		try {
			//this.out.flush();
			
			//receive profile
			BufferedReader in = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));  
			String msg = in.readLine();  
			//Object obj=this.in.readObject();
			//String ip=obj.toString();
			
			LOG.info("host msg address: "+msg);
			System.out.println(msg);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
}