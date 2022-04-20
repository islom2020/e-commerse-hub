package model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import telegramBot.userBot.UserState;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class MyMessage {


    private Integer messageId;


    private UserState messageState;

    /*public MyMessage(Integer messageId, UserState userState) {
        this.messageId = messageId;
        this.messageState = userState;
    }*/


}
