package chat;

import java.util.Date;
import java.text.SimpleDateFormat;

public class ChatMessage {
    private final String username, message, time;

    public ChatMessage(String username, String message) {
        this(username, message, new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()));
    }

    public ChatMessage(String username, String message, String time) {
        this.time = time;
        this.message = message;
        this.username = username;
    }

    public String getTime() { return time; }

    public String getMessage() { return message; }

    public String getUsername() { return username; }

    @Override
    public String toString() {
        return String.format("Time: %s\r Username: %s\r Message: %s\n", time, username, message);
    }
}
