package Utilities;

import java.util.*;

public class UserManager {  // Singleton of UserManager
    private static UserManager manager = null;
    private final Map<String, String> activeUsers;

    private UserManager() { activeUsers = new HashMap<>(); }

    public static UserManager getManager() {
        if (manager == null)
            manager = new UserManager();
        return manager;
    }

    public boolean checkUser(String username) { return activeUsers.containsKey(username); }

    public synchronized String getUserType(String username) { return this.activeUsers.get(username); }

    public synchronized Map<String, String> getUsers() { return Collections.unmodifiableMap(activeUsers);}

    public synchronized void removeUser(String username) { activeUsers.remove(username); }

    public synchronized void addUser(String username, String kind) { activeUsers.put(username, kind); }
}
