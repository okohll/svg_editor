package fr.itris.glips.rtda.rtdp;

import java.util.*;
import java.util.concurrent.*;
import fr.itris.glips.rtda.*;
import java.io.*;
import java.net.*;

/**
 * the first real time provider
 * @author ITRIS, Jordi SUC
 */
public class ServerRealTimeDataProvider extends RealTimeDataProvider{
	
	/**
	 * the IP address of the server
	 */
	private String serverIPAddress="";
	
	/**
	 * the handler of the animations
	 */
	private AnimationsHandler handler;
	
	/**
	 * the set of the names of the data that should be listened to
	 */
	private Set<String> dataNames=new CopyOnWriteArraySet<String>();
	
	/**
	 * the server socket
	 */
	private ServerSocket serverSocket;
	
	/**
	 * the thread for the server
	 */
	private Thread serverThread;
	
	/**
	 * the port of the socket
	 */
	private int port=4012;
	
	/**
	 * the set of all the sockets that have been created
	 */
	private Set<Socket> sockets=new CopyOnWriteArraySet<Socket>();
	
	/**
	 * the constructor of the class
	 * @param handler the handler of the animations
	 * @param IPAddress the IP address of the server
	 */
	public ServerRealTimeDataProvider(AnimationsHandler handler, String IPAddress){

		this.handler=handler;
		this.serverIPAddress=IPAddress;
		handleServerSocket();
	}
	
	/**
	 * handles the socket
	 */
	protected void handleServerSocket() {
		
		serverThread=new Thread(){
			
			@Override
			public void run() {
				
				//whether the socket has been opened
				boolean socketOpened=false;

				while(! socketOpened) {
					
					try{
						serverSocket=new ServerSocket(port);
						socketOpened=true;
					}catch (Exception ex){
						
						try{
							if(serverSocket!=null) {

								serverSocket.close();
							}
						}catch (Exception e) {}

						port++;
					}
					
					try{
						sleep(250);
					}catch(Exception ex){}
				}
				
				while(true){
					
					//creating the socket input stream
					try {
						Socket socket=serverSocket.accept();
						handleSocket(socket);
					}catch (Exception ex) {}
					
					try{
						sleep(250);
					}catch(Exception ex){}
				}
			}
		};
		
		serverThread.start();
	}
	
	/**
	 * handles the actions on the provided socket
	 * @param socket a socket
	 */
	protected void handleSocket(final Socket socket){
		
		if(socket!=null){

			sockets.add(socket);
			
			Thread thread=new Thread(){
				
				@Override
				public void run() {
					
					InputStream in=null;
					BufferedReader reader=null;
					
					try{
						in=socket.getInputStream();
						reader=new BufferedReader(new InputStreamReader(in));
					}catch (Exception ex){ex.printStackTrace();}

					if(reader!=null){
						
						String line="";
						String[] splitLine=null;
						
						while(! socket.isClosed()) {

							try {
								line=reader.readLine();

								if(line!=null){

									line=line.trim();
									splitLine=line.split("=");

									if(splitLine!=null && splitLine.length==2){

										if(handler.isDebugMode()){
											
											System.out.println("receiving tag="+splitLine[0]+" "+splitLine[1]);
										}
										
										setDataValueForAnimationsHandler(splitLine[0], 
												splitLine[1].equals(AnimationsHandler.invalidKeyWord)?null:splitLine[1]);//TODO
									}
									
								}else{
									
									break;
								}

							}catch (Exception e){
								line=null;
								splitLine=null;
								e.printStackTrace();
								break;
							}
						}

						invalidateAllTags();
					}
				}
			};
			
			thread.start();
		}
	}
	
	@Override
	public void dispose(){

		//sending in all the sockets an end message
		try{
			for(Socket socket : sockets){
				
				if(socket!=null && ! socket.isClosed()){
					
					OutputStream out=socket.getOutputStream();
					Writer writer=new BufferedWriter(new OutputStreamWriter(out));
					writer.write("+++\n");
					writer.flush();
					writer.close();
				}
			}
		}catch (Exception ex){ex.printStackTrace();}
		
		try{
			serverSocket.close();
		}catch (Exception ex){ex.printStackTrace();}
		
		for(Socket socket : sockets){
			
			if(socket!=null &&! socket.isClosed()){
				
				try{
					socket.close();
				}catch (Exception ex){ex.printStackTrace();}
			}
		}
	}
	
	@Override
	public String getDataProviderId() {
		
		return serverIPAddress;
	}
	
	@Override
	public void addDataToListen(String name) {

		dataNames.add(name);
	}
	
	@Override
	public void removeDataToListen(String name) {

		dataNames.remove(name);
	}
	
	@Override
	public void setDataValueForAnimationsHandler(String name, Object newValue) {
		
		handler.setData(name, newValue);
	}
	
	@Override
	public void setDataValue(final String name, final Object newValue) {
		
		Thread thread=new Thread(){
			
			@Override
			public void run() {
				
				if(serverIPAddress!=null && ! serverIPAddress.equals("")){
					
					try{
						String urlStr="http://"+serverIPAddress+":8080"+
							"/settag?Tag="+name+"&Value="+newValue;
						URL url=new URL(urlStr);
						
						HttpURLConnection connection=(HttpURLConnection)url.openConnection();
						connection.setRequestMethod("GET");
						
						if(handler.isDebugMode()){
							
							System.out.println("sending tag="+name+" "+newValue);
						}

						connection.connect();
						connection.getContent();
					}catch (Exception ex) {ex.printStackTrace();}
				}
			}
		};
		
		thread.start();
	}
	
    @Override
    public void loadView(final String path) {

		Thread thread=new Thread(){
			
			@Override
			public void run() {
				
				if(serverIPAddress!=null && ! serverIPAddress.equals("")){
					
					try{
						String urlStr="http://"+serverIPAddress+
							":8080"+"/loadview?View="+path+"&Port="+port;

						URL url=new URL(urlStr);
						
						HttpURLConnection connection=(HttpURLConnection)url.openConnection();
						connection.setRequestMethod("GET");
						
						if(handler.isDebugMode()){
							
							System.out.println("loading view="+path);
						}

						connection.connect();
						connection.getContent();
					}catch (Exception ex) {
						
						//invalidates all the tags
						invalidateAllTags();
						ex.printStackTrace();
					}	
				}
			}
		};
		
		thread.start();
    }
    
    @Override
    public void unLoadView(final String path) {
    	
		if(serverIPAddress!=null && ! serverIPAddress.equals("")){
			
			Thread thread=new Thread(){
				
				@Override
				public void run() {
					
					try{
						String urlStr="http://"+serverIPAddress+
							":8080"+"/unloadview?View="+path+"&Port="+port;
						URL url=new URL(urlStr);
						
						HttpURLConnection connection=(HttpURLConnection)url.openConnection();
						connection.setRequestMethod("GET");
						
						if(handler.isDebugMode()){
							
							System.out.println("unloading view="+path);
						}

						connection.connect();
						connection.getContent();
					}catch (Exception ex) {ex.printStackTrace();}		
				}
			};
			
			thread.start();
		}
    }
    
    /**
     * invalidates all the tags
     */
    protected void invalidateAllTags() {
    	
    	for(String name : dataNames) {
    		
    		setDataValueForAnimationsHandler(name, null);
    	}
    }
}
