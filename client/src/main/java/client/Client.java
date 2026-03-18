package client;

import java.util.ArrayList;
import java.util.Scanner;

import chess.ChessBoard;
import chess.ChessGame;
import dataaccess.AlreadyTakenException;
import dataaccess.UnauthorizedException;
import model.GameData;
import requests.*;
import results.*;
import server.Server;
import serverAccess.ServerFacade;
import ui.ChessGamePrinter;

public class Client {
    private ServerFacade serverFacade;
    private Scanner userInput;
    private int menuLevel;
    private String user;
    private String playerType;
    private ChessGame chessGame;
    private String authToken;
    private int[] consoleGameIndices = new int[0];
    private int gameID;

    private static final String ALPHA_NUMERIC = "^[a-zA-Z0-9]+$";

    public Client(Server server){
        this.serverFacade = new ServerFacade(server);
        this.userInput = new Scanner(System.in);
    }

    public void run(){
        System.out.print(ChessGamePrinter.SET_BOARD_BACKGROUND);
        this.menuLevel = 0;
        while(this.menuLevel>=0){
            if(this.menuLevel==0){
                printPrelogin();
                userInputPrelogin();
            }
            else if(this.menuLevel==1){
                printPostLogin();
                userInputPostLogin();
            }
            else if(this.menuLevel==2){
                printGame();
                userInputGame();
            }
        }
    }

    public void stop(){
        this.serverFacade.stop();
        this.menuLevel = -1;
    }

    private String getInput() throws ExitException{
        String input = userInput.nextLine().trim();
        if(input.equals("exit")){
            throw new ExitException();
        } else {
            return input;
        }
    }

    private void handleException(Exception e){
        if(e instanceof UnauthorizedException){
            System.out.println(e.getMessage());
        } else if(e instanceof AlreadyTakenException){
            System.out.println(e.getMessage());
        } else{
            System.out.println("Invalid input.");
        }
    }

    private void printIncorrectInputMessage(){
        System.out.println("Your input is invalid. Please type \"help\" to view valid options");
    }

    private void printPrelogin(){
        System.out.println("Login menu: Type a command or type \"help\" to proceed:");
    }

    private void userInputPrelogin(){
        try{
            String input = getInput().toLowerCase();
            switch (input) {
                case "help":
                    printHelp();
                    break;
                case "quit":
                    stop();
                    break;
                case "login":
                    loginPrompt();
                    break;
                case "register":
                    registerPrompt();
                    break;
                default:
                    printIncorrectInputMessage();
            }
        }  catch (ExitException e){
            System.out.println("Successfully exited loop. ");
        }
    }

    private void printPostLogin(){
        System.out.println("User menu:  Type a command or \"help\" to proceed:");
    }

    private void userInputPostLogin(){
        try {
            String input = getInput().toLowerCase();
            switch (input) {
                case "help":
                    printHelp();
                    break;
                case "logout":
                    logout();
                    break;
                case "create game":
                    createGame();
                    break;
                case "list games":
                    listGames();
                    break;
                case "play game":
                    playGamePrompt();
                    break;
                case "observe game":
                    observeGamePrompt();
                    break;
                default:
                    printIncorrectInputMessage();
            }
        } catch (ExitException e) {
            System.out.println("Successfully exited loop");
        }
    }

    private void printGame(){
        this.chessGame = new ChessGame();
        if(this.playerType.equals("white") || this.playerType.equals("black")) {
            ChessGamePrinter.printChessBoard(this.chessGame.getBoard(), this.playerType);
        } else{
            ChessGamePrinter.printChessBoard(this.chessGame.getBoard(), "white");
        }
        printHelp();
    }

    private void userInputGame(){
        try {
            String input = getInput().toLowerCase();
            printIncorrectInputMessage();
        }
        catch (ExitException e){
            this.menuLevel = 1;
        }
    }
    private void printHelp(){
        switch (menuLevel){
            case 0:
                System.out.println("help - display your current options");
                System.out.println("quit - exit the program");
                System.out.println("login - login as an already registered user");
                System.out.println("register - register as a new user");
                System.out.println("Exit - exit the current loop");
                break;
            case 1:
                System.out.println("Help - display your current options");
                System.out.println("Logout - logout from the session");
                System.out.println("Create Game - create new game");
                System.out.println("List Games - list all active games");
                System.out.println("Play game - join an active game as black or white");
                System.out.println("Observe game - join an active game as observer");
                System.out.println("Exit - exit any loop which is not a menu");
                break;
            case 2:
                System.out.println("The actual game is not yet implemented! Come back later to play!");
                System.out.println("Type exit to exit the game");
        }
    }

    private void registerPrompt() throws ExitException{
        String username = usernamePrompt();
        this.user = username;
        String password = passwordPrompt();
        String email = emailPrompt();
        //Check for failures in request
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        try {
            RegisterResult registerResult = serverFacade.register(registerRequest);
            this.authToken = registerResult.getAuthToken();
            this.menuLevel = 1;
        } catch (Exception e){
            handleException(e);
        }
    }

