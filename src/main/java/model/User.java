package model;


import enums.RoleUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Location;
import telegramBot.sellerBot.NewSellerState;
import telegramBot.userBot.UserState;


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor

@Data
public class User extends BaseModel {

    private String username;
    private String phoneNumber;
    private RoleUser role;
    private double balance;
    private String chatId;
    private UserState userState;
    private NewSellerState sellerState;
    private Location location;

    @Override
    public String toString() {
        String str = "| NAME='" + name +
                "\t|\tCREATED DATE: " + createdDate;
        if (updatedDate != null) str += "\t|\tUPDATED DATE: " + updatedDate;
        str += "\t|\tIS ACTIVE: " + isActive +
                "\t|\tUSERNAME: " + username +
                "\t|\tPHONE NUMBER: " + phoneNumber +
                "\t|\tLOCATION: " + location +
                "\t|\tBALANCE: " + balance + " |";
        return str;
    }
}