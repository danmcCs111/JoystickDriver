import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpResponse;

public class HttpRequestJoy 
{
		public static String executePutRequest(
				String endpoint, 
				int portNumber, 
				String requestData, 
				String requestTypeHeaderName, 
				String requestTypeHeaderValue)
		{
			BodyPublisher bp = HttpRequest.BodyPublishers.ofString(requestData);
			
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(endpoint + portNumber))
					.header(requestTypeHeaderName, requestTypeHeaderValue)
					.method("PUT", bp)
					.build();
			HttpResponse<String> response = null;
			try {
				response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
				System.out.println(response.body());
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
			return (response==null)
				? null
				: response.body();
		}
}
