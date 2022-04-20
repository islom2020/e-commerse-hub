package telegramBot.admin;

public interface AdminInterface {
    String ADMIN_USERNAME = "amazon_team5_admin_bot";
    String ADMIN_TOKEN = "5096213228:AAHHwGV1D33XKJM4gSnj3_MZPnAOXRbsLMU";
    String ADMIN_PASSWORD = "root";

    //admin hasMessage
    String ADMIN_USER_LIST = "user list";
    String ADMIN_SELLER_LIST = "seller list";
    String ADMIN_PRODUCT_LIST = "product list";
    String ADMIN_NOTIFICATIONS = "notifications";
    String ADMIN_HISTORY = "all histories";
    String ADMIN_BALANCE = "system balance";
    String ADMIN_CHECK = "/admin";
    String ADMIN_START = "/start";
    String ADMIN_SELLER_BLOCK = "block seller";
    String ADMIN_CATEGORY = "category";

    // admin query method names
    String ADMIN_ADD_CATEGORY = "adminAddCategory";
    String ADMIN_EDIT_CATEGORY = "adminEditCategory";
    String ADMIN_DEACTIVATE_CATEGORY = "adminDeactivateCategory";
    String ADMIN_ACTIVATE_CATEGORY = "adminActivateCategory";
    String ADMIN_SEND_CATEGORY_INFO_FILE = "sendCategoryFullInfoFile";
    String ADMIN_ACCEPT_SELLER_REQUEST = "accept";
    String ADMIN_REJECT_SELLER_REQUEST = "reject";
    String SELLER_REQUEST = "seller request";
    String ADMIN_BACK = "back";
    String NONE = "none";
}
