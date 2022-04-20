package telegramBot.admin.service;

import com.vdurmont.emoji.EmojiParser;
import model.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import telegramBot.admin.AdminInterface;

import java.util.ArrayList;
import java.util.List;

public class AdminBotButtonsService implements AdminInterface {

    public ReplyKeyboardMarkup adminMENU() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(ADMIN_USER_LIST);
        keyboardRow.add(ADMIN_SELLER_LIST);
        keyboardRowList.add(keyboardRow);

        keyboardRow = new KeyboardRow();
        keyboardRow.add(ADMIN_PRODUCT_LIST);
        keyboardRow.add(ADMIN_NOTIFICATIONS);
        keyboardRowList.add(keyboardRow);

        keyboardRow = new KeyboardRow();
        keyboardRow.add(ADMIN_HISTORY);
        keyboardRow.add(ADMIN_BALANCE);
        keyboardRowList.add(keyboardRow);

        keyboardRow = new KeyboardRow();
        keyboardRow.add(ADMIN_SELLER_BLOCK);
        keyboardRow.add(ADMIN_CATEGORY);
        keyboardRowList.add(keyboardRow);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        return replyKeyboardMarkup;
    }

    public InlineKeyboardMarkup sellerBlockIndex(List<User> sellerList) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(rowsLine);

        List<InlineKeyboardButton> rowLine = new ArrayList<>();

        for (int i = 0; i < sellerList.size(); i++) {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(i + "");
            inlineKeyboardButton.setCallbackData(sellerList.get(i).getChatId());
            rowLine.add(inlineKeyboardButton);
        }
        rowsLine.add(rowLine);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup categoryCRUD() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(rowsLine);
        // first row
        List<InlineKeyboardButton> rowLine = new ArrayList<>();

        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setCallbackData(ADMIN_ADD_CATEGORY);
        inlineKeyboardButton.setText("add category");
        rowLine.add(inlineKeyboardButton);

        inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setCallbackData(ADMIN_EDIT_CATEGORY);
        inlineKeyboardButton.setText("edit category");
        rowLine.add(inlineKeyboardButton);

        rowsLine.add(rowLine);

        rowLine = new ArrayList<>();
        inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setCallbackData(ADMIN_DEACTIVATE_CATEGORY);
        inlineKeyboardButton.setText("deactivate category");
        rowLine.add(inlineKeyboardButton);

        inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setCallbackData(ADMIN_ACTIVATE_CATEGORY);
        inlineKeyboardButton.setText("activate category");
        rowLine.add(inlineKeyboardButton);

        rowsLine.add(rowLine);

        // second row
        rowLine = new ArrayList<>();
        inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setCallbackData(ADMIN_SEND_CATEGORY_INFO_FILE);
        inlineKeyboardButton.setText("category full info");
        rowLine.add(inlineKeyboardButton);

        inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setCallbackData(ADMIN_BACK);
        inlineKeyboardButton.setText("back");
        rowLine.add(inlineKeyboardButton);

        rowsLine.add(rowLine);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup notificationButtons(String sellerChatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(rowsLine);

        List<InlineKeyboardButton> rowLine = new ArrayList<>();

        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setCallbackData(ADMIN_ACCEPT_SELLER_REQUEST + " " + sellerChatId);
        inlineKeyboardButton.setText(EmojiParser.parseToUnicode(ADMIN_ACCEPT_SELLER_REQUEST + " :white_check_mark:"));
        rowLine.add(inlineKeyboardButton);

        inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setCallbackData(ADMIN_REJECT_SELLER_REQUEST + " " + sellerChatId);
        inlineKeyboardButton.setText(EmojiParser.parseToUnicode(ADMIN_REJECT_SELLER_REQUEST + " :x:"));
        rowLine.add(inlineKeyboardButton);

        rowsLine.add(rowLine);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup acceptedOrRejectedSeller(String answerToSeller) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsLine = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(rowsLine);

        List<InlineKeyboardButton> rowLine = new ArrayList<>();


        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setCallbackData(NONE);
        inlineKeyboardButton.setText(answerToSeller);
        rowLine.add(inlineKeyboardButton);

        rowsLine.add(rowLine);

        return inlineKeyboardMarkup;
    }
}
