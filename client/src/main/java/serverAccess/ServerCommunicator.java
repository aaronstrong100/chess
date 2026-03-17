package serverAccess;
import server.Server;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;

public class ServerCommunicator {
    private Server server;

    public ServerCommunicator(Server server){
        this.server = server;
    }

    public void stop(){
        this.server.stop();
    }
}
