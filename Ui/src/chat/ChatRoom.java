package chat;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class ChatRoom {
    private final String chatName;
    private final Set<String> participants;
    private final List<ChatMessage> messages;

    public ChatRoom(String name, Set<String> participants) {
        this.chatName = name;
        this.participants = participants;
        this.messages = new ArrayList<>();
    }

    public String getChatName() { return this.chatName; }

    public List<ChatMessage> getMessages() { return this.messages; }

    public void addParticipant(String username) { this.participants.add(username); }

    public Set<String> getParticipants() { return Collections.unmodifiableSet(this.participants); }

    public boolean checkParticipant(String username) { return this.participants.contains(username); }

    public void addMessage(String username, String message) { this.messages.add(new ChatMessage(username, message)); }

    public void removeChatMessage(String time) { this.messages.removeIf(e -> e.getTime().equals(time)); }

    public void removeParticipant(String username) { this.participants.removeIf(e -> e.equals(username)); }

    public void removeParticipantMessages(String username) { this.messages.removeIf(e -> e.getUsername().equals(username)); }
}
