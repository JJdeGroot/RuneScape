package org.obduro.java.proxy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProxyHandler {
	
	private ArrayList<ProxyInfo> proxyList;
	
	public ProxyHandler(){
		proxyList = new ArrayList<ProxyInfo>();
	}

	private String getSource(String link){
		StringBuilder sb = new StringBuilder();
		
		try {
			URL url = new URL(link);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			
			String line;
			while((line = in.readLine()) != null){
				sb.append(line);
			}
		} catch (Exception e) {
			System.out.println("Failed to get source from: " + link);
		}

		return sb.toString();
	}
	
	public void getProxys(){
		//String source = getSource("http://www.vpngeeks.com/proxylist.php?country=0&port=&speed[]=3&anon[]=3&type[]=1&type[]=2&conn[]=3&sort=1&order=1&rows=200&search=Find+Proxy");
		String source = getSource("http://www.vpngeeks.com/proxylist.php?country=0&port=&speed[]=3&anon[]=3&type[]=1&type[]=2&type[]=3&conn[]=3&sort=3&order=1&rows=200&search=Find+Proxy");
		// Page 2 = http://www.vpngeeks.com/proxylist.php?from=201&country=0&port=&speed[]=3&anon[]=3&type[]=1&type[]=2&type[]=3&conn[]=3&sort=3&order=1&rows=200&search=Find+Proxy#pagination
		Pattern p = Pattern.compile("(.*?)<td>(([0-9]{1,3}\\.){3}[0-9]{1,3})</td>(.*?)<td>(([0-9]{1,4}))</td>(.*?)<td>(.*?)((socks4/5|HTTP|HTTPS))</td>");
		Matcher matcher = p.matcher(source);
		
		while(matcher.find()){
			String ip = matcher.group(2);
			int port = Integer.parseInt(matcher.group(6));
			String type = matcher.group(9);
			
			//System.out.println("IP: " + ip + " | Port: " + port + " | Type: " + type);
			proxyList.add(new ProxyInfo(ip, port, type));
		}
		
		//System.out.println("ProxyList: " + proxyList.toString());
	}
	
	public void connect(){
		try {
			ProxyInfo info = proxyList.get(3);
			
			System.out.println("Using info: " + info);
			
			Proxy proxy = new Proxy(info.getType(), new InetSocketAddress(info.getIP(), info.getPort()));
			URLConnection conn = new URL("http://ipchicken.com/").openConnection(proxy);
			conn.setConnectTimeout(10000);
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String line;
			while((line = in.readLine()) != null){
				System.out.println(line);
			}

		} catch (Exception e) {
			System.out.println("Failed to connect");
		}
	}
	
	public void registerAccount(String email, String password){
		// Storing the post values
		String[][] postVars = {{"onlyOneEmail", "1"},
							   {"age", "18"},
					           {"email1", email},
					           {"password1", password},
					           {"password2", password},
					           {"agree_pp_and_tac", "1"},
					           {"submit", "Join+Free+Now"}};
		try {
	        ProxyInfo info = proxyList.get(66);
	        Proxy proxy = new Proxy(info.getType(), new InetSocketAddress(info.getIP(), info.getPort()));
	        System.out.println("info: " + info);

	        URL postURL = new URL("https://secure.runescape.com/m=account-creation/create_account_funnel.ws");
	        URLConnection postURLConnect = postURL.openConnection(proxy);
	        postURLConnect.setDoOutput(true);
	       
	        System.out.println("Connected");
	        
	        OutputStreamWriter out = new OutputStreamWriter(postURLConnect.getOutputStream());
	        for(String[] postVar : postVars){
	            out.write(postVar[0] + "=" + postVar[1] + "&");
	        }
	        out.flush();
	        
	        System.out.println("Reading input stream");
	        // Get the response
	        BufferedReader br = new BufferedReader(new InputStreamReader(postURLConnect.getInputStream()));
	        String line;
	        StringBuilder sb = new StringBuilder();
	        while ((line = br.readLine()) != null) {
	            System.out.println(line);
	            sb.append(line);
	        }
	        
	        out.close();
	        
	        if(sb.toString().contains("Your RuneScape account has been created")){
	        	System.out.println("Succesfully created an account!");
	        }
	
	        
		} catch (Exception e) {
			System.out.println("Registration failed");
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		ProxyHandler ph = new ProxyHandler();
		ph.getProxys();
		//ph.connect();
		
		ph.registerAccount("epyyy672z@gmail.com", "jj234234jvvva4");

		
	}
	
	
}
