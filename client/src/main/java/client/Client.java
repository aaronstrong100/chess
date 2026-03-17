package client;

import java.util.Scanner;

import requests.*;
import server.Server;
import serverAccess.ServerFacade;

public class Client {
    private ServerFacade serverFacade;
    private Scanner userInput;
    private int menuLevel;
    private String user;

    private static final String ALPHA_NUMERIC = "^[a-zA-Z0-9]+$";

    public Client(Server server){
        this.serverFacade = new ServerFacade(server);
        this.userInput = new Scanner(System.in);
    }

    public void run(){
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
            case "play game":
                playGamePrompt();
            case "observe game":
                observeGamePrompt();
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
        }
    }

    public void registerPrompt(){
        String username = usernamePrompt();
        this.user = username;
        String password = passwordPrompt();
        String email = emailPrompt();
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        this.menuLevel = 1;
    }

    public void loginPrompt(){
        String username = usernamePrompt();
        this.user = username;
        String password = passwordPrompt();
        LoginRequest loginRequest = new LoginRequest(username, password);
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
        System.out.println("Please enter the ID of the game you wish to join: ");
        int gameID = Integer.parseInt(this.getInput());
    }

    public void observeGamePrompt(){
        System.out.println("Please enter the ID of the game you wish to observe: ");
        int gameID = Integer.parseInt(this.getInput());
    }
}
