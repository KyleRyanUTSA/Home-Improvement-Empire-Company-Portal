package application.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileWriter;

public class CredentialVerifier {
	public static Credential verifyCredential(String username, String password) {
		ArrayList<Credential> Credentials = CredentialLoader.loadCredentials();
		
		for(int i=0;i<Credentials.size();i++) {
			//System.out.println(Credentials.get(i).getUsername());
			if(username.equals(Credentials.get(i).getUsername()) && password.equals(Credentials.get(i).getPassword())) {
				return Credentials.get(i);
			}
			
		}
		//saveCredentials(Credentials);
		return null;
		
	}
	public static Boolean userExists(String userToCheck) {
		ArrayList<Credential> Credentials = CredentialLoader.loadCredentials();
		
		for(int i=0;i<Credentials.size();i++) {
			//System.out.println(Credentials.get(i).getUsername());
			if(userToCheck.equals(Credentials.get(i).getUsername())) {
				return true;
			}
			
		}
		//saveCredentials(Credentials);
		return false;
		
	}
}
