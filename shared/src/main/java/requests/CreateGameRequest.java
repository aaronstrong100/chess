package requests;

import com.google.gson.annotations.Expose;

public class CreateGameRequest {
    private String gameName;
    @Expose(serialize = false)
    private String authToken;
    public CreateGameRequest(String gameName, String authToken){
        this.gameName = gameName;
        this.authToken = authToken;
    }
    public String getGameName(){
        return this.gameName;
    }
    public String getAuthToken(){
        return this.authToken;
    }
}
