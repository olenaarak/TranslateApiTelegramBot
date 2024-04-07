package com.example.translateapitelegrambot.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class BotConfig {
    String botName = "GoogleTranslateApiBot";
    String token = "";
}