    private void loginPrompt() throws ExitException{
        String username = usernamePrompt();
        this.user = username;
        String password = passwordPrompt();
        LoginRequest loginRequest = new LoginRequest(username, password);
        try {
            LoginResult loginResult = serverFacade.login(loginRequest);
            this.authToken = loginResult.getAuthToken();
            this.menuLevel = 1;
        } catch (Exception e){
            handleException(e);
        }
    }

    private String usernamePrompt() throws ExitException{
        System.out.println("Please enter your username: ");
        String username = getInput();
        if(username.matches(ALPHA_NUMERIC)){
            return username;
        }
        else{
            System.out.print("Username may only contain alphanumeric characters. ");
            return usernamePrompt();
        }
    }

    private String passwordPrompt() throws ExitException{
        System.out.println("Please enter your password: ");
        String password = getInput();
        if(password.equals("exit")){
            throw new ExitException();
        }
        return password;
    }

    private String emailPrompt() throws ExitException{
        System.out.println("Please enter your email: ");
        String email = getInput();
        if(email.contains("@") && email.substring(email.indexOf("@")).contains(".")){
            return email;
        } else {
            System.out.println("Must be a valid email. ");
            return emailPrompt();
        }
    }

    private void logout(){
        LogoutRequest logoutRequest = new LogoutRequest(this.authToken);
        try{
            serverFacade.logout(logoutRequest);
            this.menuLevel = 0;
        } catch(Exception e){
            handleException(e);
        }
    }

    public void createGame() throws ExitException {
        String gameName = gameNamePrompt();
        CreateGameRequest createGameRequest = new CreateGameRequest(gameName, this.authToken);
        try{
            CreateGameResult createGameResult = serverFacade.createGame(createGameRequest);
            System.out.println("Successfully created game.");
        } catch (Exception e){
            handleException(e);
        }
    }

    private void listGames(){
        ListGamesResult listGamesResult = refreshGames();
        System.out.println("Current games:");
        for (int i = 0; i < listGamesResult.getGames().size(); i++) {
            printGameData(listGamesResult.getGames().get(i), i);
        }
    }

    private ListGamesResult refreshGames(){
        ListGamesRequest listGamesRequest = new ListGamesRequest(this.authToken);
        ListGamesResult listGamesResult = null;
        try {
            listGamesResult = serverFacade.listGames(listGamesRequest);
            this.consoleGameIndices = new int[listGamesResult.getGames().size()];
            for (int i = 0; i < listGamesResult.getGames().size(); i++) {
                consoleGameIndices[i] = listGamesResult.getGames().get(i).getGameID();
            }
        } catch (Exception e){
            handleException(e);
        }
        return listGamesResult;
    }

    private void printGameData(GameData gameData, int consoleGameIndex){
        System.out.print((consoleGameIndex+1) + ". ");
        String whiteUsername = gameData.getWhiteUsername();
        if(whiteUsername == null){
            whiteUsername = "<available>";
        }
        String blackUsername = gameData.getWhiteUsername();
        if(blackUsername==null){
            blackUsername = "<available>";
        }
        System.out.println(gameData.getGameName() + ": White user: " + whiteUsername + ", Black user: " + blackUsername);
    }

    private String gameNamePrompt() throws ExitException {
        System.out.println("Please enter name of new game:");
        return getInput();
    }

    private void playGamePrompt() throws ExitException {
        refreshGames();
        int gameIDInput = gameIdPrompt();
        String color = colorPrompt();
        this.playerType = color;
        JoinGameRequest joinGameRequest = new JoinGameRequest(this.playerType, this.consoleGameIndices[gameIDInput], this.authToken);
        try {
            JoinGameResult joinGameResult = serverFacade.joinGame(joinGameRequest);
            this.gameID = joinGameResult.getGameID();
            this.menuLevel = 2;
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void observeGamePrompt() throws ExitException {
        refreshGames();
        int gameIDInput = gameIdPrompt();
        this.playerType = "observer";
        JoinGameRequest joinGameRequest = new JoinGameRequest(this.playerType, this.consoleGameIndices[gameIDInput], this.authToken);
        try {
            JoinGameResult joinGameResult = serverFacade.joinGame(joinGameRequest);
            this.gameID = joinGameResult.getGameID();
            this.menuLevel = 2;
        } catch (Exception e){
            handleException(e);
        }
    }

    private int gameIdPrompt() throws ExitException {
        System.out.println("Please enter the game ID: ");
        int inputID;
        try {
            inputID = Integer.parseInt(this.getInput())-1;
        } catch (NumberFormatException e){
            System.out.println("The GameID must be an integer.");
            return gameIdPrompt();
        }
        if(inputID<consoleGameIndices.length){
            return inputID;
        } else {
            System.out.println("The game ID is invalid. Here is a list of the current games: ");
            this.listGames();
            return gameIdPrompt();
        }
    }

    private String colorPrompt() throws ExitException {
        System.out.println("Please enter the color you wish to play as: ");
        String color = this.getInput().toLowerCase();
        //Add logic to check that the color is not taken
        if(color.equals("white") || color.equals("black")){
            return color;
        } else {
            System.out.println("Valid colors are black and white.");
            return colorPrompt();
        }
    }

    private static class ExitException extends Exception{
        public ExitException(){
            super();
        }
    }
}
