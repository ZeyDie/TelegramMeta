package com.zeydie.telegram.meta.api;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

public interface IDatabase {
    @NotNull Connection getConnection();

    void load();

    void save();
}