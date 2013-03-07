package be.kdg.trips.model.chat;

import be.kdg.trips.model.user.User;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public class ChatServer {
    private User user1;
    private User user2;

    public ChatServer(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
    }
}
