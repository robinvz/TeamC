package be.kdg.trips.model.chat;

import be.kdg.trips.model.user.User;
import javax.validation.constraints.NotNull;
import javax.persistence.*;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public class ChatServer {
    @NotNull
    private User user1;
    @NotNull
    private User user2;

    public ChatServer(User user1, User user2)
    {
        this.user1 = user1;
        this.user2 = user2;
    }

    public ChatServer() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatServer that = (ChatServer) o;

        if (!user1.equals(that.user1)) return false;
        if (!user2.equals(that.user2)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = user1.hashCode();
        result = 31 * result + user2.hashCode();
        return result;
    }
}
