package br.com.ufabc.sistemasdistribuidos.ep3.reducer.tcp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClient {
	private Socket socket;

	public TCPClient(String host, int serverPort) throws Exception {
		if (host == null)
			this.socket = new Socket(InetAddress.getLocalHost(), serverPort);
		else
			this.socket = new Socket(InetAddress.getByName(host), serverPort);
	}

	public void close() throws IOException {
		socket.close();
	}

	public void enviaMensagem(String msg) throws Exception {

		System.out.println("Enviando para "+socket.getRemoteSocketAddress()+":"+socket.getPort()+": " + msg);

		// envia as urls pro coordenador
		PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
		out.println(msg);
		out.flush();
	}
}
