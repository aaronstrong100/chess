package server;

import Requests.RegisterRequest;
import Results.RegisterResult;
import com.google.gson.*;
import io.javalin.*;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import io.javalin.json.JsonMapper;
import org.jetbrains.annotations.NotNull;
import service.UserService;

import java.lang.reflect.Type;

import java.util.Map;

public class Server {

    private final Javalin javalin;
    protected UserService userService;
    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        userService = new UserService();
        javalin.post("/user", new RegisterHandler(userService));
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
}
