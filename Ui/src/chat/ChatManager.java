package chat;

import objects.dto.AnswerDto;
import ExceptionsType.ExpType;
import objects.interfaces.Dto;
import com.web.ContextListener;
import objects.interfaces.CommandAnswer;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public enum ChatManager {   // Singleton of UserManager
    Instance;

    private final Map<String, ChatRoom> chat = new HashMap<>();


    public Set<String> getRooms(String username) {
        /* return a set of names of the active rooms */
        Set<String> rooms = new HashSet<>();
        this.chat.values().forEach(r -> {
            if (r.checkParticipant(username))
                rooms.add(r.getChatName());
        });
        return Collections.unmodifiableSet(rooms);
    }

    // public ChatRoom getChat(String chatName) { }

    public List<ChatMessage> getChatMessages(String chatName, String username, boolean action, int messageAmount) {
        /* return the chat message of the room given, if action true than change all username with the user to session,
        *  otherwise filter the user messages out of the messages. return the subList from messageAmount till the end,
        * if the chatRoom does not exists than return null */
        if (!this.chat.containsKey(chatName) || messageAmount < 0)
            return null;
        List<ChatMessage> chat = this.chat.get(chatName).getMessages();
        if (action)
            return Collections.unmodifiableList(substitute(chat, username).subList(messageAmount, chat.size()));
        List<ChatMessage> filteredChat = filter(chat, username);
        return Collections.unmodifiableList(filteredChat.subList(messageAmount, filteredChat.size()));
    }

    public List<ChatMessage> getChatMessages(String chatName, String username, boolean action) {
        /* return the chat message of the room given, if action true than change all username with the user to session,
         *  otherwise filter the user messages out of the messages, if the chatRoom does not exists than return null */
        if (!this.chat.containsKey(chatName))
            return null;
        List<ChatMessage> chat = this.chat.get(chatName).getMessages();
        if (action)
            return Collections.unmodifiableList(substitute(chat, username));
        return Collections.unmodifiableList(filter(chat, username));
    }

    public List<ChatMessage> getChatMessages(String chatName) {
        /* return the messages from the chatRoom given as is, if the chatRoom does not exists than return null */
        if (!this.chat.containsKey(chatName))
            return null;
        return Collections.unmodifiableList(chat.get(chatName).getMessages());
    }

    public boolean chatCheck(String chatName) { return this.chat.containsKey(chatName); }  /* return true if the room with the name given exists false otherwise */

    public CommandAnswer<Dto, String> addNewChat(String chatName, Set<String> participants) {
        /* add new chat with the given name and the set of participants, return a CommandAnswer if there is an exception */
        if (this.chat.containsKey(chatName))
            return new AnswerDto<>(null, "There is a chat with room with the name " + chatName + " already in the system", ExpType.ChatNameDuplication);
        List<String> names = new ArrayList<>();
        AtomicBoolean found = new AtomicBoolean(true);
        participants.forEach( u -> {
            // if (!ContextListener.um.checkUser(u)) {
            if (!UserManager.Instance.checkUser(u)) {
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
        /* add new message to an existing room, return CommandAnswer if there is an exception */
        if (!this.chat.containsKey(chatName))
            return new AnswerDto<>(null, "There is no chat room with the name " + chatName, ExpType.ChatNotFound);
        // if (!ContextListener.um.checkUser(username))
        if (!UserManager.Instance.checkUser(username))
            return new AnswerDto<>(null, "There is no user with the username " + username, ExpType.UserNotFound);

        this.chat.get(chatName).addMessage(username, message);
        return new AnswerDto<>();
    }

    public void addUserToChat(String chatName, String username) {
        if (this.chat.containsKey(chatName))
            this.chat.get(chatName).addParticipant(username);
    }

    public void removeUserFromAllChats(String username) {
        /* remove a user from all the chats */
        this.chat.values().forEach(room -> room.removeParticipant(username));
    }

    private void removeUserFromChat(String chatName, String username) {
        if (this.chat.containsKey(chatName))
            this.chat.get(chatName).removeParticipant(username);
    }

    private void removeChat(String chatName) { this.chat.remove(chatName); }

    private void removeMessage(String chatName, String time) { this.chat.get(chatName).removeChatMessage(time); }

    private List<ChatMessage> filter(List<ChatMessage> chat, String username) {
        /* filter all chat messages from username and return the list */
        List<ChatMessage> new_chat = new ArrayList<>();
        chat.stream().filter(message -> !message.getUsername().equals(username)).forEach(new_chat::add);
        return new_chat;
    }

    private List<ChatMessage> substitute(List<ChatMessage> chat, String username) {
        /* replace all chat messages from username into a chat messages from session and return the list */
        List<ChatMessage> new_chat = new ArrayList<>();
        chat.forEach(message -> {
            if (message.getUsername().equals(username))
                new_chat.add(new ChatMessage("session", message.getMessage(), message.getTime()));
            else
                new_chat.add(message);
        });
        return new_chat;
    }
}
