package client;

import java.util.Scanner;

import requests.*;
import
import server.Server;
import serverAccess.ServerFacade;

public class Client {
    private ServerFacade serverFacade;
    private Scanner userInput;
    private int menuLevel;
    private boolean running;

    private static final String ALPHA_NUMERIC = "^[a-zA-Z0-9]+$";

    public Client(Server server){
        this.serverFacade = new ServerFacade(server);
        this.userInput = new Scanner(System.in);
    }

    public void run(){
        this.running = true;
        while(this.running){
            if(this.menuLevel==0){
                printPrelogin();
                userInputPrelogin();
            }
        }
    }

    public void stop(){
        this.serverFacade.stop();
        this.running = false;
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
            case "register":
                registerPrompt();
        }

    }

    public void printHelp(){
        switch (menuLevel){
            case 0:
                System.out.println("help - display your current options");
                System.out.println("quit - exit the program");
                System.out.println("login - login as an already registered user");
                System.out.println("register - register as a new user\n");
        }
    }

    public void registerPrompt(){
        String username = usernamePrompt();
        String password = passwordPrompt();
        String email = emailPrompt();
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
    }

    public void loginPrompt(){
        String username = usernamePrompt();
        String password = passwordPrompt();
        String email = emailPrompt();
        LoginRequest loginRequest = new LoginRequest(username, password);
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
}
