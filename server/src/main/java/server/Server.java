package server;

import io.javalin.*;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.javalin.json.JsonMapper;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Type;

import java.util.Map;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.get("/game", new RegisterHandler());
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    public static class RegisterHandler implements Handler {
        /**
         *
         * @param context
         * @throws Exception
         */
        @Override
        public void handle(@NotNull Context context){
            Map<String, String> output = Map.of("title","Registering...");
            Gson gson = new Gson();
            context.json(gson.toJson(output,output.getClass()));
        }
    }
}
