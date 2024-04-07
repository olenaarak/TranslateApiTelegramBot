package com.example.translateapitelegrambot.service;

import com.example.translateapitelegrambot.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    BotConfig config;

    public TelegramBot(BotConfig botConfig){
        this.config = botConfig;
        List<BotCommand> listOfComands = new ArrayList<>();
        listOfComands.add(new BotCommand("/english", "en"));
        listOfComands.add(new BotCommand("/german", "de"));
        listOfComands.add(new BotCommand("/spanish", "es"));
        listOfComands.add(new BotCommand("/polish", "pl"));
        try{
            this.execute(new SetMyCommands(listOfComands,
                    new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            System.out.println("Error setting list of commands");
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    private String selectedLanguage = null;

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText) {
                case "/start":
                    String name = update.getMessage().getChat().getFirstName();
                    String answer = "Привіт, " + name + "! Виберіть одну з запропонованих мов, якою хочете перекласти ваш текст";
                    sendMessage(chatId, answer);
                    break;
                case "/english":
                    selectedLanguage = "en";
                    sendMessage(chatId, "Введіть текст для перекладу:");
                    break;
                case "/german":
                    selectedLanguage = "de";
                    sendMessage(chatId, "Введіть текст для перекладу:");
                    break;
                case "/spanish":
                    selectedLanguage = "es";
                    sendMessage(chatId, "Введіть текст для перекладу:");
                    break;
                case "/polish":
                    selectedLanguage = "pl";
                    sendMessage(chatId, "Введіть текст для перекладу:");
                    break;
                default:
                    if (selectedLanguage == null) {
                    sendMessage(chatId, "Спочатку виберіть мову для перекладу!");
                    } else {
                        String textToTranslate = update.getMessage().getText();
                        translateAndSendTelegramText(chatId, selectedLanguage, textToTranslate);
                    }
            }
        }
    }

    private void translateAndSendTelegramText(long chatId, String language, String text) {
        String translation = TranslateText.translateText(language, text);
        sendMessage(chatId, translation);
    }

    private void sendMessage(long chatId, String text){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println("Error sending message in chat with chatId " + chatId);
            throw new RuntimeException(e);
        }
    }
}
