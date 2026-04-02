package WebSocket;

import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import exceptions.UnauthorizedException;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import server.Server;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private final ConnectionManager connectionManager = new ConnectionManager();
    private AuthDAO authDAO;

    public WebSocketHandler(AuthDAO authDAO){
        super();
        this.authDAO = authDAO;
    }

    private void handleWebsocketException(Session user, Exception e){
        try{
            connectionManager.sendError(user, new ErrorMessage(e.getMessage()));
        } catch (Exception ex) {

        }
    }

    @Override
    public void handleClose(@NotNull WsCloseContext wsCloseContext) throws Exception {
        System.out.println("websocket closed");
    }

    @Override
    public void handleConnect(@NotNull WsConnectContext wsConnectContext) throws Exception {
        System.out.println("websocket connected");
        wsConnectContext.enableAutomaticPings();
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext wsMessageContext) throws Exception {
        UserGameCommand command = new Gson().fromJson(wsMessageContext.message(), UserGameCommand.class);
        switch(command.getCommandType()){
            case UserGameCommand.CommandType.MAKE_MOVE:
                command = new Gson().fromJson(wsMessageContext.message(), MakeMoveCommand.class);
                makeMove(command.getGameID(), wsMessageContext.session, getUsername(command.getAuthToken()));
                break;
            case UserGameCommand.CommandType.CONNECT:
                enterGame(command.getGameID(), wsMessageContext.session, getUsername(command.getAuthToken()));
                break;
            case UserGameCommand.CommandType.LEAVE:
                leaveGame(command.getGameID(), wsMessageContext.session, getUsername(command.getAuthToken()));
                break;
            case UserGameCommand.CommandType.RESIGN:
                resign(command.getGameID(), wsMessageContext.session, getUsername(command.getAuthToken()));
                break;
        }
    }

    private String getUsername(String authToken) throws UnauthorizedException {
        return authDAO.getAuthData(authToken).getUsername();
    }

    private void enterGame(int gameID, Session session, String username){
        connectionManager.add(gameID, session);
        String message = String.format("%s entered the game", username);
        try {
            connectionManager.broadcast(gameID, session, new NotificationMessage(message));
        } catch (Exception e) {
            handleWebsocketException(session, e);
        }
    }

    private void leaveGame(int gameID, Session session, String username){
        connectionManager.add(gameID, session);
        String message = String.format("%s left the game", username);
        try {
            connectionManager.broadcast(gameID, session, new NotificationMessage(message));
        } catch (Exception e) {
            handleWebsocketException(session, e);
        }
    }

    private void resign(int gameID, Session session, String username){
        connectionManager.add(gameID, session);
        String message = String.format("%s resigned from the game", username);
        try {
            connectionManager.broadcast(gameID, session, new NotificationMessage(message));
        } catch (Exception e) {
            handleWebsocketException(session, e);
        }
    }

    private void makeMove(int gameID, Session session, String username){
        connectionManager.add(gameID, session);
        String message = String.format("%s made a move", username);
        try {
            connectionManager.broadcast(gameID, session, new NotificationMessage(message));
        } catch (Exception e) {
            handleWebsocketException(session, e);
        }
    }
}
