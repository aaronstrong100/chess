package serveraccess;

import chess.ChessGame;
import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;

public class WebsocketCommunicator extends Endpoint {

    private static final int TIMEOUT_MILLIS = 5000;
    private static final String HOST = "localhost";

    Session session;
    ServerMessageObserver serverMessageObserver;

    public WebsocketCommunicator(int port, ServerMessageObserver serverMessageObserver){
        try{
            String urlString = String.format("ws://%s:%d", HOST, port);
            URI socketURI = new URI(urlString + "/ws");
            this.serverMessageObserver = serverMessageObserver;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = deserializeMessage(message);
                    serverMessageObserver.sendMessage(serverMessage);
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private ServerMessage deserializeMessage(String message){
        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
        switch(serverMessage.getServerMessageType()) {
            case ServerMessage.ServerMessageType.LOAD_GAME:
                return new Gson().fromJson(message, LoadGameMessage.class);
            case ServerMessage.ServerMessageType.ERROR:
                return new Gson().fromJson(message, ErrorMessage.class);
            case ServerMessage.ServerMessageType.NOTIFICATION:
                return new Gson().fromJson(message, NotificationMessage.class);
        }
        return null;
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig){

    }

    public void enterGame(String authToken, int gameID, String playerType){
        try{
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID, playerType);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void leaveGame(String authToken, int gameID, String playerType){
        try{
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID, playerType);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void resign(){

    }

    public void makeMove(){

    }
}
