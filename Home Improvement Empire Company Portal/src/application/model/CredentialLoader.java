package application.model;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
            	 toReturn.add(new Credential(linesplit[0],linesplit[1]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return toReturn;
    }
}
