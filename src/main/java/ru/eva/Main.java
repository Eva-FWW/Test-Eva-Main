package ru.eva;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.HashSet;
import java.util.Set;

public class Main {
    public static final String BOT_TOKEN = "7057508771:AAF_9O1V-KbkxyWhZiEvxyagWPTVXMNrIRo";
    public static TelegramBot telegramBot = new TelegramBot(BOT_TOKEN);
    private static final Set<Long> newUsersChatIds = new HashSet<>();
    // Переменная для отслеживания идентификаторов чатов новых пользователей

    public static void main(String[] args) {
        // Устанавливаем обработчик обновлений
        telegramBot.setUpdatesListener(updates -> {
            // Проходимся по каждому обновлению
            for (Update update : updates) {
                // Получаем сообщение из обновления
                Message message = update.message();
                // Проверяем, является ли это первым сообщением нового пользователя
                if (message != null && isNewUser(message)) {
                    // Отправляем приветственное сообщение
                    sendWelcomeMessage(telegramBot, message);
                }
            }
            // Возвращаемое значение (CONFIRMED_UPDATES_ALL) указывает, что все обновления успешно обработаны
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    // Метод для проверки, является ли пользователь новым
    public static boolean isNewUser(Message message) {
        // Получаем идентификатор чата
        Long chatId = message.chat().id();
        // Если идентификатор чата уже есть в списке чатов новых пользователей, значит это не первое обращение пользователя
        return !newUsersChatIds.contains(chatId);
    }

    // Метод для отправки приветственного сообщения
    public static void sendWelcomeMessage(TelegramBot telegramBot, Message message) {
        // Получаем идентификатор чата
        Long chatId = message.chat().id();

        // Отправляем приветственное сообщение
        telegramBot.execute(new SendMessage(chatId, "Привет!"));

        // Добавляем идентификатор чата в список чатов новых пользователей
        newUsersChatIds.add(chatId);
    }
}