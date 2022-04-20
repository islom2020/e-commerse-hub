package telegramBot.newSeller;

import lombok.SneakyThrows;
import model.Category;
import model.Product;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import service.*;
import telegramBot.sellerBot.NewSellerBot;

import java.util.ArrayList;
import java.util.List;

public interface NewSellerInterface {
    String SELLER_BOT_USERNAME = "https://t.me/SampleForMyProject_Bot";
    String SELLER_BOT_TOKEN = "5065478999:AAFz-0f7WLe64CsMXCJT1uhNmXxTgjiy2a0";

    UserService userService = new UserService();
    CategoryService categoryService = new CategoryService();
    HistoryService historyService = new HistoryService();
    MyCartService myCartService = new MyCartService();

    ProductService productService = new ProductService();
    String NEXT_BUTTON = "next";
    String PREVIOUS_BUTTON = "prev";

    default SendMessage sellerMainMenu(String chatId){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("Product CRUD");
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("EDIT Profile");
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("History");
        keyboardRows.add(row);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        SendMessage sendMessage = new SendMessage(chatId, "MAIN MENU");
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    default SendMessage productCrudMenu(String chatId){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("Product list");
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("Add product");
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("Edit product");
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("Delete product");
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("Back");
        keyboardRows.add(row);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        SendMessage sendMessage = new SendMessage(chatId, "Product CRUD Menu");
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    @SneakyThrows
    default SendMessage sharePhoneNumber(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Welcome \uD83D\uDE0A\nSend your phone number");

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardButton button = new KeyboardButton();
        button.setText("\uD83D\uDCDE Share your phone number >");
        button.setRequestContact(true);

        keyboardRow.add(button);
        keyboard.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return sendMessage;
    }

    @SneakyThrows
    default SendMessage shareLocation(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Share your location >");

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardButton button = new KeyboardButton();
        button.setText("\uD83D\uDCCD Share location >");
        button.setRequestLocation(true);

        keyboardRow.add(button);
        keyboard.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return sendMessage;
    }

    default SendMessage getProductList(String chatId) {
        SendMessage sendMessage = new SendMessage();
        List<Product> productList = productService.getList();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineKeyboardButtonList = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(inlineKeyboardButtonList);

        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
        for (Product product : productList) {
            if (product.getSellerId().equals(chatId) && product.isActive()) {
                InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton(product.getName());
                inlineKeyboardButton1.setCallbackData(product.getName());
                inlineKeyboardButtons.add(inlineKeyboardButton1);
                inlineKeyboardButtonList.add(inlineKeyboardButtons);
                inlineKeyboardButtons = new ArrayList<>();
            }
        }
        sendMessage.setChatId(chatId);
        sendMessage.setText("product list");
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }

    default SendPhoto sendProductFullInfo(String chatId, String name){
        SendPhoto photo = new SendPhoto();
        List<Product> productList = productService.getList();
        for (Product product : productList) {
            if (product.getName().equals(name)){
                photo.setChatId(chatId);
                photo.setCaption(product.getProductInfo());
                photo.setPhoto(new InputFile(product.getFileUrlPhoto()));
                break;
            }
        }
        return photo;
    }

    default SendMessage addProductName(String chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Enter name of product");
        return sendMessage;
    }

    default SendMessage addProductInfo(String chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Enter something about product");
        return sendMessage;
    }

    default SendMessage addProductPrice(String chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Enter price of product");
        return sendMessage;
    }

    default SendMessage addProductPhoto(String chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Send photo of product");
        return sendMessage;
    }

    default SendMessage editProductName(String chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Enter name of product");
        return sendMessage;
    }

    default SendMessage editProductPrice(String chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Enter new price of product");
        return sendMessage;
    }

    default SendMessage editProductInfo(String chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Enter new data about product");
        return sendMessage;
    }

    default SendMessage editProductPhoto(String chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Send new photo of product");
        return sendMessage;
    }

    default SendMessage editProductActivation(String chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Enter \"true\" or \"false\" in order to set product activation");
        return sendMessage;
    }

    default SendMessage deleteProductName(String chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Enter name of product");
        return sendMessage;
    }

    default SendMessage deletedProductName(String chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("confirm by pressing 1 or 0");
        return sendMessage;
    }

    default List<InlineKeyboardButton> getPreviousButtonRow() {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(PREVIOUS_BUTTON);
        inlineKeyboardButton.setCallbackData(PREVIOUS_BUTTON);
        return List.of(inlineKeyboardButton);
    }

    default List<InlineKeyboardButton> getNextButtonRow() {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(NEXT_BUTTON);
        inlineKeyboardButton.setCallbackData(NEXT_BUTTON);
        return List.of(inlineKeyboardButton);
    }

    default List<InlineKeyboardButton> getPreviousNextButtonRow() {
        InlineKeyboardButton inlineKeyboardButtonPrevious = new InlineKeyboardButton(PREVIOUS_BUTTON);
        inlineKeyboardButtonPrevious.setCallbackData(PREVIOUS_BUTTON);
        InlineKeyboardButton inlineKeyboardButtonNext = new InlineKeyboardButton(NEXT_BUTTON);
        inlineKeyboardButtonNext.setCallbackData(NEXT_BUTTON);
        return List.of(inlineKeyboardButtonPrevious, inlineKeyboardButtonNext);
    }
}
