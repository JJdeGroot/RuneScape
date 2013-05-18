package org.obduro.java.creator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class AccountCreator {
	
	// Creates a RuneScape account using the input age, email and password
	private boolean createAccount(String email, String password) throws IOException{
		// Storing the post values
		String[][] postVars = {{"age", "18"},
					            {"email1", email},
					            {"email2", email },
					            { "password1", password},
					            {"onlyOnePassword", "1"},
					            {"agree_pp_and_tac", "1"},
					            {"submit", "register"}};

        URL postURL = new URL("https://secure.runescape.com/m=account-creation/");
        URLConnection postURLConnect = postURL.openConnection();
        postURLConnect.setDoOutput(true);

        OutputStreamWriter out = new OutputStreamWriter(postURLConnect.getOutputStream());
        for(String[] postVar : postVars){
            out.write(postVar[0] + "=" + postVar[1] + "&");
        }
        out.flush();
        
        // Get the response
        BufferedReader br = new BufferedReader(new InputStreamReader(postURLConnect.getInputStream()));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }

        out.close();

		return true;
	}

	public static void main(String[] args) throws IOException{
		AccountCreator creator = new AccountCreator();
		creator.createAccount("verifier575+100@gmail.com", "test575");
	
	
	}
}
