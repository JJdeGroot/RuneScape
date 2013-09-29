package scripts.jjsherblore;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HerbStatsThread extends Thread {
	
	private String link;
	private String parameters;
	
	public HerbStatsThread(String link, String parameters){
		this.link = link;
		this.parameters = parameters;
		setDaemon(true);
	}
	
	/**
	 * Sends post data to a certain link
	 * @param link Link to send the post data to
	 * @param parameters Parameters to send
	 * @throws Exception When failed
	 */
	private void sendStatistics(String link, String parameters) throws Exception {
		URL obj = new URL(link);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		// Add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "Script Data");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(parameters);
		wr.flush();
		wr.close();

		// Read response
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
	}
	
	@Override
	public void run() {
		try {
			sendStatistics(link, parameters);
			System.out.println("Comitted: " + parameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
