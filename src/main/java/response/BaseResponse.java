package response;

public interface BaseResponse {
    String SUCCESS = "========== COMPLETED_SUCCESSFULLY ==========";
    String ERROR_ID_NOT_FOUND = "========== ID NOT FOUND ==========";


    String ERROR_USER_ALREADY_EXIST = "\n========== USER ALREADY EXIST ==========\n";
    String ERROR_USER_NOT_FOUND = "\n========== USER NOT FOUND ==========\n";
    String ERROR_WRONG_LOGIN = "\n========== WRONG USERNAME OR PASSWORD ==========\n";

    String USER_LIST = "\n========== USERS ==========\n";
    String SELLER_LIST = "\n========== SELLERS ==========\n";


    String ERROR_PRODUCT_ALREADY_EXIST = "\n========== PRODUCT ALREADY EXIST ==========\n";
    String ERROR_PRODUCT_NOT_FOUND = "\n========== PRODUCT NOT FOUND ==========\n";
    String ERROR_PRODUCT_AMOUNT_NOT_ENOUGH = "\n========== PRODUCT AMOUNT IS NOT ENOUGH ==========\n";


    String PRODUCT_LIST = "\n========== PRODUCTS ==========\n";


    String ERROR_CATEGORY_ALREADY_EXIST = "\n========== CATEGORY ALREADY EXIST ==========\n";
    String ERROR_CATEGORY_NOT_FOUND = "\n========== CATEGORY NOT FOUND ==========\n";

    String WRONG_INPUT = "\n========== WRONG INPUT ==========\n";
    String SELLER_REQUEST = "\n========== SELLER REQUESTS ==========\n";

    String ERROR_NOTIFICATION_NOT_FOUND = "\n========== NOTIFICATION NOT FOUND ==========\n";

}
