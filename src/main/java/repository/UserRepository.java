package repository;

import enums.RoleUser;
import model.User;

import java.util.List;

public abstract class UserRepository implements BaseRepository<User, String, List<User>> {
    protected abstract List<User> getUsers(RoleUser roleUser);

    public abstract String toggleActivation(String phoneNumber);
    public abstract User login(String chatId);
    public abstract void editByChatId(String userChatId, User editedUser);
}
