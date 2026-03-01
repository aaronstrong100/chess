package results;

public class LoginResult {
    private String username;
    private String authToken;
    public LoginResult(String username, String authToken){
        this.username = username;
        this.authToken = authToken;
    }
    public String getUsername(){
        return this.username;
    }
    public String getAuthToken(){
        return authToken;
    }
}
