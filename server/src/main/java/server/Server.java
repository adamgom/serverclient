package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import server.session.SessionsManager;
import server.session.SessionsRemover;

public class Server {

	static {
		Properties properties = System.getProperties();
		try {
			properties.load(ClassLoader.getSystemResourceAsStream("server.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Server();
	}

	public Server() {
		System.out.println("Server start");
		ServicesManager.getInstance();
		SessionsManager.getInstance();
		new Thread(new SessionsRemover(15)).start();
		
		try (ServerSocket server = new ServerSocket(Integer.valueOf(System.getProperty("server.port")))) {
			Executor executor = Executors.newFixedThreadPool(Integer.valueOf(System.getProperty("server.pool")));
			while(true) {
				Socket socket = server.accept();
				executor.execute(new ProcessConnection(socket));
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}