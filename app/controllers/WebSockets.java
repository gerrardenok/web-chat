package controllers;

import actions.GlobalContextParams;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import play.*;
import play.libs.Json;
import play.mvc.*;
import play.libs.F.*;

import org.codehaus.jackson.*;
import views.html.*;

import models.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Реализует API для работы с данными по протоколу Web-socket.</p>
 * @version 2.0
 */
public class WebSockets extends Application {
    // Members of this room.
    HashMap<User,WebSocket.Out<JsonNode>> members = new HashMap<User, WebSocket.Out<JsonNode>>();

    public static WebSocket<JsonNode> connectToRoom(Long id) {
        User loggedUser = GlobalContextParams.loggedUser();
        if(loggedUser == null) {
            //forbidden();
        }
        Room room = Room.findById(id);
        if(room == null) {
            //return badRequest();
        }

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);

        return new WebSocket<JsonNode>() {
            // Called when the Websocket Handshake is done.
            public void onReady(WebSocket.In<JsonNode> in, WebSocket.Out<JsonNode> out){
                try {
                    System.out.println("Socket is READY");
                    out.write(objectMapper.valueToTree("TEST MESSSAGE"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                // For each event received on the socket,
                in.onMessage(new Callback<JsonNode>() {
                    public void invoke(JsonNode event) {
                        // Send a Talk message to the room.
                        System.out.println("INCOMING MESSAGE");
                        System.out.println(event.get("text").asText());

                        User loggedUser = GlobalContextParams.loggedUser();
                        if(loggedUser != null) System.out.println("NOTIFY ALL! sender:"+loggedUser.toString());
                    }
                });

                // When the socket is closed.
                in.onClose(new Callback0() {
                    public void invoke() {
                        User loggedUser = GlobalContextParams.loggedUser();
                        if(loggedUser == null) {
                            System.out.println("USER IS OUT");
                        } else {
                            System.out.println("USER IS AWAY");
                        }
                    }
                });
            }
        };
    }

    // Send a Json event to all members
    public void notifyAll(String kind, User user, String text) {
        for(WebSocket.Out<JsonNode> channel: members.values()) {

            ObjectNode event = Json.newObject();
            event.put("kind", kind);
            event.put("user", user.toString());
            event.put("message", text);

            ArrayNode m = event.putArray("members");
            for(User u: members.keySet()) {
                m.add(u.toString());
            }

            channel.write(event);
        }
    }
}
