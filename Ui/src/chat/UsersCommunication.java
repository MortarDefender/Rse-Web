package chat;


import objects.interfaces.Dto;
import objects.interfaces.CommandAnswer;

import java.util.Set;
import java.util.Map;
import java.util.List;


public class UsersCommunication {

    public static void logoutAction(String username) {
        UserManager.Instance.removeUser(username);
        ChatManager.Instance.removeUserFromAllChats(username);
    }

    public static void addUser(String username, boolean userType) {
        UserManager.Instance.addUser(username, userType ? "Stock Broker" : "Admin");
        ChatManager.Instance.addUserToChat("Everyone", username);
    }

    public static Map<String, String> getUsers() { return UserManager.Instance.getUsers(); }

    public static boolean checkUser(String username) { return UserManager.Instance.checkUser(username); }

    public static boolean chatCheck(String chatName) { return ChatManager.Instance.chatCheck(chatName); }

    public static Set<String> getRooms(String username) { return ChatManager.Instance.getRooms(username); }

    public static CommandAnswer<Dto, String> addNewChat(String chatName, Set<String> participants) { return ChatManager.Instance.addNewChat(chatName, participants); }

    public static List<ChatMessage> getChatMessages(String chatName, String username, boolean action) { return ChatManager.Instance.getChatMessages(chatName, username, action); }

    public static CommandAnswer<Dto, String> addToExistingChat(String chatName, String username, String message) { return ChatManager.Instance.addToExistingChat(chatName, username, message); }
}
