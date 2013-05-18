package scripts;

import java.net.*;
import java.io.*;

public class SuperheatPrices {
	// Gets the GE price, credit to random people on the internet for the code idea
	public int getPrice(int id) throws IOException { 
        String price;
        URL url = new URL("http://open.tip.it/json/ge_single_item?item=" + id);
        URLConnection con = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            if (line.contains("mark_price")){
        		int start = line.indexOf("mark_price") + 13;
        		int end = line.indexOf("daily_gp") - 3;
        	    price = line.substring(start, end);
        	    price = price.replace(",", "");
        	    
        	    
        	    try {
        	    	return Integer.parseInt(price);
	            } catch (NumberFormatException e) {
	                return -1;
	            }
            }
        }
        return -1;
	}
}