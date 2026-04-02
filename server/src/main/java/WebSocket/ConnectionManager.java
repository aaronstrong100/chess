package WebSocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class ConnectionManager {
    public final HashMap<Integer, Collection<Session>> connections = new HashMap<>();

    public void add(int gameID, Session session) {
        if(!connections.containsKey(gameID)){
            connections.put(gameID, new HashSet<Session>());
        }
        connections.get(gameID).add(session);
    }

    public void remove(int gameID, Session session){
        connections.get(gameID).remove(session);
    }

    public void broadcast(int broadcastingGame, Session messageSender, NotificationMessage notification) throws IOException {
        for (Session user : connections.get(broadcastingGame)) {
            if (user != messageSender) {
                user.getRemote().sendString(new Gson().toJson(notification));
            }
        }
    }

    public void sendError(Session user, ErrorMessage error) throws IOException{
        String msg = error.toString();
        user.getRemote().sendString(new Gson().toJson(error));
    }
}
