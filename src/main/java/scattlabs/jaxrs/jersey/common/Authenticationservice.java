package scattlabs.jaxrs.jersey.common;

import java.util.StringTokenizer;

import org.apache.commons.codec.binary.Base64;

public class Authenticationservice {
	public boolean authenticate(String authCredentials){
		if (authCredentials == null) {
			return false;
		}
		
		final String encodeedUserPassword = authCredentials.replaceFirst("Basic" +" ","");
		String usernameAndPassword = null;
		try {
			byte[] decodedBytes = Base64.decodeBase64(encodeedUserPassword);
			usernameAndPassword = new String(decodedBytes, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
		final String username = tokenizer.nextToken();
		final String password = tokenizer.nextToken();
		
		boolean authenticationStatus = "admin".equals(username) && "admin".equals(password);
		return authenticationStatus;
	}
}