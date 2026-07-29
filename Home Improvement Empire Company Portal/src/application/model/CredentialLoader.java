package application.model;

import java.io.BufferedReader;
import java.nio.file.Path;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;


public class CredentialLoader {




	public static ArrayList<Credential> loadCredentials() {
	    ArrayList<Credential> toReturn = new ArrayList<Credential>();
	    Path credentialsPath = Paths.get("src", "Data", "Credentials", "Credentials.txt");

	    if (!Files.exists(credentialsPath)) {
	        throw new IllegalStateException("Credentials.txt not found at: " + credentialsPath.toAbsolutePath());
	    }

	    try (BufferedReader reader = Files.newBufferedReader(credentialsPath)) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            if (line.isBlank()) {
	                continue;
	            }
	            //System.out.println(line);
	            String linesplit[] = line.split("\\|");
	            if (linesplit.length == 4) {
	                toReturn.add(new Credential(linesplit[0], linesplit[1], linesplit[2], linesplit[3]));
	            } else if (linesplit.length == 3) {
	                toReturn.add(new Credential(linesplit[0], linesplit[1], linesplit[2], "000000000"));
	            } else {
	                System.out.println("Skipping malformed line: " + line);
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return toReturn;
	}
	
    public static void saveCredentials(Credential cred) {
        Path credentialsPath = Paths.get("src", "Data", "Credentials", "Credentials.txt");
        try {
            Files.createDirectories(credentialsPath.getParent());
            try (Writer credWriter = Files.newBufferedWriter(
                    credentialsPath, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                credWriter.write("\n" + cred.getUsername() + "|" + cred.getPassword() + "|"
                        + cred.getAddress() + "|" + cred.getPhoneNumber());
                credWriter.close();
            }
            System.out.println("Saved to: " + credentialsPath.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean updateCredential(Credential updatedCredential) {
        if (updatedCredential == null) {
            return false;
        }

            URL credentialsURL = CredentialLoader.class.getResource("/Data/Credentials/Credentials.txt");

        if (credentialsURL == null) {
            System.err.println("Credentials file could not be found.");
            return false;
        }

        try {
            URI credentialsURI = credentialsURL.toURI();
                ArrayList<Credential> credentials = loadCredentials();
                boolean userFound = false;

            for (Credential credential : credentials) {
                if (credential.getUsername().equals(updatedCredential.getUsername())) {
                        credential.setAddress(updatedCredential.getAddress());
                        credential.setPhoneNumber(updatedCredential.getPhoneNumber());
                        userFound = true;
                     break;
                }
            }

            if (!userFound) {
                return false;
            }

            List<String> updatedLines = new ArrayList<>();

                for (Credential credential : credentials) {
                    updatedLines.add(

                        credential.getUsername()
                                + "|"
                                + credential.getPassword()
                                + "|"
                                + credential.getAddress()
                                + "|"
                                + credential.getPhoneNumber()
                );
            }

            Files.write(
                    Paths.get(credentialsURI),
                    updatedLines,

                    StandardCharsets.UTF_8
            );

            return true;

        } catch (Exception e) {


            e.printStackTrace();
            return false;
        }
    }
}