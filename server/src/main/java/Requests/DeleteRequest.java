package Requests;

public class DeleteRequest {
    private String authToken;
    public DeleteRequest(String authToken){
        this.authToken = authToken;
    }
    public String getAuthToken(){
        return this.authToken;
    }
}
