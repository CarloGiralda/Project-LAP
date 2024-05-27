package MACC.ChatService.model;

import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class MappingService {
    private final Mapping activeSessions;

    public MappingService(){
        activeSessions=new Mapping(new HashMap<>());
    }
    public void updateSession(String username,String sessionId){
        // update older sessionID
        activeSessions.updateActiveSession(username,sessionId);}
    public String getSession(String username){
        // get current user sessionID
        return activeSessions.getActiveSession(username);
    }
}
