package telegramBot.userBot;


import enums.RoleUser;
import lombok.SneakyThrows;
import model.MyCart;
import model.MyMessage;
import model.Product;
import model.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import service.*;
import telegramBot.userBot.service.UserBotService;

import java.util.HashMap;
import java.util.UUID;

public class NewUserBot extends TelegramLongPollingBot implements UserInterface {

    public HashMap<Integer, String> messageCategory = new HashMap<>();
    public HashMap<Integer, String> messageProduct = new HashMap<>();

    public UserService userService = new UserService();
    public CategoryService categoryService = new CategoryService();
    public HistoryService historyService = new HistoryService();
    public MyCartService myCartService = new MyCartService();
    public ProductService productService = new ProductService();
    public UserBotService userBotService = new UserBotService();
    public MyMessageService myMessageService = new MyMessageService();

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();
            String chatId = message.getChatId().toString();
            User currentUser = userService.login(chatId);
            UserState userState = UserState.MAIN_MENU;


            if (message.hasText() && !message.getText().equals("/start")){
                String text = message.getText();

                if(text.equals("📋 Categories")) {
                    userState = UserState.CATEGORIES;
                }
                else if(text.equals("\uD83D\uDED2 My Cart"))
                    userState = UserState.MY_CART;
                else if (text.equals("\uD83D\uDCD1 History"))
                    userState = UserState.HISTORY;
            }
            else if (message.hasContact()) {
                userState = UserState.START_CONTACT_SHARED;
            }
            else if (message.hasLocation()) {
                userState = UserState.START_LOCATION;
            }
            else if (currentUser == null) {
                userState = UserState.START_NEW_USER;
            }
            else if (currentUser.getLocation() == null) {
                execute(userBotService.shareLocation(chatId));
                return;
            }
            else if(message.getText().equals("/start")){
                userState = UserState.MAIN_MENU;
            }
            else
                userState = currentUser.getUserState();

            switch (userState) {
                case START_NEW_USER: {
                    execute (userBotService.sharePhoneNumber(chatId));
                } break;
                case START_CONTACT_SHARED: {
                    userService.add(createUser(message));
                    execute(userBotService.shareLocation(chatId));
                } break;
                case START_LOCATION: {
                    Location location = message.getLocation();
                    currentUser.setLocation(location);
                    currentUser.setUserState(UserState.MAIN_MENU);
                    userService.editByChatId(chatId, currentUser);
                    execute(userBotService.userMainMenu(chatId));
                } break;
                case MAIN_MENU: {
                        execute(userBotService.userMainMenu(chatId));
                } break;
                case CATEGORIES: {
                    changeUserState(UserState.PRODUCTS, currentUser);
                    execute(userBotService.categories(chatId));
                } break;
                case MY_CART:{
                    changeUserState(UserState.CHANGE_PRODUCT_AMOUNT, currentUser);
                    userService.editByChatId(chatId, currentUser);
                    execute(userBotService.myCart(chatId, currentUser.getId()));
                } break;
                case HISTORY: {
                    execute(userBotService.history(chatId,currentUser.getId()));
                } break;
                default:

            }

        }
        else if (update.hasCallbackQuery()){
            CallbackQuery callbackQuery = update.getCallbackQuery();

            String callData = callbackQuery.getData();
            Integer messageId = callbackQuery.getMessage().getMessageId();
            String chatId = callbackQuery.getMessage().getChatId().toString();
            User currentUser = userService.login(chatId);

            UserState userState = currentUser.getUserState();



            if (callData.equals("backToCategories") || callData.equals("cancel")) {
                userState = UserState.CATEGORIES;
            }
            else if (callData.equals("backToProductList")) {
                userState = UserState.PRODUCTS;
                callData = messageCategory.get(messageId);
            }
            else if (callData.equals("myCart")){
                userState = UserState.MY_CART;
            }
            else if(callData.equals("order")){
                userState = UserState.ORDER;
            }
            else if(callData.equals("payme") || callData.equals("click"))
                userState = UserState.PAY;
            else if(myMessageService.get(messageId) != null){
                userState = myMessageService.get(messageId).getMessageState();
            }


            switch (userState){
                case PRODUCTS: {
                    messageCategory.put(messageId, callData);
                    userState = UserState.PRODUCT_INFO;
                    changeUserState(UserState.PRODUCT_INFO, currentUser);
                    userService.editByChatId(chatId, currentUser);
                    execute(userBotService.products(callData, messageId, chatId));
                } break;

                case CATEGORIES: {
                    userState = UserState.PRODUCTS;
                    changeUserState(UserState.PRODUCTS, currentUser);
                    execute(userBotService.editToCategories(messageId, chatId));
                } break;

                case PRODUCT_INFO:{
                    userState = UserState.ADD_TO_CART;
                    messageProduct.put(messageId, callData);
                    changeUserState(UserState.ADD_TO_CART, currentUser);
                    execute(userBotService.productInfo(chatId, messageId, callData));
                } break;

                case ADD_TO_CART:{
                    userState = UserState.PRODUCT_INFO;
                    createMyCart(callData, currentUser.getId(), messageId);
                    changeUserState(UserState.PRODUCT_INFO, currentUser);
                    execute(userBotService.productAdded(chatId, messageId));
                } break;

                case MY_CART: {
                    userState = UserState.CHANGE_PRODUCT_AMOUNT;
                    changeUserState(UserState.CHANGE_PRODUCT_AMOUNT, currentUser);
                    execute(userBotService.myCart(chatId, currentUser.getId()));
                } break;

                case CHANGE_PRODUCT_AMOUNT: {
                    userBotService.changeProductAmount(callData, currentUser.getId());
                    execute(userBotService.editMyCart(messageId, currentUser.getId(), chatId));
                } break;
                case ORDER: {
                    execute(userBotService.editToBuy(messageId, currentUser.getId(), chatId));
                } break;
                case PAY: {
                    execute(userBotService.editToPay(messageId, currentUser.getId(), chatId, currentUser.getName()));
                } break;

            }

            myMessageService.add(new MyMessage(messageId, userState));

        }

    }

    private User createUser(Message message){
        String phoneNumber = message.getContact().getPhoneNumber();
        User user = new User();
        user.setRole(RoleUser.USER);
        user.setPhoneNumber(phoneNumber);
        user.setChatId(message.getChatId().toString());
        if (message.getChat().getUserName() != null)
            user.setUsername(message.getChat().getUserName());

        user.setName(message.getContact().getFirstName());
        user.setUserState(UserState.START_LOCATION);

        return user;
    }

    private void createMyCart(String callData, UUID userId, Integer messageId){
        Product product = productService.get(UUID.fromString(messageProduct.get(messageId)));
        MyCart myCart = new MyCart();
        myCart.setPrice(product.getPrice());
        myCart.setUserId(userId);
        myCart.setProductName(product.getName());
        myCart.setAmount(Integer.parseInt(callData));
        myCart.setProductId(product.getId());
        myCartService.add(myCart);
    }

    private void changeUserState(UserState userState, User user){
        user.setUserState(userState);
        userService.editByChatId(user.getChatId(), user);
    }

    private void changeMessageState(UserState messageState, MyMessage message){
        message.setMessageState(messageState);
        myMessageService.editById(message.getMessageId(), message);
    }
}
