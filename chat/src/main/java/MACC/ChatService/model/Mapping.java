package MACC.ChatService.model;

import java.util.HashMap; // import the HashMap class


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Mapping {

   private HashMap<String, String> activeSessions;

   public void updateActiveSession(String username,String sessionId){
      activeSessions.put(username,sessionId);
   }

   public String getActiveSession(String username){return activeSessions.get(username);}


}
