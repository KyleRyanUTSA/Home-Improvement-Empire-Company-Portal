package application.model;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import javafx.scene.shape.Path;

public class CredentialLoader {
	
	
	
	
	public static ArrayList<Credential> loadCredentials() {
		InputStream is = CredentialLoader.class.getResourceAsStream("/Data/Credentials/Credentials");
		ArrayList<Credential> toReturn = new ArrayList<Credential>();
        if (is == null) {
            throw new IllegalStateException("Credentials.txt not found on classpath!");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
           
            while ((line = reader.readLine()) != null) {
                //System.out.println(line);
            	 String linesplit[] = line.split("\\|");
            	 if(linesplit.length == 4) {
            		 toReturn.add(new Credential(linesplit[0],linesplit[1],linesplit[2],linesplit[3]));
            	 }
            	 else {
            		 toReturn.add(new Credential(linesplit[0],linesplit[1],linesplit[2]));
            	 }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return toReturn;
    }
	public static void saveCredentials(Credential cred) {
		URL credentialsURL  = CredentialLoader.class.getResource("/Data/Credentials/Credentials");
		try {
			URI credentialsURI = credentialsURL.toURI();
			Writer credWriter = Files.newBufferedWriter(Paths.get(credentialsURI),StandardOpenOption.CREATE,StandardOpenOption.APPEND);
			
				credWriter.write("\n"+cred.getUsername()+"|"+cred.getPassword()+"|"+cred.getAddress()+cred.getPhoneNumber());
			
			credWriter.close();
			
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
}
