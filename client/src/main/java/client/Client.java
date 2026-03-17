package client;

import java.util.Scanner;

import server.Server;
import serverAccess.ServerFacade;

public class Client {
    private ServerFacade serverFacade;
    private Scanner userInput;
    private int menuLevel;
    private boolean running;

    public Client(Server server){
        this.serverFacade = new ServerFacade(server);
        this.userInput = new Scanner(System.in);
    }

    public void run(){
        this.running = true;
        while(this.running){
            if(menuLevel==0){
                printPrelogin();
                userInputPrelogin();
            }
        }
    }

    public void stop(){
        this.serverFacade.stop();
        this.running = false;
    }

    public void printPrelogin(){
        System.out.println("Welcome to Aaron Strong's Chess Server. Type an option to proceed:");
        printHelp();
    }

    public void userInputPrelogin(){
        String input = userInput.nextLine().trim().toLowerCase();
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

    public void loginPrompt(){

    }

    public void registerPrompt(){

    }
}
