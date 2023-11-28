package com.zeydie.telegram.meta.api;

import com.zeydie.telegrambot.api.modules.interfaces.IInitialize;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

public interface IDatabase extends IInitialize {
    @NotNull Connection getConnection();

    void save();
}