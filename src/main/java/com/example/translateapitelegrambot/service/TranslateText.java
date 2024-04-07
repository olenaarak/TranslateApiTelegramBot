package com.example.translateapitelegrambot.service;

import com.google.cloud.translate.v3.LocationName;
import com.google.cloud.translate.v3.TranslateTextRequest;
import com.google.cloud.translate.v3.TranslateTextResponse;
import com.google.cloud.translate.v3.Translation;
import com.google.cloud.translate.v3.TranslationServiceClient;
import java.io.IOException;

public class TranslateText {

    public static String translateText(String targetLanguage, String text) {
        String projectId = "";
        return translateText(projectId, targetLanguage, text);
    }

    public static String translateText(String projectId, String targetLanguage, String text) {
        try (TranslationServiceClient client = TranslationServiceClient.create()) {
            LocationName parent = LocationName.of(projectId, "global");

            TranslateTextRequest request =
                    TranslateTextRequest.newBuilder()
                            .setParent(parent.toString())
                            .setMimeType("text/plain")
                            .setTargetLanguageCode(targetLanguage)
                            .addContents(text)
                            .build();

            TranslateTextResponse response = client.translateText(request);
            StringBuilder translationsStringBuilder = new StringBuilder();

            for (Translation translation : response.getTranslationsList()) {
                translationsStringBuilder.append(translation.getTranslatedText()).append("\n");
            }
            String allTranslations = translationsStringBuilder.toString();
            return allTranslations;
        } catch (IOException e) {
            System.out.println("Error translating text");
            throw new RuntimeException(e);
        }
    }
}