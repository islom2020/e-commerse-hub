package service;

import jsonFile.CollectionsTypeFactory;
import jsonFile.FileUrls;
import jsonFile.FileUtils;
import jsonFile.Json;
import lombok.SneakyThrows;
import model.MyMessage;
import model.User;
import repository.MyMessageRepository;

import java.util.ArrayList;
import java.util.List;

import static response.BaseResponse.SUCCESS;


public class MyMessageService implements MyMessageRepository {

    @Override
    public List<MyMessage> getMyMessageListFromFile() {
        String myMessageJsonStringFromFile = FileUtils.readFromFile(FileUrls.myMessagesUrl);
        List<MyMessage> myMessageList;
        try {
            myMessageList = Json.objectMapper.readValue(myMessageJsonStringFromFile, CollectionsTypeFactory.listOf(MyMessage.class));
        } catch (Exception e) {
            System.out.println(e);
            myMessageList = new ArrayList<>();
        }
        return myMessageList;
    }

    @Override
    public MyMessage get(Integer messageId) {
        for (MyMessage mes :getMyMessageListFromFile()) {
            if (mes.getMessageId().equals(messageId))
                return mes;
        }
        return null;
    }

    @Override
    public List<MyMessage> getList() {
        return getMyMessageListFromFile();
    }

    @Override
    public void add(MyMessage myMessage) {
        if(get(myMessage.getMessageId()) != null) {
            editById(myMessage.getMessageId(), myMessage);
            return;
        }

        List<MyMessage> myMessageList = getMyMessageListFromFile();
        myMessageList.add(myMessage);
        setMyMessageListToFile(myMessageList);
    }

    @Override
    public void editById(Integer messageId, MyMessage editedMyMessage) {
        List<MyMessage> myMessageList = getMyMessageListFromFile();
        int index = 0;
        for (MyMessage message : myMessageList) {
            if (message.getMessageId().equals(messageId)){
                myMessageList.set(index, editedMyMessage);
                break;
            }
            index++;
        }

        setMyMessageListToFile(myMessageList);
    }

    @SneakyThrows
    public void setMyMessageListToFile(List<MyMessage> myMessageList) {
        String newMyMessageJsonFromObject = Json.prettyPrint(myMessageList);
        FileUtils.writeToFile(FileUrls.myMessagesUrl, newMyMessageJsonFromObject);
    }
}
