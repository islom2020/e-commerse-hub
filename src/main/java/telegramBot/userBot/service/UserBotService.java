package telegramBot.userBot.service;

import com.ctc.wstx.shaded.msv_core.relaxns.reader.IncludeGrammarState;
import lombok.SneakyThrows;
import model.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import response.BaseResponse;
import service.*;
import telegramBot.userBot.UserInterface;

import javax.swing.text.html.parser.TagElement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserBotService{

    private UserService userService = new UserService();
    private CategoryService categoryService = new CategoryService();
    private HistoryService historyService = new HistoryService();
    private MyCartService myCartService = new MyCartService();
    private ProductService productService = new ProductService();
    private MyMessageService myMessageService = new MyMessageService();
    @SneakyThrows
    public SendMessage sharePhoneNumber(String chatId) {
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
    public SendMessage shareLocation(String chatId) {
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

    public SendMessage userMainMenu(String chatId){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("\uD83D\uDCCB Categories");
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("\uD83D\uDCD1 History");
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("\uD83D\uDED2 My Cart");
        keyboardRows.add(row);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        SendMessage sendMessage = new SendMessage(chatId, "MAIN MENU");
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    public SendMessage categories(String chatId){
        SendMessage sendMessage = new SendMessage(chatId, "\uD83D\uDCCB Categories");

        if (categoriesInlineMarkup() == null){
            sendMessage.setText("❗️ NO CATEGORIES AVAILABLE ❗️");
            return sendMessage;
        }
        else {
            sendMessage.setReplyMarkup(categoriesInlineMarkup());
        }
        return sendMessage;
    }

    public SendMessage myCart(String chatId, UUID userId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(myCartButtons(myCartService.getList(userId)));
        sendMessage.setText(getCartText(myCartService.getList(userId)));

        return sendMessage;
    }

    public SendMessage history(String chatId, UUID userId){
        return new SendMessage(chatId, String.valueOf(historyService.getUserHistories.getHistories(userId)));
    }

    public SendPhoto productInfoWithPhoto(String chatId, Integer messageId, String productIdStr){
        SendPhoto photo = new SendPhoto();
        Product product = productService.get(UUID.fromString(productIdStr));
        photo.setChatId(chatId);
        photo.setCaption(product.getProductInfo()
                + "\nPrice: " + product.getPrice()
                + "\n\nHOW MANY DO YOU WANT TO ADD TO YOUR CART? \uD83D\uDD22");
        photo.setPhoto(new InputFile(product.getFileUrlPhoto()));
        photo.setReplyMarkup(addToCartAmount(productService.get(UUID.fromString(productIdStr)).getCategoryId().toString()));

        return photo;
    }

    //CHANGED
    public EditMessageText products(String categoryIdStr, Integer messageId, String chatId){
        EditMessageText editCategoryToProduct = new EditMessageText();
        editCategoryToProduct.setText("==== PRODUCTS ====");
        editCategoryToProduct.setChatId(chatId);
        editCategoryToProduct.setMessageId(messageId);


        UUID categoryId = categoryService.get(UUID.fromString(categoryIdStr)).getId();


        InlineKeyboardMarkup productMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        int i = 2;

        List<Product> products = productService.getListByCategoryId(categoryId);

        if(products.size() == 0){
            editCategoryToProduct.setText("❗️ NO PRODUCTS AVAILABLE IN THIS CATEGORY ❗️");
        } else {
            for (Product product : products) {
                if (product.getCategoryId().equals(categoryId)) {
                    i--;

                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(product.getName());
                    button.setCallbackData(String.valueOf(product.getId()));

                    row.add(button);

                    if (i == 0) {
                        rows.add(row);
                        i = 2;
                        row = new ArrayList<>();
                    }
                }
            }

            if (row.size() != 0) {
                rows.add(row);
            }
        }

        row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("⬅️ back");
        button.setCallbackData("backToCategories");

        row.add(button);

        rows.add(row);

        productMarkup.setKeyboard(rows);
        editCategoryToProduct.setReplyMarkup(productMarkup);

        return editCategoryToProduct;
    }

    public EditMessageText editToCategories(Integer messageId, String chatId){
        EditMessageText backToCategories = new EditMessageText();
        backToCategories.setText("\uD83D\uDCCB Categories");
        backToCategories.setReplyMarkup(categoriesInlineMarkup());
        backToCategories.setMessageId(messageId);
        backToCategories.setChatId(chatId);

        return backToCategories;
    }

    public EditMessageText productInfo(String chatId, Integer messageId, String productIdStr) {
        EditMessageText info = new EditMessageText();
        info.setChatId(chatId);
        info.setMessageId(messageId);
        Product product = productService.get(UUID.fromString(productIdStr));
        info.setText(product.getProductInfo()
                + "\nPrice: " + product.getPrice()
                + "\n\nHOW MANY DO YOU WANT TO ADD TO YOUR CART? \uD83D\uDD22");
        info.setReplyMarkup(addToCartAmount(productService.get(UUID.fromString(productIdStr)).getCategoryId().toString()));

        return info;
    }


    //NEW
    public EditMessageText productAdded(String chatId, Integer messageId){
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText("SUCCESSFULLY ADDED ✅");
        editMessageText.setMessageId(messageId);
        editMessageText.setChatId(chatId);
        editMessageText.setReplyMarkup(productAddedButton());

        return editMessageText;
    }

    public EditMessageText editMyCart(Integer messageId, UUID userId, String chatId){
        EditMessageText sendMessage = new EditMessageText();
        sendMessage.setChatId(chatId);
        sendMessage.setMessageId(messageId);
        sendMessage.setReplyMarkup(myCartButtons(myCartService.getList(userId)));
        sendMessage.setText(getCartText(myCartService.getList(userId)));

        return sendMessage;
    }

    public EditMessageText editToBuy(Integer messageId, UUID userid, String chatId){
        EditMessageText edit = new EditMessageText("\uD83D\uDCB4 CHOOSE METHOD TO PAY \uD83D\uDCB4" +
                "Your purchase: " + myCartService.myPurchase(userid));
        edit.setChatId(chatId);
        edit.setMessageId(messageId);
        edit.setReplyMarkup(buyInlineMarkup());

        return edit;
    }


    public EditMessageText editToPay(Integer messageId, UUID userid, String chatId, String userName){
        pay(userid, userName);
        removeFromMyCart(userid);
        EditMessageText edit = new EditMessageText(BaseResponse.SUCCESS + "\nTHANKS FOR YOUR PURCHASE");
        edit.setChatId(chatId);
        edit.setMessageId(messageId);

        return edit;
    }

    public DeleteMessage deleteMessage(String chatId, Integer messageId){
        return new DeleteMessage(chatId, messageId);
    }

    //NEW
    private InlineKeyboardMarkup addToCartAmount(String categoryId){
        InlineKeyboardMarkup amounts = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton();
        for (int i = 1; i <= 10; i++) {
            button = new InlineKeyboardButton("" + i);
            button.setCallbackData("" + i);
            row.add(button);

            if (row.size() == 3) {
                rows.add(row);
                row = new ArrayList<>();
            }
        }

        button = new InlineKeyboardButton("⬅️ product list");
        button.setCallbackData("backToProductList");
        row.add(button);

        rows.add(row);

        amounts.setKeyboard(rows);
        return amounts;
    }

    private InlineKeyboardMarkup productAddedButton(){
        InlineKeyboardMarkup buttons = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton("\uD83D\uDED2 My Cart");
        button.setCallbackData("myCart");
        row.add(button);

        button = new InlineKeyboardButton("\uD83D\uDCCB Categories");
        button.setCallbackData("backToCategories");
        row.add(button);

        rows.add(row);

        button = new InlineKeyboardButton("⬅️back to product list");
        button.setCallbackData("backToProductList");

        rows.add(List.of(button));
        buttons.setKeyboard(rows);

        return buttons;
    }

    private InlineKeyboardMarkup myCartButtons(List<MyCart> myCartList){
        InlineKeyboardMarkup products = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        int i = 1;
        for (MyCart cart: myCartList) {
            InlineKeyboardButton button = new InlineKeyboardButton("➖");
            button.setCallbackData("" + i);
            i++;
            row.add(button);

            button = new InlineKeyboardButton(cart.getProductName());
            button.setCallbackData(" ");
            row.add(button);

            button = new InlineKeyboardButton("➕");
            button.setCallbackData("" + i);
            row.add(button);
            i++;
            rows.add(row);
            row = new ArrayList<>();
        }

        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("➕ add product");
        button.setCallbackData("backToCategories");
        row.add(button);

        button = new InlineKeyboardButton();
        button.setText("\uD83D\uDD16 Order");
        button.setCallbackData("order");
        row.add(button);

        rows.add(row);

        products.setKeyboard(rows);
        return products;
    }

    //CHANGED
    private InlineKeyboardMarkup categoriesInlineMarkup() {
        InlineKeyboardMarkup categoriesMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineButtonsRows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        List<Category> categories = categoryService.getCategoryListFromFile();

        if(categories.size() == 0){
            return null;
        }
        for (Category category: categories) {
            if (category.isActive()) {
                InlineKeyboardButton button = new InlineKeyboardButton(category.getName());
                button.setCallbackData(category.getId().toString());

                row.add(button);

                if (row.size() == 2) {
                    inlineButtonsRows.add(row);
                    row = new ArrayList<>();
                }
            }
        }

        if (row.size() != 0) {
            inlineButtonsRows.add(row);
        }

        categoriesMarkup.setKeyboard(inlineButtonsRows);
        return categoriesMarkup;
    }

    private InlineKeyboardMarkup buyInlineMarkup(){
        InlineKeyboardMarkup buyMethods = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton("\uD83D\uDCB3 Click");
        button.setCallbackData("click");
        button.setPay(true);
        row.add(button);

        button = new InlineKeyboardButton("\uD83D\uDCF2 PayMe");
        button.setCallbackData("payme");
        button.setPay(true);
        row.add(button);
        rows.add(row);

        button = new InlineKeyboardButton("❌ Cancel");
        button.setCallbackData("cancel");


        rows.add(List.of(button));
        buyMethods.setKeyboard(rows);


        return buyMethods;
    }



    private String getCartText(List<MyCart> myCartList){
        String text = "\uD83D\uDCDC PRODUCTS IN YOUR CART \uD83D\uDCDC\n\n";
        double overall = 0;
        //📜📌💸

        for (MyCart cart: myCartList) {
            text += "\uD83D\uDCCC" + cart.getProductName() + "  |  " + cart.getAmount()
                    + "  |  \uD83D\uDCB8" + (cart.getAmount()*cart.getPrice()) + "\n";
            overall += cart.getAmount()*cart.getPrice();
        }

        text += "\nYour overall purchase: " + overall;
        return text;
    }

    public void changeProductAmount(String callData, UUID userId){
        List<MyCart> myCartList = myCartService.getList(userId);

        int i = Integer.parseInt(callData);
        MyCart myCart = myCartList.get(i % 2 == 0 ? i/2-1 : i/2);
        myCart.setAmount(i % 2 == 0 ? myCart.getAmount() + 1 : myCart.getAmount() - 1 );

        if(myCart.getAmount() == 0) {
            myCartService.delete(myCart.getId());
            return;
        }
        myCartService.editById(myCart.getId(), myCart);
    }


    private void pay(UUID userId, String userName){
        double commission = 0;

        for(MyCart cart: myCartService.getList(userId)){
            Product product = productService.get(cart.getProductId());
            User user = userService.get(product.getSellerId());
            user.setBalance(user.getBalance() + cart.getAmount()* cart.getPrice() * 0.96);
            userService.editByChatId(user.getChatId(), user);
            commission += cart.getAmount()* cart.getPrice() * 0.04;

            historyService.add(createHistory(userId, user.getId(), userName, user.getName(), product.getName(), cart.getAmount()));
        }
    }

    private void removeFromMyCart(UUID userId){
        for (MyCart cart:myCartService.getList(userId)) {
            myCartService.delete(cart.getId());
        }
    }

    private History createHistory(UUID userId, UUID sellerId, String userName, String sellerName, String productName, int amount){
        return new History(userId, sellerId, userName, sellerName, productName, amount);

    }


    private String historyText(History history){
        return "From: " + history.getSellerName() + "\t|\tProduct: " + history.getProductName()
                + "\t|\tAmount: " + history.getAmount() + "\n";
    }

}

