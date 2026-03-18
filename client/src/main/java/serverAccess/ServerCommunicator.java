package serverAccess;
import com.google.gson.JsonParser;
import dataaccess.AlreadyTakenException;
import dataaccess.UnauthorizedException;
import server.Server;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;

public class ServerCommunicator {
    private Server server;

    private static final int TIMEOUT_MILLIS = 5000;
    private static final String HOST = "localhost";

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public ServerCommunicator(Server server){
        this.server = server;
    }

    public void stop(){
        this.server.stop();
    }

    public String getErrorMessage(HttpResponse<String> httpResponse){
        return JsonParser.parseString(httpResponse.body()).getAsJsonObject().get("message").getAsString();
    }

    public HttpResponse<String> handleResponse(HttpResponse<String> httpResponse) throws UnauthorizedException, AlreadyTakenException, RuntimeException {
        switch (httpResponse.statusCode()){
            case 200:
                return httpResponse;
            case 401:
                throw new UnauthorizedException(getErrorMessage(httpResponse));
            case 403:
                throw new AlreadyTakenException(getErrorMessage(httpResponse));
            default:
                throw new RuntimeException();
        }
    }

    public HttpResponse<String> get(String header, String urlPath)  throws URISyntaxException, IOException, InterruptedException, UnauthorizedException, AlreadyTakenException {
        String urlString = String.format("http://%s:%d%s", HOST, this.server.getPort(), urlPath);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(TIMEOUT_MILLIS))
                .header("authorization", header)
                .GET()
                .build();

        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return handleResponse(httpResponse);
    }

    public HttpResponse<String> post(String header, String urlPath, String message) throws URISyntaxException, IOException, InterruptedException, UnauthorizedException, AlreadyTakenException {
        String urlString = String.format("http://%s:%d%s", HOST, this.server.getPort(), urlPath);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(TIMEOUT_MILLIS))
                .header("authorization", header)
                .POST(HttpRequest.BodyPublishers.ofString(message))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return handleResponse(httpResponse);
    }

    public HttpResponse<String> put(String header, String urlPath, String message) throws URISyntaxException, IOException, InterruptedException, UnauthorizedException, AlreadyTakenException {
        String urlString = String.format("http://%s:%d%s", HOST, this.server.getPort(), urlPath);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(TIMEOUT_MILLIS))
                .header("authorization", header)
                .PUT(HttpRequest.BodyPublishers.ofString(message))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return handleResponse(httpResponse);
    }

    public String delete(String header, String urlPath){
        return "";
    }

    /**
     * javalin.post("/user", new RegisterHandler(userService));
     *
     *         javalin.post("/session", new LoginHandler(userService));
     *
     *         javalin.delete("/session", new LogoutHandler(userService));
     *
     *         javalin.get("/game", new ListGamesHandler(gameService));
     *
     *         javalin.post("/game", new CreateGameHandler(gameService));
     *
     *         javalin.put("/game", new JoinGameHandler(gameService));
     *
     *         javalin.delete("/db", new ClearDataBaseHandler(deleteService));
     */
}
