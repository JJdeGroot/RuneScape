package org.obduro.mole;

public class General {
	private static long lastKillTime = System.currentTimeMillis() - 120000;

	public static void setKillTime(long time){
		lastKillTime = time;
	}
	
	public static long getKillTime(){
		return lastKillTime;
	}

}