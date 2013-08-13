/**
 * The objective of program is to study about protocol communication only.
 * Please do not use to difference objective. The author will not responsible 
 * anything about the damage coming from this program.
 */


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Tunnel extends Thread implements Runnable {
	ServerSocket tunnelServerSock = null;
	ChannelSessionInfo channelSessionInfo[] = null;
	String forwardHost = "127.0.0.1";
	int forwardPort = 8192;
	int tunnelServerPort = 8202; 
	public Tunnel(String forwardHost, int forwardPort, int tunnelPort){
		this.forwardHost = forwardHost;
		this.forwardPort = forwardPort;
		this.tunnelServerPort = tunnelPort;
		try {
			tunnelServerSock = new ServerSocket(this.tunnelServerPort);
			tunnelServerSock.setReuseAddress(true);
			if((forwardHost.equalsIgnoreCase("127.0.0.1") || forwardHost.equalsIgnoreCase("localhost") || forwardHost.equalsIgnoreCase(this.tunnelServerSock.getInetAddress().getHostName()) || forwardHost.equalsIgnoreCase(this.tunnelServerSock.getInetAddress().getCanonicalHostName()) || forwardHost.equalsIgnoreCase(this.tunnelServerSock.getInetAddress().getHostAddress())) && forwardPort == tunnelPort){
				System.out.println("Forwarding optional error please check. Please specify in different port between the forwading port and tunnel channel port at forwarding host["+forwardHost+"]");
				System.exit(1);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}
	public Tunnel(ChannelSessionInfo channelSessionInfo[]){
		this.channelSessionInfo = channelSessionInfo;
		this.openSeveralTunnelChannel();
	}
	private void openSeveralTunnelChannel(){
		try{
			for(ChannelSessionInfo info : channelSessionInfo){
				(new Tunnel(info.nodeName, info.nodePort, info.tunnelChannelPort)).start();
			}
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}
	public void run(){
		if(this.channelSessionInfo != null){
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			while(true){
				try {
					String readLn = input.readLine();
					if(readLn.trim().equalsIgnoreCase("exit")){
						System.exit(0);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else{
			this.mainTunnel();
		}
	}
	private void mainTunnel(){
		Socket realClientSideIncoming = null;
		
		
		//String serverChannel = "serverChannel";
		//String channelType = "channelType";
		
		/*ServerSocketChannel ssc = null;
		Selector socketChannelSelector = null;
		SelectionKey socketServerSelectionKey = null;
		Map properties;
		try {
			ssc = ServerSocketChannel.open();
			ssc.bind(new InetSocketAddress(arg[0],forwardPort));
			tunnelServerSock.getChannel().accept();
			ssc.configureBlocking(false);
			realClientSideIncoming = tunnelServerSock.accept();
		
			socketChannelSelector = Selector.open();
			socketServerSelectionKey = ssc.register(socketChannelSelector, SelectionKey.OP_ACCEPT);
		
			properties = new HashMap();
			properties.put(channelType, serverChannel);
			socketServerSelectionKey.attach(properties);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
        
        
		for (;;) {
			try {
				
				/*if (socketChannelSelector.select() == 0)
	                continue;
				
				Set selectedKeys = socketChannelSelector.selectedKeys();
	            Iterator iterator = selectedKeys.iterator();
	            while (iterator.hasNext()) {
	            	SelectionKey key = (SelectionKey) iterator.next();
	            	
	                if (((Map) key.attachment()).get(channelType).equals(
	                        serverChannel)) {
	                	ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
	                    // accept the new connection on the server socket. Since the
	                    // server socket channel is marked as non blocking
	                    // this channel will return null if no client is connected.
	                    SocketChannel clientSocketChannel = serverSocketChannel.accept();
	                }
	            }*/
				
				realClientSideIncoming = tunnelServerSock.accept();
				System.out.println("sss");
				System.out.println( this.tunnelServerSock.getInetAddress().getHostAddress()  + " " + this.tunnelServerSock.getInetAddress().getCanonicalHostName() + " " + this.tunnelServerSock.getInetAddress().getHostName());
				if((forwardHost.equalsIgnoreCase("127.0.0.1") || forwardHost.equalsIgnoreCase("localhost") || forwardHost.equalsIgnoreCase(this.tunnelServerSock.getInetAddress().getHostName()) || forwardHost.equalsIgnoreCase(this.tunnelServerSock.getInetAddress().getCanonicalHostName()) || forwardHost.equalsIgnoreCase(this.tunnelServerSock.getInetAddress().getHostAddress())) && forwardPort == this.tunnelServerPort){
					System.out.println("Forwarding optional error please check. Please specify in different port between the forwading port and tunnel channel port at forwarding host["+forwardHost+"]");
					System.out.println("This channel will be closed!");
					this.tunnelServerSock.close();
					break;
				}else{
					new TunnelChannel(forwardHost, forwardPort, realClientSideIncoming).start();
					
					System.out.println("qqq");
				}
			} catch (Exception e) {
				System.out.println("MAIN: " + e);
				//System.exit(1);
				//break;
			}
		}
		/*try {
			tunnelServerSock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}*/
		// t.openConnection();
		// t.main_proc();
	}
	

	// main
	public static void main(String[] arg) {
		String forwardHost = "127.0.0.1";
		int forwardPort = 8192;
		int tunnelServerPort = 8203;
		
		try {
			switch (arg.length % 3) {
			case 0:
				int nodeNum = (arg.length)/3;
				ChannelSessionInfo channelSessionInfo[] = new ChannelSessionInfo[nodeNum];
				forwardHost = arg[0];
				forwardPort = Integer.parseInt(arg[1]);
				for(int i = 0; i < arg.length/3; i++){
					forwardHost = arg[i*3];
					forwardPort = Integer.parseInt(arg[i*3+1]);
					tunnelServerPort= Integer.parseInt(arg[i*3+2]);
					channelSessionInfo[i] = new ChannelSessionInfo(forwardHost, forwardPort, tunnelServerPort);
				}
				Tunnel mainTunnelStartor = new Tunnel(channelSessionInfo);
				mainTunnelStartor.start();
				break;
			default:
				System.out.println("usage:java Tunnel [<forwarding host name[n]> <forwarding port number[n]> <tunnel channel port[n]>] . . .");
				System.exit(0);
			}
		} catch (Exception e) {
			System.out.println("BEGIN phase....ERR");
			e.printStackTrace();
			System.exit(1);
		}
		
		//Tunnel mainTunnelStartor = new Tunnel(forwardHost, forwardPort, tunnelServerPort);
		
		
	}
}
class ChannelSessionInfo{
	String nodeName = "";
	int nodePort = 0;
	int tunnelChannelPort = 0;
	ChannelSessionInfo(String nodeName, int nodePort, int tunnelChannelPort){
		this.nodeName = nodeName;
		this.nodePort = nodePort;
		this.tunnelChannelPort = tunnelChannelPort;
	}
}


class TunnelChannel extends Thread implements Runnable {
	public Socket forwardSideSock;
	public OutputStream forwardSideServerOutput;
	public BufferedInputStream forwardSideServerInput;

	Socket realClientSideSock = null;
	public OutputStream realClientSideOutput;
	public BufferedInputStream realClientSideInput;

	public String host;
	public int port;

	public TunnelChannel(String host, int port, Socket realClientSideSock) {
		this.init();
		this.host = host;
		this.port = port;
		this.realClientSideSock = realClientSideSock;
	}

	private void init() {
		this.forwardSideServerInput = null;
		this.forwardSideServerOutput = null;
		this.forwardSideSock = null;

		this.realClientSideInput = null;
		this.realClientSideOutput = null;
		this.realClientSideSock = null;
	}

	public boolean openForwardSideConnection(Socket RealClientSideSock,
			Socket forwardSideSock) {
		boolean ret = false;

		try {

			this.forwardSideSock = forwardSideSock = new Socket(host, port);
			this.forwardSideServerOutput = this.forwardSideSock.getOutputStream();
			this.forwardSideServerInput = new BufferedInputStream(
					this.forwardSideSock.getInputStream());
			
			this.realClientSideOutput = this.realClientSideSock.getOutputStream();
			this.realClientSideInput = new BufferedInputStream(this.realClientSideSock.getInputStream());
			if((host.equalsIgnoreCase("127.0.0.1") || host.equalsIgnoreCase("localhost") || host.equalsIgnoreCase(this.realClientSideSock.getInetAddress().getHostName()) || host.equalsIgnoreCase(this.realClientSideSock.getInetAddress().getCanonicalHostName()) || host.equalsIgnoreCase(this.realClientSideSock.getInetAddress().getHostAddress())) && port == this.realClientSideSock.getLocalPort()){
				System.out.println("Forwarding optional error please check. Please specify in different port between the forwading port and tunnel channel port at forwarding host["+port+"]");
				System.out.println("All channels will be closed!");
				System.exit(1);
			}else{
				ret = true;
			}
		} catch (UnknownHostException e) {
			System.err
					.println("openForwardSideConnection UnknownHostException Error : "
							+ e);
		} catch (IOException e) {
			System.err.println("openForwardSideConnection IOException Error : "
					+ e);
		}
		return ret;
	}

	public void tunnelMainProc() {
		Thread input_thread = null;
		Thread output_thread = null;
		StreamConnector stdin_to_socket = null;
		StreamConnector socket_to_stdout = null;
		
		try {
			stdin_to_socket = new StreamConnector(
					this.realClientSideInput,
					forwardSideServerOutput, this.host, this.port, "streamin", this);
			System.out.println("OK created stdin_to_socket client name: " + this.realClientSideSock.getInetAddress().getHostName() + ", " + this.realClientSideSock.getInetAddress().getHostAddress() + ", " + this.realClientSideSock.getInetAddress().getCanonicalHostName() + ", " + new String(this.realClientSideSock.getInetAddress().getAddress()));
			
			socket_to_stdout = new StreamConnector(
					forwardSideServerInput,
					this.realClientSideOutput, this.host,
					this.port, "streamout", this);
			System.out.println("OK created socket_to_stdout");
			System.out.println("forward: " +this.forwardSideSock.getSoTimeout() + " realClient: " + this.realClientSideSock.getSoTimeout());


			if(stdin_to_socket != null){
				input_thread = new Thread(stdin_to_socket);
				stdin_to_socket.addThreadObj(input_thread);
				input_thread.start();
			}
			
			if(socket_to_stdout != null){
				output_thread = new Thread(socket_to_stdout);
				socket_to_stdout.addThreadObj(output_thread);
				output_thread.start();
			}
		} catch (Exception e) {
			System.err.print("main_proc: " + e);
			e.printStackTrace();
			// System.exit(1);
			System.out.println("tunnelMainProc.....<<Checked");
			this.destroyTunnelSession();
		}
	}

	public void destroyTunnelSession() {
		try {
			if(this.realClientSideSock !=null){
				if(!this.realClientSideSock.isClosed()){
					if(this.realClientSideSock.getInputStream() != null){
						this.realClientSideSock.getInputStream().close();
						this.realClientSideSock.getInputStream().reset();
					}
					if(this.realClientSideSock.getOutputStream() != null){
						this.realClientSideSock.getOutputStream().close();
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		try{
			if(this.forwardSideSock != null){
				if(!this.forwardSideSock.isClosed()){
					if(this.forwardSideSock.getInputStream() != null){
						this.forwardSideSock.getInputStream().close();
					}
					if(this.forwardSideSock.getOutputStream() != null){
						this.forwardSideSock.getOutputStream().close();
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if(this.realClientSideSock != null){
				this.realClientSideSock.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			if(this.forwardSideSock != null){
				this.forwardSideSock.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	

	private boolean createTunnelSession() {
		boolean isOpenForwardSideConn = this.openForwardSideConnection(
				realClientSideSock, this.forwardSideSock);
		return isOpenForwardSideConn;
	}

	public void run() {
		if (this.createTunnelSession()) {
			this.tunnelMainProc();
		} else {
			System.out.println("Cannot create tunnel session");
			this.destroyTunnelSession();
		}
	}

	

}



// StreamConnector
class StreamConnector implements Runnable {
	InputStream src = null;
	OutputStream dist = null;
	String host = "";
	String type = "";
	int port = 23;
	boolean isDeaded = false;
	Thread threadObj = null;
	TunnelChannel tunnelChannel;

	public StreamConnector(InputStream in, OutputStream out, String host,
			int port, String type, TunnelChannel tunnelChannel) {
		src = in;
		dist = out;
		this.host = host;
		this.port = port;
		this.type = type;
		this.isDeaded = false;
		this.tunnelChannel = tunnelChannel;
	}
	
	public void addThreadObj(Thread threadObj){
		this.threadObj = threadObj;
	}

	public void run() {
		//byte[] buff = new byte[8192];
		byte[] buff = new byte[4096];
		while (true) {
			
			try {
				int n = src.read(buff);
				if (n > 0) {
					//System.out.println(this.type + " (will be write out): " + new String(buff));
					//System.out.println("n="+n);
					dist.write(buff, 0, n);
					dist.flush();
				}else{
					dist.flush();
					src.reset();
				}
				
			} catch (Exception e) {
				if (this.type.equals("streamin")) {
					System.out.println("Error: streamin");
				} else if (this.type.equals("streamout")) {
					System.out.println("Error: streamout");
				}
				this.tunnelChannel.destroyTunnelSession();
				System.out.println("Connection to host lost.");
				this.isDeaded = true;
				break;
			}
			
			if(this.tunnelChannel.forwardSideSock.isClosed() || this.tunnelChannel.forwardSideSock.isInputShutdown() || this.tunnelChannel.forwardSideSock.isOutputShutdown()
					|| this.tunnelChannel.realClientSideSock.isClosed() || this.tunnelChannel.realClientSideSock.isInputShutdown() || this.tunnelChannel.realClientSideSock.isOutputShutdown()){
				System.out.println("StreamConnector crashed[" + this.type + "]");
				this.tunnelChannel.destroyTunnelSession();
				break;
			}
		}
		try {
			System.out.println("END of StreamConnector");
			this.finalize();
			this.threadObj.stop();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
