package org.obduro.java;

public class ScriptFolder {

	private OS os;
	
	public ScriptFolder(){
		// "user.home"  slash ".tribot" slash "bin" slash "scripts"
		
		
		String osName = System.getProperty("os.name");
		System.out.println("OS Name: " + osName);
		
		if(osName.startsWith("Windows")){
			os = OS.WINDOWS;
		}else if(osName.startsWith("Mac")){
			os = OS.MACINTOSH;
		}else{
			os = OS.LINUX;
		}
		
		System.out.println("OS: " + os);
	}
	
	public String getLocation() {
		return os.getScriptFolderLocation();
	}
	
	public enum OS {
		WINDOWS(System.getenv("APPDATA") + "\\.tribot\\bin\\scripts"),
		MACINTOSH(""),
		LINUX("");
		
		private final String scriptFolderLocation;
		
		private OS(String scriptFolderLocation){
			this.scriptFolderLocation = scriptFolderLocation;
		}
		
		public String getScriptFolderLocation(){
			return scriptFolderLocation;
		}
	}
	
}
