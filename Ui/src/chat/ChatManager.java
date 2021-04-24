package chat;

import objects.Dto;
import objects.AnswerDto;
import objects.CommandAnswer;
import ExceptionsType.ExpType;
import com.web.ContextListener;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChatManager {   // Singleton of UserManager
    private final Map<String, ChatRoom> chat;
    private static ChatManager manager = null;

    private ChatManager() { chat = new HashMap<>(); }

    public static ChatManager getManager() {
        if (manager == null)
            manager = new ChatManager();
        return manager;
    }

    public Set<String> getRooms(String username) {
        Set<String> rooms = new HashSet<>();
        this.chat.values().forEach(r -> {
            if (r.checkParticipant(username))
                rooms.add(r.getChatName());
        });
        return Collections.unmodifiableSet(rooms);
    }

    // public ChatRoom getChat(String chatName) { }

    public List<ChatMessage> getChatMessages(String chatName, String username, boolean action) {
        if (!this.chat.containsKey(chatName))
            return null;
        List<ChatMessage> chat = this.chat.get(chatName).getMessages();
        if (action)
            return Collections.unmodifiableList(substitute(chat, username));
        return Collections.unmodifiableList(filter(chat, username));
    }

    public List<ChatMessage> getChatMessages(String chatName) {
        if (!this.chat.containsKey(chatName))
            return null;
        return Collections.unmodifiableList(chat.get(chatName).getMessages());
    }

    public boolean chatCheck(String chatName) { return this.chat.containsKey(chatName); }

    public CommandAnswer<Dto, String> addNewChat(String chatName, Set<String> participants) {
        if (this.chat.containsKey(chatName))
            return new AnswerDto<>(null, "There is a chat with room with the name " + chatName + " already in the system", ExpType.ChatNameDuplication);
        List<String> names = new ArrayList<>();
        AtomicBoolean found = new AtomicBoolean(true);
        participants.forEach( u -> {
            if (!ContextListener.um.checkUser(u)) {
                found.set(false);
                names.add(u);
            }
        });
        if (!found.get())
            return new AnswerDto<>(null, "These users are not in the system: " + names, ExpType.UserNotFound);
        this.chat.put(chatName, new ChatRoom(chatName, participants));
        return new AnswerDto<>();
    }

    public CommandAnswer<Dto, String> addToExistingChat(String chatName, String username, String message) {
        if (!this.chat.containsKey(chatName))
            return new AnswerDto<>(null, "There is no chat room with the name " + chatName, ExpType.ChatNotFound);
        if (!ContextListener.um.checkUser(username))
            return new AnswerDto<>(null, "There is no user with the username " + username, ExpType.UserNotFound);

        this.chat.get(chatName).addMessage(username, message);
        return new AnswerDto<>();
    }

    public void addUserToChat(String chatName, String username) {
        if (this.chat.containsKey(chatName))
            this.chat.get(chatName).addParticipant(username);
    }

    public void removeUserFromAllChats(String username) {
        this.chat.values().forEach(room -> room.removeParticipant(username));
    }

    private void removeUserFromChat(String chatName, String username) {
        if (this.chat.containsKey(chatName))
            this.chat.get(chatName).removeParticipant(username);
    }

    private void removeChat(String chatName) { this.chat.remove(chatName); }

    private void removeMessage(String chatName, String time) { this.chat.get(chatName).removeChatMessage(time); }

    private List<ChatMessage> filter(List<ChatMessage> chat, String username) {
        List<ChatMessage> new_chat = new ArrayList<>();
        chat.stream().filter(message -> !message.getUsername().equals(username)).forEach(new_chat::add);
        return new_chat;
    }

    private List<ChatMessage> substitute(List<ChatMessage> chat, String username) {
        List<ChatMessage> new_chat = new ArrayList<>();
        for(ChatMessage message: chat) {
            if (message.getUsername().equals(username))
                new_chat.add(new ChatMessage("session", message.getMessage(), message.getTime()));
            else
                new_chat.add(message);
        }
        return new_chat;
    }
}
