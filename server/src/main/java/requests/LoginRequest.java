package requests;

public class LoginRequest {
    private String username;
    private String password;
    public LoginRequest(String username, String password, String email){
        this.username = username;
        this.password = password;
    }
    public String getUsername(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }
}