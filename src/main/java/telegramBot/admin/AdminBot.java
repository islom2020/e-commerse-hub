package telegramBot.admin;

import com.vdurmont.emoji.EmojiParser;
import enums.RoleUser;
import lombok.SneakyThrows;
import model.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import service.*;
import telegramBot.admin.service.AdminBotButtonsService;

import java.util.Date;
import java.util.List;

public class AdminBot extends TelegramLongPollingBot implements AdminInterface{
    UserService userService = new UserService();
    CategoryService categoryService = new CategoryService();
    HistoryService historyService = new HistoryService();
    NotificationService notificationService = new NotificationService();
    ProductService productService = new ProductService();
    AdminBotButtonsService adminBotButtonsService = new AdminBotButtonsService();


    private String chatId;
    private String messageFromUser;
    private String callData;
    private AdminState adminState;
    private final String adminPassword;
    public static double systemBalance;
    public Integer messageId;
    String messageFromBot;
    private boolean isAdminChecked = true;
    {
        adminPassword = ADMIN_PASSWORD;
    }
    private Category editingCategory;
    @Override
    public String getBotUsername() {
        return ADMIN_USERNAME;
    }

    @Override
    public String getBotToken() {
        return ADMIN_TOKEN;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            this.chatId = update.getMessage().getChatId().toString();
            messageFromUser = update.getMessage().getText();
            switch (messageFromUser) {
                case ADMIN_START : {
                    adminState = AdminState.START;
                    break;
                }
                case ADMIN_CHECK : {
                    adminState = AdminState.CHECK_ADMIN;
                    break;
                }
                case ADMIN_USER_LIST : {
                    adminState = (isAdminChecked) ? AdminState.GET_USER_LIST : AdminState.UNKNOWN_COMMAND;
                    break;
                }
                case ADMIN_SELLER_LIST : {
                    adminState = (isAdminChecked) ? AdminState.GET_SELLER_LIST : AdminState.UNKNOWN_COMMAND;
                    break;
                }
                case ADMIN_PRODUCT_LIST : {
                    adminState = (isAdminChecked) ? AdminState.GET_PRODUCT_LIST : AdminState.UNKNOWN_COMMAND;
                    break;
                }
                case ADMIN_BALANCE : {
                    adminState = (isAdminChecked) ? AdminState.BALANCE : AdminState.UNKNOWN_COMMAND;
                    break;
                }
                case ADMIN_HISTORY : {
                    adminState = (isAdminChecked) ? AdminState.HISTORY : AdminState.UNKNOWN_COMMAND;
                    break;
                }
                case ADMIN_NOTIFICATIONS : {
                    adminState = (isAdminChecked) ? AdminState.NOTIFICATIONS : AdminState.UNKNOWN_COMMAND;
                    break;
                }
                case ADMIN_SELLER_BLOCK : {
                    adminState = (isAdminChecked) ? AdminState.BLOCK_SELLER : AdminState.UNKNOWN_COMMAND;
                    break;
                }
                case ADMIN_CATEGORY : {
                    adminState = (isAdminChecked) ? AdminState.CATEGORY_CRUD : AdminState.UNKNOWN_COMMAND;
                    break;
                }
            }

            executeAdminState(adminState);

        }
        else if (update.hasCallbackQuery()) {
            this.callData = update.getCallbackQuery().getData();
            String queryMessageFromUser = update.getCallbackQuery().getMessage().getText();
            this.chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            this.messageId = update.getCallbackQuery().getMessage().getMessageId();

            switch (callData) {
                case ADMIN_BACK : {
                    break;
                }
                case ADMIN_ADD_CATEGORY: {
                    this.adminState = AdminState.ADD_CATEGORY;
                    break;
                }
                case ADMIN_EDIT_CATEGORY: {
                    this.adminState = AdminState.EDIT_CATEGORY;
                    break;
                }
                case ADMIN_DEACTIVATE_CATEGORY: {
                    this.adminState = AdminState.DELETE_CATEGORY;
                    break;
                }
                case ADMIN_ACTIVATE_CATEGORY: {
                    this.adminState = AdminState.ACTIVATE_CATEGORY;
                    break;
                }
                case SELLER_REQUEST: {
                    this.adminState = AdminState.SELLER_REQUEST;
                    break;
                }
                case NONE: {
                    this.adminState = AdminState.NONE;
                }
            }
            executeAdminState(adminState);
        }
    }

    @SneakyThrows
    public void executeAdminState(AdminState adminState) {
        if (adminState == null) adminState = AdminState.UNKNOWN_COMMAND;


        switch (adminState) {
            case START : {
                this.adminState = null;
                messageFromBot = EmojiParser.parseToUnicode("Welcome, admin bro :smile:");
                sendMessageToUser(messageFromBot);
                this.isAdminChecked = false;
                break;
            }
            case CHECK_ADMIN: {
                messageFromBot = EmojiParser.parseToUnicode("Enter admin password: ");
                this.adminState = AdminState.PASSWORD_CHECK;
                sendMessageToUser(messageFromBot);
                break;
            }
            case PASSWORD_CHECK: {
                this.passwordCheck();
                break;
            }
            case GET_USER_LIST: {
                this.getUserList();
                break;
            }
            case GET_SELLER_LIST: {
                this.getSellerList();
                break;
            }
            case GET_PRODUCT_LIST: {
                this.getProductList();
                break;
            }
            case BALANCE: {
                messageFromBot = EmojiParser.parseToUnicode(":dollar: :dollar: :dollar:\nsystem balance: $" + systemBalance);
                sendMessageToUser(messageFromBot);
                this.adminState = AdminState.UNKNOWN_COMMAND;
                break;
            }
            case HISTORY: {
                this.getHistory();
                break;
            }
            case NOTIFICATIONS: {
                this.getNotifications();
                break;
            }
            case SELLER_REQUEST: {
                this.answerSellerRequest();
                break;
            }
            case BLOCK_SELLER: {
                this.blockSeller();
                break;
            }
            case BLOCK_SELLER_PROCESS: {
                this.blockSellerProcess();
                break;
            }
            case CATEGORY_CRUD: {
                this.categoryCrud();
                break;
            }
            case ADD_CATEGORY: {
                messageFromBot = "Enter new category name: ";
                sendMessageToUser(messageFromBot);
                this.adminState = AdminState.ADDING_CATEGORY_PROCESS;
                break;
            }
            case ADDING_CATEGORY_PROCESS: {
                this.addingCategoryProcess();
                break;
            }
            case DELETE_CATEGORY: {
                messageFromBot = "enter category name: ";
                sendMessageToUser(messageFromBot);
                this.adminState = AdminState.DELETING_CATEGORY_PROCESS;
                break;
            }
            case DELETING_CATEGORY_PROCESS: {
                this.deletingCategoryProcess();
                break;
            }
            case ACTIVATE_CATEGORY: {
                messageFromBot = "enter activating category name: ";
                this.adminState = AdminState.ACTIVATING_CATEGORY_PROCESS;
                sendMessageToUser(messageFromBot);
                break;
            }
            case ACTIVATING_CATEGORY_PROCESS: {
                this.activatingCategoryProcess();
                break;
            }
            case EDIT_CATEGORY: {
                messageFromBot = EmojiParser.parseToUnicode("enter editing category name: :relieved:");
                this.adminState = AdminState.EDITING_CATEGORY_PROCESS;
                sendMessageToUser(messageFromBot);
                break;
            }
            case EDITING_CATEGORY_PROCESS: {
                this.editingCategoryProcess();
                break;
            }
            case RENAMING_CATEGORY_PROCESS: {
                this.editingCategory.setUpdatedDate(new Date());
                this.editingCategory.setName(this.messageFromUser);
                messageFromBot = categoryService.editById(this.editingCategory.getId(), this.editingCategory);
                sendMessageToUser(messageFromBot);
                this.adminState = AdminState.PROCESS_COMPLETED;
                break;
            }
            case UNKNOWN_COMMAND: {
                messageFromBot = EmojiParser.parseToUnicode("unknown command :expressionless: :expressionless: ");
                sendMessageToUser(messageFromBot);
                break;
            }
            case PROCESS_COMPLETED: {
                messageFromBot = EmojiParser.parseToUnicode(":zap: mission completed already :zap:");
                sendMessageToUser(messageFromBot);
            }
            case NONE: {
                break;
            }
        }
    }

    @SneakyThrows
    public void sendMessageToUser(String messageFromBot) {
        SendMessage sendMessage = new SendMessage(this.chatId, messageFromBot);
        execute(sendMessage);
    }

    @SneakyThrows
    public void sendMessageToUserWithReplyMarkup(String messageFromBot, ReplyKeyboardMarkup replyKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage(this.chatId, messageFromBot);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        execute(sendMessage);
    }

    @SneakyThrows
    public void sendMessageToUserWithInlineMarkup(String messageFromBot, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage(this.chatId, messageFromBot);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        execute(sendMessage);
    }

    @SneakyThrows
    public void editMessageWithInlineKeyboard(String messageFromBot, InlineKeyboardMarkup inlineKeyboardMarkup) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(this.messageId);
        editMessageText.setChatId(this.chatId);
        editMessageText.setText(messageFromBot);
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
        execute(editMessageText);
    }

    public void passwordCheck() {
        if (this.messageFromUser.equals(this.adminPassword)) {
            this.isAdminChecked = true;
            messageFromBot = EmojiParser.parseToUnicode("Admin successfully verified, :white_check_mark::relaxed:");
            sendMessageToUserWithReplyMarkup(messageFromBot, adminBotButtonsService.adminMENU());
            this.adminState = AdminState.PROCESS_COMPLETED;

        } else {
            this.isAdminChecked = false;
            messageFromBot = EmojiParser.parseToUnicode("wrong password: :angry:, reenter");
            sendMessageToUser(messageFromBot);
            this.adminState = AdminState.PASSWORD_CHECK;
        }
    }
    public void getUserList() {
        List<User> userList = userService.getAllUsers(RoleUser.USER);
        if (userList == null || userList.size() == 0) {
            messageFromBot = "No users in our system";
            sendMessageToUser(messageFromBot);
        }
        else {
            int index = 1;
            messageFromBot = "All users in our system:";
            sendMessageToUser(messageFromBot);
            for (User user : userList) {
                messageFromBot = (index ++) + ". " + user.toString();
                sendMessageToUser(messageFromBot);
            }
        }
        this.adminState = AdminState.UNKNOWN_COMMAND;
    }
    public void getSellerList() {
        List<User> sellerList = userService.getAllUsers(RoleUser.SELLER);
        if (sellerList == null || sellerList.size() == 0) {
            messageFromBot = "No sellers in our system";
            sendMessageToUser(messageFromBot);
        }
        else {
            int index = 1;
            messageFromBot = "All sellers in our system:";
            sendMessageToUser(messageFromBot);
            for (User user : sellerList) {
                messageFromBot = (index ++) + ". " + user.toString();
                sendMessageToUser(messageFromBot);
            }
        }
        this.adminState = AdminState.UNKNOWN_COMMAND;
    }
    public void getProductList() {
        List<Product> productList = productService.getList();
        if (productList == null || productList.size() == 0) {
            messageFromBot = "no products in our system";
            sendMessageToUser(messageFromBot);
        }
        else {
            int index = 1;
            messageFromBot = "All products in our system";
            sendMessageToUser(messageFromBot);
            for (Product product : productList) {
                messageFromBot = (index ++) + ". " + product.toString();
                sendMessageToUser(messageFromBot);
            }
        }
        this.adminState = AdminState.UNKNOWN_COMMAND;
    }
    public void getHistory() {
        List<History> historyList = historyService.getList();
        if (historyList == null || historyList.size() == 0) {
            messageFromBot = "no history in our system";
            sendMessageToUser(messageFromBot);
        }
        else {
            int index = 1;
            messageFromBot = EmojiParser.parseToUnicode("All histories :smile:");
            sendMessageToUser(messageFromBot);
            for (History history : historyList) {
                messageFromBot = (index ++) + ". " + history.toString();
                sendMessageToUser(messageFromBot);
            }
        }
        this.adminState = AdminState.UNKNOWN_COMMAND;
    }
    public void getNotifications() {
        List<Notification> notificationList = notificationService.getList();
        if (notificationList == null || notificationList.size() == 0) {
            messageFromBot = EmojiParser.parseToUnicode("No notifications in our system: :hushed:");
            sendMessageToUser(messageFromBot);
            this.adminState = AdminState.UNKNOWN_COMMAND;
        }
        else {
            messageFromBot = "!!! You must reject or accept sellers' requests !!!";
            sendMessageToUser(messageFromBot);
            for (Notification notification : notificationList) {
                messageFromBot = notification.toString();
                sendMessageToUserWithInlineMarkup(messageFromBot, adminBotButtonsService.notificationButtons(notification.getSellerChatId()));
            }
            this.adminState = AdminState.SELLER_REQUEST;
        }
    }
    public void answerSellerRequest() {
        String[] s = this.callData.split(" ");
        User user = userService.login(s[1]);

        Notification notification = notificationService.getBySellerChatId(user.getChatId());
        notification.setActive(false);
        notificationService.editBySellerId(notification.getSellerChatId(), notification);
        if (s[0].equals(ADMIN_ACCEPT_SELLER_REQUEST)) {
            user.setActive(true);
            userService.editByChatId(user.getChatId(), user);
            messageFromBot = "activated successfully";
            sendMessageToUser(messageFromBot);

            editMessageWithInlineKeyboard(notification.toString(), adminBotButtonsService.acceptedOrRejectedSeller(EmojiParser.parseToUnicode("accepted :white_check_mark:")));
        }
        else if (s[0].equals(ADMIN_REJECT_SELLER_REQUEST)) {
            user.setActive(false);
            userService.editByChatId(user.getChatId(), user);
            messageFromBot = "seller request rejected!!!";
            sendMessageToUser(messageFromBot);
            editMessageWithInlineKeyboard(notification.toString(), adminBotButtonsService.acceptedOrRejectedSeller(EmojiParser.parseToUnicode("rejected :x:")));
        }
    }
    public void blockSeller() {
        List<User> sellerList = userService.getUsers(RoleUser.SELLER);
        if (sellerList == null || sellerList.size() == 0) {
            messageFromBot = "no sellers yet, bro";
            sendMessageToUser(messageFromBot);
            this.adminState = AdminState.UNKNOWN_COMMAND;
        } else {
            int index = 0;
            StringBuilder sb = new StringBuilder();
            sb.append("All sellers in our system: \n\n");
            for (User seller : sellerList) {
                sb.append("\n").append(index++).append(". ").append(seller.getName()).append(" -> ").append(seller.getPhoneNumber());
            }
            messageFromBot = EmojiParser.parseToUnicode(sb.toString());
            this.adminState = AdminState.BLOCK_SELLER_PROCESS;
            sendMessageToUserWithInlineMarkup(messageFromBot, adminBotButtonsService.sellerBlockIndex(sellerList));
        }
    }
    public void blockSellerProcess() {
        User user = userService.login(this.callData);
        if (user == null) {
            messageFromBot = EmojiParser.parseToUnicode("this seller already blocked :smirk:");
        }
        else {
            user.setActive(false);
            userService.editById(user.getId(), user);
            messageFromBot = EmojiParser.parseToUnicode(user.getUsername() + " blocked successfully! :white_check_mark:");
        }
        sendMessageToUser(messageFromBot);
        this.adminState = AdminState.PROCESS_COMPLETED;
    }
    public void categoryCrud() {
        List<Category> allCategories = categoryService.getList();
        if (allCategories == null || allCategories.size() == 0) {
            messageFromBot = "no category, yet";
            sendMessageToUser(messageFromBot);
        }
        else {
            int index = 1;
            StringBuilder sb = new StringBuilder();
            sb.append("All categories in our system : \n\n");
            String isActive;
            for (Category category : allCategories) {
                isActive = category.isActive() ? "active" : "deactivated";
                sb.append("\n").append(index++).append(". ").append(category.getName()).append(" -- ").append(isActive);
            }
            messageFromBot = sb.toString();
            sendMessageToUserWithInlineMarkup(messageFromBot, adminBotButtonsService.categoryCRUD());
        }
        this.adminState = AdminState.UNKNOWN_COMMAND;
    }
    public void addingCategoryProcess() {
        if (this.messageFromUser == null) {
            messageFromBot = EmojiParser.parseToUnicode("please enter valid name! :worried:");
            this.adminState = AdminState.ADDING_CATEGORY_PROCESS;
        }
        else {
            Category category = new Category();
            category.setName(this.messageFromUser);
            category.setActive(true);
            category.setCreatedDate(new Date());
            category.setUpdatedDate(new Date());
            messageFromBot = categoryService.add(category);
        }
        sendMessageToUser(messageFromBot);
        this.adminState = AdminState.PROCESS_COMPLETED;
    }
    public void deletingCategoryProcess() {
        Category category = categoryService.getByCategoryName(this.messageFromUser);
        if (category == null) {
            messageFromBot = EmojiParser.parseToUnicode("no such category in our system! :worried:, reenter");
            this.adminState = AdminState.DELETING_CATEGORY_PROCESS;
            sendMessageToUser(messageFromBot);
        }
        else if (!category.isActive()){
            messageFromBot = EmojiParser.parseToUnicode("this category is already blocked :blush:");
            sendMessageToUser(messageFromBot);
            this.adminState = AdminState.PROCESS_COMPLETED;
        }
        else {
            category.setUpdatedDate(new Date());
            category.setActive(false);
            messageFromBot = categoryService.editById(category.getId(), category);
            sendMessageToUser(messageFromBot);
            this.adminState = AdminState.PROCESS_COMPLETED;
        }
    }
    public void activatingCategoryProcess() {
        Category category = categoryService.getByCategoryName(this.messageFromUser);
        if (category == null) {
            messageFromBot = EmojiParser.parseToUnicode("no such category in our system! :worried:, reenter");
            this.adminState = AdminState.ACTIVATING_CATEGORY_PROCESS;
            sendMessageToUser(messageFromBot);
        }
        else if (category.isActive()){
            messageFromBot = EmojiParser.parseToUnicode("this category is already active :relieved:");
            sendMessageToUser(messageFromBot);
            this.adminState = AdminState.PROCESS_COMPLETED;
        }
        else {
            category.setUpdatedDate(new Date());
            category.setActive(true);
            messageFromBot = categoryService.editById(category.getId(), category);
            sendMessageToUser(messageFromBot);
            this.adminState = AdminState.PROCESS_COMPLETED;
        }
    }
    public void editingCategoryProcess() {
        if (this.messageFromUser == null) {
            messageFromBot = EmojiParser.parseToUnicode(":unamused: please, enter valid name! :unamused:");
            this.adminState = AdminState.EDITING_CATEGORY_PROCESS;
        }
        else {
            Category category = categoryService.getByCategoryName(this.messageFromUser);
            if (category == null) {
                messageFromBot = EmojiParser.parseToUnicode("no such category in our system :anguished:, reenter");
                this.adminState = AdminState.EDITING_CATEGORY_PROCESS;
            }
            else {
                this.editingCategory = category;
                messageFromBot = "enter a new name:";
                this.adminState = AdminState.RENAMING_CATEGORY_PROCESS;
            }
        }
        sendMessageToUser(messageFromBot);
    }
}
