package ru.eva;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static final String BOT_TOKEN = "7057508771:AAF_9O1V-KbkxyWhZiEvxyagWPTVXMNrIRo";
    public static TelegramBot telegramBot = new TelegramBot(BOT_TOKEN);
    public static LogService logService = new LogService();
    public static Map<Long, TGUser> users = new HashMap<>();

    public static void main(String[] args) {
        telegramBot.setUpdatesListener(updates -> {
            updates.forEach(Main::newMessageFromUser);

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }); // Данный метод нужен для того чтобь установить слушатель обновлений setUpdatesListener, который обрабатывает каждое обновление, вызывая метод newMessageFromUser

    }

    public static void newMessageFromUser (Update update) {
        logService.log(update);

        Long userId = update.message().from().id();
        if (startMessage(userId)) return;
    } // Метод newMessageFromUser предназначен для записывания логов, извлечения идентификатора пользователя и отправки начального сообщения

    public static Boolean startMessage(Long userId){
        if(!users.containsKey(userId)) {
            users.put(userId, new TGUser(userId));

            SendMessage sendMessage = new SendMessage(userId, Texts.HELLO_MESSAGE);
            sendMessage.parseMode(ParseMode.Markdown); //чтобы тескт был жирным

            telegramBot.execute(sendMessage);
            return true;
        } return false;
    }  // Метод startMessage отправляет приветственное письмо, в данном примере при помощи if ставится условие: если пользователь новый - отправить письмо, если пользователь существует - ничего не отправлять.
}