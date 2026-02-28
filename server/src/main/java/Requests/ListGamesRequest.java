package Requests;

public class ListGamesRequest {
    private String authToken;
    public ListGamesRequest(String authToken){
        this.authToken = authToken;
    }
    public String getAuthToken(){
        return this.authToken;
    }
}
