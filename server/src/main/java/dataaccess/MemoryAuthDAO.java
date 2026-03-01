package dataaccess;

import model.AuthData;
import dataaccess.UnauthorizedException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    private ArrayList<AuthData> authData;
    public MemoryAuthDAO(){
        this.authData = new ArrayList<>();
    }

    @Override
    public AuthData addAuthData(AuthData authData){
        this.authData.add(authData);
        return authData;
    }

    @Override
    public AuthData getAuthData(String authToken) throws DataAccessException {
        for(AuthData authData : this.authData){
            if(authData.getAuthToken().equals(authToken)){
                return authData;
            }
        }
        throw new UnauthorizedException("The authorization token is invalid.");
    }

    public void deleteAuthData(String authToken){
        Iterator<AuthData> authDataIterator = this.authData.iterator();
        while(authDataIterator.hasNext()){
            AuthData authData = authDataIterator.next();
            if(authData.getAuthToken().equals(authToken)){
                authDataIterator.remove();
            }
        }
    }

    public String generateNewAuthToken(){
        return UUID.randomUUID().toString();
    }

    @Override
    public void clearDataBase(){
        this.authData = new ArrayList<>();
    }
}
