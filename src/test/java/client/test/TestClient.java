package client.test;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class TestClient {
	public static void main(String[] args) {
		Client client = Client.create();

		WebResource web = client.resource("http://localhost:8888/jaxrs.jersey/student/getAll");
		client.addFilter(new HTTPBasicAuthFilter("admin", "admin"));

		ClientResponse response = web.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		if (response.getStatus() != 200) {
			return;
		}

		String output = response.getEntity(String.class);
		System.out.println(output);

	}
}
