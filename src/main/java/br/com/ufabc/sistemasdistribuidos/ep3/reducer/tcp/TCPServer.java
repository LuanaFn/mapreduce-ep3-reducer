package br.com.ufabc.sistemasdistribuidos.ep3.reducer.tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import br.com.ufabc.sistemasdistribuidos.ep3.reducer.bo.ReducerBO;

public class TCPServer {
	private ServerSocket server;
	int mappers;

	public TCPServer(String ipAddress, int mappers) throws Exception {
		this.mappers = mappers;

		if (ipAddress != null && !ipAddress.isEmpty())
			this.server = new ServerSocket(8082, 1, InetAddress.getByName(ipAddress));
		else
			this.server = new ServerSocket(8082, 1, InetAddress.getLocalHost());
	}

	private void listen() throws Exception {
		String data = null;
		Socket client = this.server.accept();
		String clientAddress = client.getInetAddress().getHostAddress();
		System.out.println("\r\nNew connection from " + clientAddress);

		BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		StringBuilder builder = new StringBuilder();
		while ((data = in.readLine()) != null) {
			System.out.println("\r\nMessage from " + clientAddress + ": " + data);

			builder.append(data);
		}
		
		//cria um bo a cada mensagem recebida para evitar problema de farol nas vari�veis
		ReducerBO reducerbo = new ReducerBO(mappers);
		reducerbo.recebeLinks(builder.toString());
	}

	public InetAddress getSocketAddress() {
		return this.server.getInetAddress();
	}

	public int getPort() {
		return this.server.getLocalPort();
	}

	public static void main(String[] args) throws Exception {
		System.out.println("REDUCER");
		TCPServer app;

//		if (args.length > 0)
			// proprio ip e n de mappers 
			app = new TCPServer("http://ec2-34-229-109-240.compute-1.amazonaws.com/", 3);
//		else
//			app = new TCPServer(null, 1);

		System.out.println(
				"\r\nRunning Server: " + "Host=" + app.getSocketAddress().getHostAddress() + " Port=" + app.getPort());

		while (true)
			app.listen();
	}
}
