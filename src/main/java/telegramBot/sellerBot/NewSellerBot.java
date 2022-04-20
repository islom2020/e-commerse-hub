package telegramBot.sellerBot;

import enums.RoleUser;
import lombok.SneakyThrows;
import model.Category;
import model.Notification;
import model.Product;
import model.User;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NewSellerBot extends TelegramLongPollingBot implements NewSellerInterface {
    Map<String, String> map = new HashMap<>();
    Map<String, Integer> productStatus = new HashMap<>();
    Map<String, String> categoryStatus = new HashMap<>();
    Map<String, Integer> categoryProductStatus = new HashMap<>();

    @Override
    public String getBotUsername() {
        return "https://t.me/SampleForMyProject_Bot";
    }

    @Override
    public String getBotToken() {
        return "5065478999:AAFz-0f7WLe64CsMXCJT1uhNmXxTgjiy2a0";
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();
            String chatId = message.getChatId().toString();
            User currentUser = userService.login(chatId);
            String name = map.get(chatId);
            Product currentProduct = productService.checkProduct(name);
            NewSellerState sellerState;

            if (message.hasText() && !message.getText().equals("/start")) {
                String text = message.getText();

                if (text.equals("Product CRUD"))
                    sellerState = NewSellerState.PRODUCT_CRUD;

                else if (text.equals("EDIT Profile"))
                    sellerState = NewSellerState.EDIT_PROFILE;

                else if (text.equals("History"))
                    sellerState = NewSellerState.HISTORY;

                else if (text.equals("Product list"))
                    sellerState = NewSellerState.PRODUCT_LIST;

                else if (text.equals("Add product"))
                    sellerState = NewSellerState.ADD_PRODUCT;

                else if (text.equals("Edit product"))
                    sellerState = NewSellerState.EDIT_PRODUCT;

                else if (text.equals("Delete product"))
                    sellerState = NewSellerState.DELETE_PRODUCT;

                else if (text.equals("Back"))
                    sellerState = NewSellerState.MAIN_MENU;

                else sellerState = currentUser.getSellerState();

            }
            else if (message.hasContact()) {
                sellerState = NewSellerState.START_CONTACT_SHARED;
            }
            else if (message.hasLocation()) {
                sellerState = NewSellerState.START_LOCATION;
            }
            else if (currentUser == null) {
                sellerState = NewSellerState.START_NEW_SELLER;
            }
            else if (currentUser.getLocation() == null) {
                execute(shareLocation(chatId));
                sellerState = NewSellerState.START_LOCATION;
            }
            else if (message.hasPhoto()) {
                if (productStatus.get(chatId) == 0) {
                    sellerState = NewSellerState.ADD_PRODUCT_PHOTO;
                } else {
                    sellerState = NewSellerState.EDIT_PRODUCT_PHOTO;
                }
            }
            else {
                if (message.getText().equals("/start"))
                    sellerState = NewSellerState.MAIN_MENU;
                else
                    sellerState = currentUser.getSellerState();
            }

            switch (sellerState) {
                case START_NEW_SELLER: {
                    execute(sharePhoneNumber(chatId));
                }
                break;
                case START_CONTACT_SHARED: {
                    String phoneNumber = message.getContact().getPhoneNumber();
                    User user = new User();
                    user.setActive(false);
                    user.setRole(RoleUser.SELLER);
                    user.setPhoneNumber(phoneNumber);
                    user.setChatId(chatId);
                    if (message.getChat().getUserName() != null)
                        user.setUsername(message.getChat().getUserName());

                    user.setName(message.getContact().getFirstName());
                    user.setSellerState(NewSellerState.START_LOCATION);
                    userService.add(user);

                    execute(shareLocation(chatId));
                }
                break;
                case START_LOCATION: {
                    Location location = message.getLocation();
                    currentUser.setLocation(location);
                    if (currentUser.isActive()) {
                        currentUser.setSellerState(NewSellerState.MAIN_MENU);
                        userService.editByChatId(chatId, currentUser);
                        execute(sellerMainMenu(chatId));
                    } else {
                        currentUser.setSellerState(NewSellerState.MESSAGE_TO_ADMIN);
                        userService.editByChatId(chatId, currentUser);
                        execute(sendMessageToAdmin(chatId));
                    }
                }
                break;
                case MESSAGE_TO_ADMIN: {
                    Notification notification = new Notification();
                    notification.setMessage(message.getText());
                    notification.setActive(true);
                    notification.setName(currentUser.getName());
                    notification.setSellerChatId(chatId);
                    notification.setCreatedDate(new Date());
                    notificationService.add(notification);

                    execute(messageFromBot(chatId));
                }
                break;
                case PRODUCT_CRUD: {
                    categoryProductStatus.put(chatId, 0);
                    /*currentUser.setSellerState(NewSellerState.PRODUCT_CRUD);
                    userService.editByChatId(chatId, currentUser);
                    execute(productCrudMenu(chatId));*/
                    currentUser.setSellerState(NewSellerState.PRODUCT_CRUD);
                    userService.editByChatId(chatId, currentUser);
                    execute(getCategoryList(chatId));
                }
                break;
                case PRODUCT_LIST: {
                    String categoryId = "";
                    List<Category> categoryList = categoryService.getList();
                    for (Category category : categoryList) {
                        if (category.getName().equals(categoryStatus.get(chatId))){
                            categoryId = category.getId().toString();
                        }
                    }
                    categoryProductStatus.put(chatId, 1);
                    currentUser.setSellerState(NewSellerState.MAIN_MENU);
                    userService.editByChatId(chatId, currentUser);
                    execute(getProductList(chatId, categoryId));
                }
                break;
                case ADD_PRODUCT: {
                    productStatus.put(chatId, 0);
                    currentUser.setSellerState(NewSellerState.ADD_PRODUCT_NAME);
                    userService.editByChatId(chatId, currentUser);
                    execute(addProductName(chatId));
                }
                break;
                case ADD_PRODUCT_NAME: {
                    map.put(chatId, message.getText());
                    Product product = new Product();
                    List<Category> categoryList = categoryService.getList();
                    for (Category category : categoryList) {
                        if (category.getName().equals(categoryStatus.get(chatId))){
                            product.setCategoryId(category.getId());
                        }
                    }
                    product.setActive(true);
                    product.setSellerId(chatId);
                    product.setName(message.getText());
                    currentUser.setSellerState(NewSellerState.ADD_PRODUCT_PRICE);
                    userService.editByChatId(chatId, currentUser);
                    productService.add(product);
                    execute(addProductPrice(chatId));
                }
                break;
                case ADD_PRODUCT_PRICE: {
                    currentProduct.setPrice(Double.parseDouble(message.getText()));
                    currentUser.setSellerState(NewSellerState.ADD_PRODUCT_INFO);
                    userService.editByChatId(chatId, currentUser);
                    productService.editBySellerName(currentProduct);
                    execute(addProductInfo(chatId));
                }
                break;
                case ADD_PRODUCT_INFO: {
                    currentProduct.setProductInfo(message.getText());
                    currentUser.setSellerState(NewSellerState.ADD_PRODUCT_PHOTO);
                    userService.editByChatId(chatId, currentUser);
                    productService.editBySellerName(currentProduct);
                    execute(addProductPhoto(chatId));
                }
                break;
                case ADD_PRODUCT_PHOTO: {
                    currentProduct.setFileUrlPhoto(message.getPhoto().get(0).getFileId());
                    currentUser.setSellerState(NewSellerState.PRODUCT_CRUD);
                    userService.editByChatId(chatId, currentUser);
                    productService.editBySellerName(currentProduct);
                    execute(productCrudMenu(chatId));
                }
                break;
                case EDIT_PRODUCT: {
                    productStatus.put(chatId, 1);
                    currentUser.setSellerState(NewSellerState.EDIT_PRODUCT_NAME);
                    userService.editByChatId(chatId, currentUser);
                    execute(editProductName(chatId));
                }
                break;
                case EDIT_PRODUCT_NAME: {
                    map.put(chatId, message.getText());
                    currentUser.setSellerState(NewSellerState.EDIT_PRODUCT_PRICE);
                    userService.editByChatId(chatId, currentUser);
                    execute(editProductPrice(chatId));
                }
                break;
                case EDIT_PRODUCT_PRICE: {
                    currentProduct.setPrice(Double.parseDouble(message.getText()));
                    currentUser.setSellerState(NewSellerState.EDIT_PRODUCT_INFO);
                    userService.editByChatId(chatId, currentUser);
                    productService.editByProductName(map.get(chatId), currentProduct);
                    execute(editProductInfo(chatId));
                }
                break;
                case EDIT_PRODUCT_INFO: {
                    currentProduct.setProductInfo(message.getText());
                    currentUser.setSellerState(NewSellerState.EDIT_PRODUCT_PHOTO);
                    userService.editByChatId(chatId, currentUser);
                    productService.editByProductName(map.get(chatId), currentProduct);
                    execute(editProductPhoto(chatId));
                }
                break;
                case EDIT_PRODUCT_PHOTO: {
                    currentProduct.setFileUrlPhoto(message.getPhoto().get(0).getFileId());
                    currentUser.setSellerState(NewSellerState.EDIT_PRODUCT_ACTIVATION);
                    userService.editByChatId(chatId, currentUser);
                    productService.editByProductName(map.get(chatId), currentProduct);
                    execute(editProductActivation(chatId));
                }
                break;
                case EDIT_PRODUCT_ACTIVATION: {
                    currentProduct.setActive(Boolean.parseBoolean(message.getText()));
                    currentUser.setSellerState(NewSellerState.PRODUCT_CRUD);
                    userService.editByChatId(chatId, currentUser);
                    productService.editByProductName(map.get(chatId), currentProduct);
                    execute(productCrudMenu(chatId));
                }
                break;
                case DELETE_PRODUCT: {
                    currentUser.setSellerState(NewSellerState.DELETE_PRODUCT_NAME);
                    userService.editByChatId(chatId, currentUser);
                    execute(deleteProductName(chatId));
                }
                break;
                case DELETE_PRODUCT_NAME: {
                    map.put(chatId, message.getText());
                    currentUser.setSellerState(NewSellerState.DELETE_PRODUCT_ACTIVE);
                    userService.editByChatId(chatId, currentUser);
                    execute(deletedProductName(chatId));
                }
                break;
                case DELETE_PRODUCT_ACTIVE: {
                    currentProduct.setActive(false);
                    currentUser.setSellerState(NewSellerState.MAIN_MENU);
                    productService.editByProductName(map.get(chatId), currentProduct);
                    userService.editByChatId(chatId, currentUser);
                    execute(sellerMainMenu(chatId));
                }
                break;
                case EDIT_PROFILE: {
                    currentUser.setSellerState(NewSellerState.START_CONTACT_SHARED);
                    userService.editByChatId(chatId, currentUser);
                    execute(sharePhoneNumber(chatId));
                }
                break;
                case HISTORY: {
                    currentUser.setSellerState(NewSellerState.MAIN_MENU);
                    userService.editByChatId(chatId, currentUser);
//                        execute(productCrudMenu(chatId));
                }
                break;
                case MAIN_MENU: {
                    execute(sellerMainMenu(chatId));
                }
                break;
                default:
                    throw new IllegalStateException("Unexpected value: " + sellerState);
            }

        } else if (update.hasCallbackQuery()) {
            String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            String data = update.getCallbackQuery().getData();
            if (categoryProductStatus.get(chatId) == 0){
                categoryStatus.put(chatId, data);
                execute(productCrudMenu(chatId));
            }else{
                execute(sendProductFullInfo(chatId, data));
            }
        }
    }
}
