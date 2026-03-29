package WebSocket;

import org.eclipse.jetty.websocket.api.Session;
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

    public void broadcast(Session messageSender, NotificationMessage notification) throws IOException {
        int broadcastingGame = -1;
        String msg = notification.toString();
        for(int gameID : connections.keySet()){
            for(Session user : connections.get(gameID)){
                if(user==messageSender){
                    broadcastingGame = gameID;
                }
            }
        }
        if(broadcastingGame==-1){
            System.out.println("User is not in a game");
        }
        else {
            for (Session user : connections.get(broadcastingGame)) {
                if (user != messageSender) {
                    user.getRemote().sendString(msg);
                }
            }
        }
    }
}
