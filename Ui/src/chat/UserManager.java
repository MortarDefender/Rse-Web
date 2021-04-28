package chat;

import java.util.*;

public enum UserManager {  // Singleton of UserManager
    Instance;

    private final Map<String, String> activeUsers = new HashMap<>();;


    public boolean checkUser(String username) { return activeUsers.containsKey(username); }

    public synchronized String getUserType(String username) { return this.activeUsers.get(username); }

    public synchronized Map<String, String> getUsers() { return Collections.unmodifiableMap(activeUsers); }

    public synchronized void removeUser(String username) { activeUsers.remove(username); }

    public synchronized void addUser(String username, String kind) { activeUsers.put(username, kind); }
}