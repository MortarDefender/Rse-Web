package Utilities;

import chat.ChatMessage;
import com.google.gson.Gson;
import com.web.Authentication;
import com.web.ContextListener;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(name = "ChatUtil", urlPatterns = "/chatUtil")
public class ChatUtil extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson  = new Gson();
        String json = "";
        Map<String, String> msg = new HashMap<>();

        if (ContextListener.DEBUG)
            System.out.println("in chat util");

        String action = request.getParameter("action");     // info || new chat || new message
        String chatName = request.getParameter("chatName");
        String username = request.getParameter("username");
        if (username.equals("session")) {
            username = Authentication.getUsernameSession(request);
            if (username == null)
                response.sendRedirect("/");
        }
        String message = request.getParameter("message");
        String users1 = request.getParameter("users");
        String[] participants = null;
        if (users1 != null)
            participants = users1.split(",");

        /*System.out.printf("username: %s\r action: %s\r chat name: %s\r message: %s\n", username, action, chatName, message);
        System.out.println("action: " + action);
        System.out.println("chat name: " + chatName);
        System.out.println("users 1 " + users1);
        System.out.println("users is null ? " + (participants == null));
        System.out.println("users: " + Arrays.toString(participants));*/

        if (participants != null && participants.length != 0 && !participants[0].equals("")) {
            for (int i = 0; i < participants.length; i++)
                    participants[i] = participants[i].substring(1, participants[i].length() - 1);
        }

        switch (action) {
            case "info":  // get chat messages
                List<ChatMessage> chat;
                if (message.equals("first"))
                    chat = ContextListener.cm.getChatMessages(chatName, username, true);
                else
                    chat = ContextListener.cm.getChatMessages(chatName, username, false);
                json = gson.toJson(chat);
                if (ContextListener.DEBUG)
                    System.out.println(chatName + ": " + json);
                break;
            case "rooms_info":  // get the rooms names
                if (ContextListener.DEBUG)
                    System.out.println("username: " + username);
                Set<String> rooms = ContextListener.cm.getRooms(username);
                json = gson.toJson(rooms);
                break;
            case "new_chat":  // create new chat
                if (participants != null && !ContextListener.cm.chatCheck(chatName) && !participants[0].equals("")) {
                    Set<String> users = new HashSet<>(Arrays.asList(participants));
                    users.add(username);
                    ContextListener.cm.addNewChat(chatName, users).doAction(item -> msg.put("message", chatName + " chat has been created"), exp -> msg.put("error", exp));
                    json = gson.toJson(msg);
                }
                else if (participants == null || participants[0].equals(""))
                    msg.put("error", "There cant be a room without users");
                else
                    msg.put("error", "There can be only one chat room with the name " + chatName);
                if (ContextListener.DEBUG)
                    System.out.println(json);
                break;
            case "new_message":  // add message to a chat
                ContextListener.cm.addToExistingChat(chatName, username, message).ifFailure(exp -> msg.put("error", exp));
                break;
            default:
                msg.put("error", "There is no option " + action);
                break;
        }

        if (msg.containsKey("error"))
            json = gson.toJson(msg);

        if (ContextListener.DEBUG)
            System.out.println(json);
        try (PrintWriter out = response.getWriter()) {
            out.println(json);
            out.flush();
        }
    }
}
