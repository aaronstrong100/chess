package server;

import Requests.LoginRequest;
import Requests.RegisterRequest;
import Results.LoginResult;
import Results.RegisterResult;
import com.google.gson.*;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import io.javalin.*;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import io.javalin.json.JsonMapper;
import org.jetbrains.annotations.NotNull;
import service.UserService;
import service.GameService;
import service.DeleteService;

import java.lang.reflect.Type;

import java.util.Map;

public class Server {

    private final Javalin javalin;

    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    private UserService userService;
    private GameService gameService;
    private DeleteService deleteService;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        userService = new UserService(userDAO, authDAO);

        javalin.post("/user", new RegisterHandler(userService));

        javalin.post("/session", new LoginHandler(userService));
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    public static class RegisterHandler implements Handler {

        UserService userService;

        public RegisterHandler(UserService userService){
            this.userService = userService;
        }

        @Override
        public void handle(@NotNull Context context){
            Gson gson = new Gson();
            String jsonString = context.body();
            RegisterRequest registerRequest = gson.fromJson(jsonString, RegisterRequest.class);
            try {
                RegisterResult registerResult = userService.register(registerRequest);
                context.json(gson.toJson(registerResult));
            } catch (Exception e){
                context.json("{\"message\": \""+ e.getMessage() + "\"}");
            }
        }
    }


    public static class LoginHandler implements Handler {

        UserService userService;

        public LoginHandler(UserService userService){
            this.userService = userService;
        }

        @Override
        public void handle(@NotNull Context context){
            Gson gson = new Gson();
            String jsonString = context.body();
            LoginRequest loginRequest = gson.fromJson(jsonString, LoginRequest.class);
            try {
                LoginResult loginResult = userService.login(loginRequest);
                context.json(gson.toJson(loginResult));
            } catch (Exception e){
                context.json("{\"message\": \""+ e.getMessage() + "\"}");
            }
        }
    }
}
