package serverAccess;
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

    public String get(String header, String urlPath){
        return "";
    }

    public String post(String header, String urlPath, String message) throws URISyntaxException, IOException, InterruptedException, UnauthorizedException, AlreadyTakenException{
        String urlString = String.format("http://%s:%d%s", HOST, this.server.getPort(), urlPath);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(TIMEOUT_MILLIS))
                .header("authorization", header)
                .POST(HttpRequest.BodyPublishers.ofString(message))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        switch (httpResponse.statusCode()){
            case 200:
                return httpResponse.body();
            case 401:
                throw new UnauthorizedException();
            case 403:
                throw new AlreadyTakenException();
            default:
                throw new RuntimeException();
        }

        /**
         * if(e instanceof UnauthorizedException){
         *             return 401;
         *         } else if(e instanceof AlreadyTakenException){
         *             return 403;
         *         } else if(e instanceof RuntimeException){
         *             return 500;
         *         }
         */
    }

    public String put(String header, String urlPath, String message){
        return "";
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
