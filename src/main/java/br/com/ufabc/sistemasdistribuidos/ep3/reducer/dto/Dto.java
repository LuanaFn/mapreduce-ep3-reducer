package br.com.ufabc.sistemasdistribuidos.ep3.reducer.dto;

import java.util.HashMap;
import java.util.List;

public class Dto {
	String host;
	int port;
	HashMap<String, List<String>> linksMap;
	public Dto(String host, int port, HashMap<String, List<String>> linksMap) {
		super();
		this.host = host;
		this.port = port;
		this.linksMap = linksMap;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public HashMap<String, List<String>> getLinksMap() {
		return linksMap;
	}
	public void setLinksMap(HashMap<String, List<String>> linksMap) {
		this.linksMap = linksMap;
	}
	public Dto() {
		super();
		// TODO Auto-generated constructor stub
	}
}
