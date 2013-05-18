package org.obduro.toymouse;

public class Timer {

	private long startTime;
	private long ms;
	
	public Timer(long ms){
		this.startTime = System.currentTimeMillis();
		this.ms = ms;
	}
	
	public boolean isRunning(){
		return System.currentTimeMillis() < startTime+ms;
	}
	
	public void reset(){
		startTime = System.currentTimeMillis();
	}
	
}
