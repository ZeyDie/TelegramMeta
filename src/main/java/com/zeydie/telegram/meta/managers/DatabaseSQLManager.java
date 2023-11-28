package com.zeydie.telegram.meta.managers;

import com.zaxxer.hikari.HikariDataSource;
import com.zeydie.telegram.meta.api.managers.IDatabaseManager;
import com.zeydie.telegram.meta.configs.ConfigStore;
import com.zeydie.telegram.meta.data.SupergroupMeta;
import com.zeydie.telegram.meta.databases.sql.ChannelMetaDatabaseSQL;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.List;

@Log4j2
public final class DatabaseSQLManager implements IDatabaseManager {
    @NotNull
    private final ChannelMetaDatabaseSQL channelMetaDatabaseSQL = new ChannelMetaDatabaseSQL();

    private HikariDataSource hikariDataSource;

    @Override
    @SneakyThrows
    public @NonNull Connection getConnection() {
        return this.hikariDataSource.getConnection();
    }

    @Override
    public void preInit() {
        log.info("Setup configurations of sql...");

        this.hikariDataSource = ConfigStore.getMetaSQLConfig().getHikariDataSource();

        this.channelMetaDatabaseSQL.preInit();
    }

    @Override
    public void init() {
        this.channelMetaDatabaseSQL.init();
    }

    @Override
    public void postInit() {
        this.channelMetaDatabaseSQL.postInit();
    }

    @Override
    public void save() {
        log.info("Saving SQL...");

        this.channelMetaDatabaseSQL.save();

        log.info("SQL successfully saved!");
    }

    @Override
    public void close() {
        log.info("Closing SQL");

        this.save();

        this.hikariDataSource.close();
    }

    @Override
    public @NotNull List<SupergroupMeta> getChannelsMetas() {
        return this.channelMetaDatabaseSQL.getChannelsMetas();
    }
}