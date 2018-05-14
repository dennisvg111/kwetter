package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import daomanagers.UserManager;
import domain.Kweet;
import domain.User;

import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.*;

@ServerEndpoint("/live/{username}")
public class SocketController {

    private static Map<Long, List<Session>> sessions = new HashMap<>();
    @Inject
    private UserManager userManager;

    @OnOpen
    public void open(@PathParam("username") String username, Session session) {
        User user = userManager.FindUser(username);

        if (user != null) {
            long id = user.getId();
            if (sessions.getOrDefault(id, null) != null) {
                sessions.get(id).add(session);
            } else {
                //singletonList is not mutable, needs to be changed to new arraylist which can't be initialized with value
                sessions.put(id, new ArrayList<>(Collections.singletonList(session)));
            }
        }
    }

    @OnClose
    public static void close(Session session) {
        long userId = findUserIdBySession(session);
        sessions.get(userId).remove(session);
    }

    private static long findUserIdBySession(Session session) {
        for (Map.Entry<Long, List<Session>> userSessions : sessions.entrySet()) {
            if (userSessions.getValue().contains(session)) {
                return userSessions.getKey();
            }
        }
        throw new IllegalArgumentException("Session with id" + session.getId() + " not found");
    }

    public static void sendKweetToUsers(Kweet kweet, List<User> users) {
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(kweet);


            for (User user : users) {
                if (sessions.getOrDefault(user.getId(), null) != null) {
                    for (Session session : sessions.get(user.getId())) {
                        if (session.isOpen()) {
                            session.getBasicRemote().sendObject(json);
                        } else {
                            close(session);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
