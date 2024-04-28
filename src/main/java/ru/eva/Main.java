package ru.eva;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.request.SendMediaGroup;
import com.pengrad.telegrambot.model.request.InputMediaPhoto;

import java.util.*;
import java.io.File;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

        sendPhotoAllUsers("C:\\Image\\FirstPhoto.jpg");

        sendManyPhotoAllUsers();

        //Timer start
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        ZonedDateTime targetTime = ZonedDateTime.of(
                LocalDateTime.of(2024, 4, 28, 17, 8, 0),
                ZoneId.of("Europe/Moscow")
        );

        long delay = java.time.Duration.between(ZonedDateTime.now(),targetTime).toMillis();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                sendMessageAllUsers("Это мой первый пост");
            }
        };

        scheduler.schedule(task,delay,TimeUnit.MILLISECONDS);

        scheduler.shutdown();
        //Timer end

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

    public static void sendMessageAllUsers(String messageText) {
        for (Long chatId : newUsersChatIds) {
            telegramBot.execute((new SendMessage(chatId, messageText)));
        }
    }

    public static void sendPhotoAllUsers(String imagePath) {
        for (Long chatId : newUsersChatIds) {
            telegramBot.execute(new SendPhoto(chatId, imagePath));
        }
    }

    public static void sendManyPhotoAllUsers() {
        List<InputMediaPhoto> photoList = new ArrayList<>();
        photoList.add(new InputMediaPhoto(new File("C:\\Image\\FirstPhoto.jpg")));
        photoList.add(new InputMediaPhoto(new File("C:\\Image\\SecondPhoto.jpg")));
        InputMediaPhoto[] photoArray = photoList.toArray(new InputMediaPhoto[0]);

        for (Long chatId : newUsersChatIds) {
            telegramBot.execute(new SendMediaGroup(chatId, photoArray));
        }
    }
}