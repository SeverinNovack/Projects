import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

public class HTTPServerTest {

    @Test
    public void RestAPI() throws IOException {

        /*
        HTTPServer server = new HTTPServer(3030);
        Thread thread = new Thread(server);
        thread.start();

        HttpUriRequest request = new HttpGet("http://localhost:3030/data");
        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        assertThat(response.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_OK));
        */
    }
}