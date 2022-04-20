package service;

import enums.RoleUser;
import jsonFile.CollectionsTypeFactory;
import jsonFile.FileUrls;
import jsonFile.FileUtils;
import jsonFile.Json;
import lombok.SneakyThrows;
import model.User;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserService extends UserRepository {

    public User get(String chatId) {
        return login(chatId);
    }

    @Override
    public User get(UUID id) {
        return null;
    }

    @Override
    public List<User> getList() {
        return getUserListFromFile();
    }

    @Override
    public List<User> getList(UUID id) {
        return null;
    }

    @Override
    public String add(User user) {
        if (isPhoneNumberExist(user.getPhoneNumber())) {
            return ERROR_USER_ALREADY_EXIST;
        }
        List<User> userList = getUserListFromFile();
        userList.add(user);
        setUserListToFile(userList);
        return SUCCESS;
    }

    @Override
    public String editById(UUID userId, User editedUser) {
        List<User> userList = getUserListFromFile();
        for (User user : userList) {
            if (user.getId().equals(userId)) {
                user.setUsername(editedUser.getUsername());
                user.setBalance(editedUser.getBalance());
                user.setRole(editedUser.getRole());
                user.setPhoneNumber(editedUser.getPhoneNumber());
                user.setActive(editedUser.isActive());
                user.setName(editedUser.getName());
                user.setCreatedBy(editedUser.getCreatedBy());
                user.setUpdatedBy(editedUser.getUpdatedBy());
                user.setCreatedDate(editedUser.getCreatedDate());
                user.setUpdatedDate(editedUser.getUpdatedDate());

                setUserListToFile(userList);
                return SUCCESS;
            }
        }
        return ERROR_USER_NOT_FOUND;
    }


    @Override
    public List<User> getUsers(RoleUser roleUser) {
        List<User> users = new ArrayList<>();
        for (User user : getUserListFromFile()) {
            if (user.getRole().equals(roleUser) && user.isActive()) {
                users.add(user);
            }
        }
        return users;
    }

    public List<User> getAllUsers(RoleUser roleUser) {
        List<User> users = new ArrayList<>();
        for (User user : getUserListFromFile()) {
            if (user.getRole().equals(roleUser)) {
                users.add(user);
            }
        }
        return users;
    }

    @Override
    public String toggleActivation(String phoneNumber) {  // phone boyicha activate qilish
        List<User> userList = getUserListFromFile();
        for (User user : userList) {
            if (user.getPhoneNumber().equals(phoneNumber)) {
                user.setActive(!user.isActive());
                setUserListToFile(userList);
                return SUCCESS;
            }
        }
        return ERROR_USER_NOT_FOUND;
    }

    @Override
    public User login(String chatId) {
        for (User user : getUserListFromFile()) {
            if (user.getChatId().equals(chatId)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void editByChatId(String userChatId, User editedUser) {
        List<User> userList = getUserListFromFile();
        int index = 0;
        for (User user : userList) {
            if (user.getChatId().equals(userChatId)) {
                if (editedUser.getUsername() != null)
                    user.setUsername(editedUser.getUsername());
                if (editedUser.getBalance() != 0)
                    user.setBalance(editedUser.getBalance());
                if (editedUser.getRole() != null)
                    user.setRole(editedUser.getRole());
                if (editedUser.getUserState() != null)
                    user.setUserState(editedUser.getUserState());
                if (editedUser.getPhoneNumber() != null)
                    user.setPhoneNumber(editedUser.getPhoneNumber());
                if (editedUser.getLocation() != null)
                    user.setLocation(editedUser.getLocation());
                if (editedUser.getName() != null)
                    user.setName(editedUser.getName());

                user.setActive(editedUser.isActive());
                user.setUpdatedDate(editedUser.getCreatedDate());
                userList.set(index, user);
                setUserListToFile(userList);
                return;
            }
            index++;
        }

    }

    public boolean isPhoneNumberExist(String phoneNumber) {
        for (User user : getUserListFromFile()) {
            if (user.getPhoneNumber().equals(phoneNumber)) {
                return true;
            }
        }
        return false;
    }

    public List<User> getUserListFromFile() {
        String userJsonStringFromFile = FileUtils.readFromFile(FileUrls.userUrl);
        List<User> userList;
        try {
            userList = Json.objectMapper.readValue(userJsonStringFromFile, CollectionsTypeFactory.listOf(User.class));
        } catch (Exception e) {
            System.out.println(e);
            userList = new ArrayList<>();
        }
        return userList;
    }

    @SneakyThrows
    public void setUserListToFile(List<User> userList) {
        String newUserJsonFromObject = Json.prettyPrint(userList);
        FileUtils.writeToFile(FileUrls.userUrl, newUserJsonFromObject);
    }
}
