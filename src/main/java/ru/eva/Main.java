package ru.eva;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.HashSet;
import java.util.Set;
import java.util.List;

public class Main {
    public static final String BOT_TOKEN = "7057508771:AAF_9O1V-KbkxyWhZiEvxyagWPTVXMNrIRo";
    public static TelegramBot telegramBot = new TelegramBot(BOT_TOKEN);
    private static final Set<Long> newUsersChatIds = new HashSet<>();

    public static void main(String[] args) {
        telegramBot.setUpdatesListener(new UpdatesListener() {
            @Override
            public int process(List<Update> updates) {
                for (Update update : updates) {
                    Message message = update.message();
                    if (message != null && isNewUser(message)) {
                        sendWelcomeMessage(telegramBot, message);
                    }
                }
                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            }
        });
    }

    public static boolean isNewUser(Message message) {
        Long chatId = message.chat().id();
        return !newUsersChatIds.contains(chatId);
    }

    public static void sendWelcomeMessage(TelegramBot telegramBot, Message message) {
        Long chatId = message.chat().id();
        telegramBot.execute(new SendMessage(chatId, "Привет!"));
        newUsersChatIds.add(chatId);
    }
}