package org.obduro.java.proxy;

import java.net.Proxy;

public class ProxyInfo {
	private String ip;
	private int port;
	private Proxy.Type type;
	
	public ProxyInfo(String ip, int port, String type){
		this.ip = ip;
		this.port = port;
		if(type.equals("HTTP") || type.equals("HTTPS")){
			this.type = Proxy.Type.HTTP;
		}else{
			this.type = Proxy.Type.SOCKS;
		}
	}
	
	public String getIP(){
		return this.ip;
	}
	
	public int getPort(){
		return this.port;
	}
	
	public Proxy.Type getType(){
		return this.type;
	}
	
	@Override
	public String toString(){
		return "IP: " + ip + " | Port: " + port + " | Type: " + type;
	}
}
