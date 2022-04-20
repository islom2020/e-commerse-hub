package telegramBot.userBot;

import lombok.SneakyThrows;
import model.Category;
import model.Product;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import service.*;
import telegramBot.userBot.service.UserBotService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public interface UserInterface {

    String BOT_USERNAME = "My_Secret_Tester_Bot";
    String BOT_TOKEN = "5042642340:AAHkp3vmhU9LJrr1x4CerG0Fj5sGGoK2VNo";

}
