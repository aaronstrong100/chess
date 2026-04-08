package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import exceptions.DataAccessException;
import exceptions.UnauthorizedException;
import io.javalin.websocket.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private final ConnectionManager connectionManager = new ConnectionManager();
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    private static final String[] COLUMN_NAMES = {"a", "b", "c", "d", "e", "f", "g", "h"};

    public WebSocketHandler(AuthDAO authDAO, GameDAO gameDAO){
        super();
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ConnectionManager getConnectionManager(){
        return this.connectionManager;
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
    public void handleMessage(@NotNull WsMessageContext wsMessageContext){
        UserGameCommand command = new Gson().fromJson(wsMessageContext.message(), UserGameCommand.class);
        Session session = wsMessageContext.session;
        if(checkAuthToken(session, command.getAuthToken())) {
            switch (command.getCommandType()) {
                case UserGameCommand.CommandType.MAKE_MOVE:
                    MakeMoveCommand moveCommand = new Gson().fromJson(wsMessageContext.message(), MakeMoveCommand.class);
                    makeMove(moveCommand.getGameID(), session, getUsername(session, moveCommand.getAuthToken()), getUserType(moveCommand.getGameID(),
                            session, moveCommand.getAuthToken()), moveCommand.getMove());
                    break;
                case UserGameCommand.CommandType.CONNECT:
                    enterGame(command.getGameID(), session, getUsername(session, command.getAuthToken()), getUserType(command.getGameID(), session,
                            command.getAuthToken()));
                    break;
                case UserGameCommand.CommandType.LEAVE:
                    leaveGame(command.getGameID(), session, getUsername(session, command.getAuthToken()), getUserType(command.getGameID(), session,
                            command.getAuthToken()));
                    break;
                case UserGameCommand.CommandType.RESIGN:
                    resign(command.getGameID(), session, getUsername(session, command.getAuthToken()), getUserType(command.getGameID(), session,
                            command.getAuthToken()));
                    break;
            }
        }
    }

    private boolean checkAuthToken(Session session, String authToken){
        try{
            authDAO.getAuthData(authToken);
            return true;
        } catch (Exception e){
            connectionManager.sendError(session, new ErrorMessage("Please enter a valid authToken"));
            return false;
        }
    }

    private String getUsername(Session session, String authToken){
        try {
            return authDAO.getAuthData(authToken).getUsername();
        } catch (UnauthorizedException e){
            connectionManager.sendError(session, new ErrorMessage("Please enter a valid authToken"));
        }
        return null;
    }

    private String getUserType(int gameID, Session session, String authToken){
        try {
            GameData gameData = gameDAO.getGame(gameID);
            String username = getUsername(session, authToken);
            if(username.equals(gameData.getWhiteUsername())){
                return "white";
            } else if(username.equals(gameData.getBlackUsername())){
                return "black";
            } else {
                return "observer";
            }
        } catch (DataAccessException e) {
            handleWebsocketException(session, e);
        }
        return "observer";
    }

    private void enterGame(int gameID, Session session, String username, String userType){
        try {
            if (!gameOver(gameID)) {
                GameData gameData = gameDAO.getGame(gameID);
                connectionManager.add(gameID, session);
                String message = String.format("%s entered the game as %s", username, userType);
                ChessGame game = gameData.getGame();
                connectionManager.loadGameBroadcast(session, new LoadGameMessage(game));
                connectionManager.broadcast(gameID, session, new NotificationMessage(message));
            }
            else {
            }
        } catch (Exception e) {
        }
    }

    private void leaveGame(int gameID, Session session, String username, String userType){
        String message = String.format("%s (%s) left the game", username, userType);
        try {
            if(!gameOver(gameID)){
                removePlayerFromGame(username, gameID);
            }
            connectionManager.broadcast(gameID, session, new NotificationMessage(message));
            connectionManager.remove(gameID, session);
        } catch (Exception e) {
            handleWebsocketException(session, e);
        }
    }

    public void removePlayerFromGame(String username, int gameID) throws DataAccessException {
        GameData gameData = gameDAO.getGame(gameID);
        if(username.equals(gameData.getWhiteUsername())){
            GameData updatedGameData = gameData.updateWhiteUsername(null);
            gameDAO.overwriteGame(gameID, updatedGameData);
        } else if(username.equals(gameData.getBlackUsername())){
            GameData updatedGameData = gameData.updateBlackUsername(null);
            gameDAO.overwriteGame(gameID, updatedGameData);
        }
    }

    private void setGameOver(int gameID) throws DataAccessException {
        GameData gameData = gameDAO.getGame(gameID);
        GameData updatedGameData = gameData.updateGameOver(true);
        gameDAO.overwriteGame(gameID, updatedGameData);
    }

    private boolean gameOver(int gameID) throws DataAccessException {
        GameData gameData = gameDAO.getGame(gameID);
        return gameData.gameOver();
    }

    private void resign(int gameID, Session session, String username, String userType){
        if(!userType.equals("observer")) {
            try {
                GameData gameData = gameDAO.getGame(gameID);
                if(!gameData.gameOver()) {
                    String message = String.format("%s (%s) resigned from the game", username, userType);
                    connectionManager.broadcastAll(gameID, new NotificationMessage(message));
                    setGameOver(gameID);
                }
                else{
                    connectionManager.sendError(session, new ErrorMessage("The game is over. You cannot resign."));
                }
            } catch (Exception e) {
                handleWebsocketException(session, e);
            }
        } else {
            connectionManager.sendError(session, new ErrorMessage("An observer cannot resign"));
        }
    }

    private boolean turnMatches(String userType, ChessGame.TeamColor turn){
        if(turn==ChessGame.TeamColor.BLACK && userType.equalsIgnoreCase("black")){
            return true;
        } else if (turn==ChessGame.TeamColor.WHITE && userType.equalsIgnoreCase("white")){
            return true;
        }
        else {
            return false;
        }
    }

    private void makeMove(int gameID, Session session, String username, String userType, ChessMove move){
        try{
            GameData gameData = gameDAO.getGame(gameID);
            ChessGame game = gameData.getGame();
            if(turnMatches(userType, game.getTeamTurn())) {
                if(!gameData.gameOver()) {
                    game.makeMove(move);
                    gameDAO.overwriteGame(gameID, gameData.updateChessGame(game));
                    connectionManager.loadGameBroadcast(gameID, new LoadGameMessage(game));
                    String message = String.format("%s (%s) made the move %s", username, userType, moveToString(move));
                    connectionManager.broadcast(gameID, session, new NotificationMessage(message));
                    if (!handleInCheckmate(gameID, game, gameData.getWhiteUsername(), gameData.getBlackUsername())) {
                        handleInCheck(gameID, game, gameData.getWhiteUsername(), gameData.getBlackUsername());
                    } else {
                        setGameOver(gameID);
                    }
                }
                else {
                    connectionManager.sendError(session, new ErrorMessage("The game is over."));
                }
            } else {
                connectionManager.sendError(session, new ErrorMessage("It is not your turn."));
            }
        } catch (Exception e) {
            handleWebsocketException(session, e);
        }
    }

    private String moveToString(ChessMove move){
        /*
        String pos = getInput().toLowerCase();
            int col = ROW_NAMES.get(pos.substring(0,1));
            int row = Integer.parseInt(pos.substring(1));
            ChessPosition piecePosition = new ChessPosition(row, col);
         */
        ChessPosition startPosition = move.getStartPosition();
        String startPositionString = COLUMN_NAMES[startPosition.getColumn()-1] + startPosition.getRow();
        ChessPosition endPosition = move.getEndPosition();
        String endPositionString = COLUMN_NAMES[endPosition.getColumn()-1] + endPosition.getRow();
        return String.format("%s -> %s", startPositionString, endPositionString);
    }

    private void handleInCheck(int gameID, ChessGame game, String whiteUsername, String blackUsername){
        String message = "";
        if(game.isInCheck(ChessGame.TeamColor.BLACK)){
            message = String.format("%s (black) is in check.", blackUsername);
        } else if(game.isInCheck(ChessGame.TeamColor.WHITE)){
            message = String.format("%s (white) is in check.", whiteUsername);
        }
        else{
            return;
        }
        try {
            connectionManager.broadcastAll(gameID, new NotificationMessage(message));
        } catch (Exception e){

        }
    }

    private boolean handleInCheckmate(int gameID, ChessGame game, String whiteUsername, String blackUsername){
        String message = "";
        if(game.isInCheckmate(ChessGame.TeamColor.BLACK)){
            message = String.format("%s (black) is in checkmate. %s (white) wins.", blackUsername, whiteUsername);
        } else if(game.isInCheckmate(ChessGame.TeamColor.WHITE)){
            message = String.format("%s (white) is in checkmate. %s (black) wins.", whiteUsername, whiteUsername);
        }
        else{
            return false;
        }
        try {
            connectionManager.broadcastAll(gameID, new NotificationMessage(message));
        } catch (Exception e){

        }
        return true;
    }
}
