package application.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileWriter;

public class CredentialVerifier {
	public static boolean verifyCredential(Credential credToVerify) {
		ArrayList<Credential> Credentials = CredentialLoader.loadCredentials();
		
		for(int i=0;i<Credentials.size();i++) {
			//System.out.println(Credentials.get(i).getUsername());
			if(credToVerify.getUsername().equals(Credentials.get(i).getUsername()) && credToVerify.getPassword().equals(Credentials.get(i).getPassword())) {
				return true;
			}
			
		}
		//saveCredentials(Credentials);
		return false;
		
	}
}
