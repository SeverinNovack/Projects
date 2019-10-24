import java.io.BufferedReader;
import java.io.IOException;

/**
 * <p>HTTPRequest class.</p>
 *
 * @author serwar
 * @version $Id: $Id
 */
public class HTTPRequest {


    public String method, url, protocolVersion, cache, connection, useragent, accept;

    /**
     * <p>Constructor for HTTPRequest.</p>
     *
     * @param input a {@link java.io.BufferedReader} object.
     * @throws java.io.IOException if any.
     */
    public HTTPRequest(BufferedReader input) throws IOException {

        String nextLine = input.readLine();
        boolean firstLine = true;
        while (nextLine != null && !nextLine.equals("")) {

            if (firstLine) {
                firstLine = false;

                this.method = nextLine.split(" ")[0];
                this.url = nextLine.split(" ")[1];
                this.protocolVersion = nextLine.split(" ")[2];


            } else {
                int separatorIndex = nextLine.indexOf(":");
                if (separatorIndex != -1) {
                    String key = nextLine.substring(0, separatorIndex);
                    String value = nextLine.substring(separatorIndex + 1);

                    switch (key) {
                        case "Connection":
                            this.connection = value;
                            break;
                        case "User-Agent":
                            this.useragent = value;
                            break;
                        case "Accept":
                            this.accept = value;
                            break;
                        case "Cache-Control":
                            this.cache = value;
                            break;
                    }
                }
            }
            nextLine = input.readLine();
        }
    }

}

