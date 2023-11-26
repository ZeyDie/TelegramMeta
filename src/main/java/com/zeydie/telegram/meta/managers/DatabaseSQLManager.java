package com.zeydie.telegram.meta.managers;

import com.zaxxer.hikari.HikariDataSource;
import com.zeydie.telegram.meta.api.managers.IDatabaseManager;
import com.zeydie.telegram.meta.configs.MetaSQLConfig;
import com.zeydie.telegram.meta.data.ChannelMeta;
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

    private MetaSQLConfig.ChannelsMetaTable channelsMetaTable;
    private HikariDataSource hikariDataSource;

    @SneakyThrows
    public @NonNull Connection getConnection() {
        return this.hikariDataSource.getConnection();
    }

    @Override
    public @NonNull MetaSQLConfig.ChannelsMetaTable getChannelsMetaTable() {
        return this.channelsMetaTable;
    }

    @Override
    public void setup(@NonNull final MetaSQLConfig metaSqlConfig) {
        log.info("Setup configurations of sql...");

        this.channelsMetaTable = metaSqlConfig.getChannelsMetaTable();
        this.hikariDataSource = metaSqlConfig.getHikariDataSource();
    }

    @Override
    public void load() {
        log.info("Connecting to SQL...");

        this.channelMetaDatabaseSQL.load();
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