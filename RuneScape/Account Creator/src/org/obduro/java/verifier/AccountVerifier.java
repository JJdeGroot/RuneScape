package org.obduro.java.verifier;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

public class AccountVerifier {
	private final String saveLocation = generateLocation();
	private final long startTime = System.currentTimeMillis();
	
	private String username, password;
	private Store store;
	private int done = 0;
	
	public AccountVerifier(String username, String password){
		this.username = username;
		this.password = password;
	}

	// Returns the calendar data as a String eg. Account Verifier(13-01-2013 17-44-16)
	private String generateLocation(){
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy HH-mm-ss");
		Date date = new Date();
		String location = System.getProperty("user.home");

		return location = location + "\\Account Verifier (" + sdf.format(date) + ").txt";
	}
	
	// Appends text to a file, or creates a new file.
	private boolean appendToFile(String text){
		System.out.println("Adding " + text + " to: " + saveLocation);
		
		try {
		    FileWriter fw = new FileWriter(saveLocation, true);
		    fw.write(text);
		    fw.write("\r\n");
		    fw.close();
		    return true;
		} catch(IOException e) {
		    System.out.println("Failed to create or append to " + saveLocation);
		}
		
		return false;
	}

	// Logs in to a gmail account
	public boolean login(){
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		Session session = Session.getDefaultInstance(props, null);
		try {
			store = session.getStore("imaps");
			store.connect("imap.gmail.com", username, password);
		} catch (MessagingException e) {
			System.out.println("Wrong username and/or password");
			return false;
		}
		return true;
	}
	
	// Gets the messages
	private Message[] getMessages(){
		try {
			Folder inbox = store.getFolder("Inbox");
			inbox.open(Folder.READ_ONLY);
			return inbox.getMessages();
		} catch (MessagingException e) {
			System.out.println("Failed to retrieve messages from your inbox");
		}
		return null;
	}
	
	// Grabs the activation link
	private String getActivationLink(String src){
		return src.substring(src.indexOf("https://"), src.indexOf("js=1") + 4);
	}
	
	private String getName(String src){
		return src.substring(src.indexOf("address=")+8, src.indexOf("&js=1")).replace("%40", "@").replace("%2B", "+");
	}

	// Navigates to a certain link
	private boolean navigateTo(String link) {
		System.out.println("Verifying " + link);
		
		try {
			URL url = new URL(link);
	        URLConnection con = url.openConnection();
	        
	        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	        while (in.readLine() != null) {}
		} catch (Exception e){
			System.out.println("Failed to navigate to: " + link);
		}

        return true;
	}
	
	// Returns the content of a message as a String
	private String getContent(Message message){
		StringBuilder sb = new StringBuilder();
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(message.getInputStream()));
			String line;
			while((line = reader.readLine()) != null){
				sb.append(line);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	// Goes through all the emails for information
	private void activate() {
		Message[] messages = getMessages();
		if(messages != null){
			for(Message message : messages) {
				System.out.println("---- MESSAGE START ----");
				
				String content = getContent(message);
				System.out.println("Content: " + content);
				
				if(content.contains("created your RuneScape Account")){
					String link = getActivationLink(content);
					System.out.println("Activation link: " + link);
					
					String name = getName(link);
					System.out.println("Account name: " + name);
					
					if(!navigateTo(link) || !appendToFile(name)){
						System.out.println("Terminating script due to errors");
						break;
					}
					
				    done++;
				    System.out.println("Elapsed time: " + (System.currentTimeMillis() - startTime));
				    System.out.println("Verified: " + done);
				    double doneH = ((double)(System.currentTimeMillis() - startTime) / 3600000) * done;
				    System.out.println("Verified " + done + " accounts at " + doneH + " per hour");
				}
				
				System.out.println("---- MESSAGE END ----");
			}
		}
	}
}