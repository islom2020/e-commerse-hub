package repository;

import model.MyMessage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface MyMessageRepository {

    List<MyMessage> getMyMessageListFromFile() ;

    MyMessage get(Integer messageId);

    List<MyMessage> getList();

    void add(MyMessage myMessage);

    void editById(Integer messageId, MyMessage myMessage);
}
