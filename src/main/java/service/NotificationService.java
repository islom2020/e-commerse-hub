package service;

import jsonFile.CollectionsTypeFactory;
import jsonFile.FileUrls;
import jsonFile.FileUtils;
import jsonFile.Json;
import lombok.SneakyThrows;
import model.Notification;
import repository.NotificationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotificationService extends NotificationRepository {
    @Override
    public Notification get(UUID id) {
        for (Notification n: getNotificationListFromFile()) {
            if(n.getId().equals(id))
                return n;
        }
        return null;
    }

    public Notification getBySellerChatId(String sellerChatId) {
        for (Notification notification : getNotificationListFromFile()) {
            if (notification.getSellerChatId().equals(sellerChatId)) {
                return notification;
            }
        }
        return null;
    }

    @Override
    public List<Notification> getList() {
        List<Notification> notificationList = new ArrayList<>();
        for (Notification notification : getNotificationListFromFile()) {
            if (notification.isActive()) {
                notificationList.add(notification);
            }
        }
        return notificationList;
    }

    @Override
    public List<Notification> getList(UUID id) {
        return null;
    }

    @Override
    public String add(Notification notification) {
        List<Notification> notificationList = getNotificationListFromFile();
        notificationList.add(notification);
        setNotificationListToFile(notificationList);
        return SUCCESS;
    }

    @Override
    public String editById(UUID id, Notification notification) {
        return null;
    }


    public String editBySellerId(String sellerChatId, Notification editingNotification) {
        List<Notification> notificationList = getNotificationListFromFile();
        for (Notification notification : notificationList) {
            if (notification.getSellerChatId().equals(sellerChatId)) {
                notification.setActive(editingNotification.isActive());
                notification.setName(editingNotification.getName());
                notification.setMessage(editingNotification.getMessage());

                setNotificationListToFile(notificationList);
                return SUCCESS;
            }
        }
        return ERROR_NOTIFICATION_NOT_FOUND;
    }

    public boolean acceptReject(int action){

        return action == 1; // accepted
    }

    public List<Notification> getNotificationListFromFile() {
        String notificationJsonStringFromFile = FileUtils.readFromFile(FileUrls.notificationUrl);
        List<Notification> notificationList;
        try {
            notificationList = Json.objectMapper.readValue(notificationJsonStringFromFile, CollectionsTypeFactory.listOf(Notification.class));
        } catch (Exception e) {
            System.out.println(e);
            notificationList = new ArrayList<>();
        }
        return notificationList;
    }

    @SneakyThrows
    public void setNotificationListToFile(List<Notification> notificationList) {
        String newNotificationJsonFromObject = Json.prettyPrint(notificationList);
        FileUtils.writeToFile(FileUrls.notificationUrl, newNotificationJsonFromObject);
    }
}
