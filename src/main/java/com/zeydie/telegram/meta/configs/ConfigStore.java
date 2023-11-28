package com.zeydie.telegram.meta.configs;

import com.zeydie.telegram.meta.configs.data.MetaSQLConfig;
import com.zeydie.telegram.meta.configs.data.TDLibConfig;
import com.zeydie.telegrambot.api.events.config.ConfigSubscribe;
import com.zeydie.telegrambot.api.events.subscribes.ConfigSubscribesRegister;
import lombok.Getter;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;

@ConfigSubscribesRegister
public class ConfigStore {
    @Getter
    @NonFinal
    @ConfigSubscribe(name = "telegram_meta_sql")
    public static @NotNull MetaSQLConfig metaSQLConfig = new MetaSQLConfig();
    @Getter
    @NonFinal
    @ConfigSubscribe(name = "telegram_meta_tdlib")
    public static @NotNull TDLibConfig tdLibConfig = new TDLibConfig();
}