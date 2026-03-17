package client;

import java.util.Scanner;

import chess.ChessBoard;
import chess.ChessGame;
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
                while(this.menuLevel==0){
                    userInputPrelogin();
                }
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

    public String getInput(){
        return userInput.nextLine().trim();
    }

    public void printPrelogin(){
        System.out.println("Welcome to Aaron Strong's Chess Server. Type an option to proceed:");
        printHelp();
    }

    public void userInputPrelogin(){
        String input = getInput().toLowerCase();
        switch (input){
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
        }
    }

    public void printPostLogin(){
        System.out.println("Welcome, " + user + ". Type an option to proceed:");
        printHelp();
    }

    public void userInputPostLogin(){
        String input = getInput().toLowerCase();
        switch (input){
            case "help":
                printHelp();
                break;
            case "logout":
                logout();
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
        }
    }

    public void printGame(){
        ChessGamePrinter.printChessBoard(this.chessGame.getBoard(), this.playerType);
        printHelp();
    }

    public void userInputGame(){
        String input = getInput().toLowerCase();
        if(input.equals("exit")){
            this.menuLevel = 1;
        }
    }
    public void printHelp(){
        switch (menuLevel){
            case 0:
                System.out.println("help - display your current options");
                System.out.println("quit - exit the program");
                System.out.println("login - login as an already registered user");
                System.out.println("register - register as a new user\n");
                break;
            case 1:
                System.out.println("Help - display your current options");
                System.out.println("Logout - logout from the session");
                System.out.println("Create Game - create new game");
                System.out.println("List Games - list all active games");
                System.out.println("Play game - join an active game as black or white");
                System.out.println("Observe game - join an active game as observer");
                break;
            case 2:
                System.out.println("The actual game is not yet implemented! Come back later to play!");
                System.out.println("Type exit to exit the game");
        }
    }

    public void registerPrompt(){
        String username = usernamePrompt();
        this.user = username;
        String password = passwordPrompt();
        String email = emailPrompt();
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        //Add logic to send the request
        this.menuLevel = 1;
    }

    public void loginPrompt(){
        String username = usernamePrompt();
        this.user = username;
        String password = passwordPrompt();
        LoginRequest loginRequest = new LoginRequest(username, password);
        //Add logic to send the request
        this.menuLevel = 1;
    }

    public String usernamePrompt(){
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

    public String passwordPrompt(){
        System.out.println("Please enter your password: ");
        String password = getInput();
        return password;
    }

    public String emailPrompt(){
        System.out.println("Please enter your email: ");
        String email = getInput();
        if(email.contains("@") && email.substring(email.indexOf("@")).contains(".")){
            return email;
        } else {
            System.out.println("Must be a valid email. ");
            return emailPrompt();
        }
    }

    public void logout(){
        this.menuLevel = 0;
    }

    public void listGames(){
        System.out.println("Games: ");
    }

    public void playGamePrompt(){
        int gameID = gameIdPrompt();
        String color = colorPrompt();
        this.playerType = color;
        //Add logic to get the chessgame
        chessGame = new ChessGame();
        this.menuLevel = 2;
    }

    public void observeGamePrompt(){
        int gameID = gameIdPrompt();
        this.playerType = "observer";
        chessGame = new ChessGame();
        this.menuLevel = 2;
    }

    public int gameIdPrompt(){
        System.out.println("Please enter the ID of the game you wish to observe: ");
        //Add logic to check gameID valid
        return Integer.parseInt(this.getInput());
    }

    public String colorPrompt(){
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
}
