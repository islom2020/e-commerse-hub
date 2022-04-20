package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Notification extends BaseModel {
    String message;
    String sellerChatId;
    // isActive  ->  have been read;
    // isNotActive -> have not been read


    @Override
    public String toString() {
        return " SELLER NAME :     " + name +
                "\ncreatedDate :     " + createdDate.getTime() +
                "\ncreatedBy :    " + createdBy +
                "\nisActive :      " + isActive +
                "\nmessage :  " + message;
    }
}
