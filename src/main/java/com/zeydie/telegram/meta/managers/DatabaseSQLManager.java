package com.zeydie.telegram.meta.managers;

import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.common.util.concurrent.Service;
import com.zaxxer.hikari.HikariDataSource;
import com.zeydie.telegram.meta.api.managers.IDatabaseManager;
import com.zeydie.telegram.meta.configs.SQLConfig;
import com.zeydie.telegram.meta.data.ChannelMeta;
import com.zeydie.telegram.meta.databases.sql.ChannelMetaDatabaseSQL;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Log4j2
public final class DatabaseSQLManager implements IDatabaseManager {
    @NotNull
    private final ChannelMetaDatabaseSQL channelMetaDatabaseSQL = new ChannelMetaDatabaseSQL();

    private SQLConfig.ChannelsMetaTable channelsMetaTable;
    private HikariDataSource hikariDataSource;

    private Service saveService;

    @SneakyThrows
    public @NonNull Connection getConnection() {
        return this.hikariDataSource.getConnection();
    }

    @Override
    public @NonNull SQLConfig.ChannelsMetaTable getChannelsMetaTable() {
        return this.channelsMetaTable;
    }

    @Override
    public void setup(@NonNull final SQLConfig sqlConfig) {
        log.debug("Setup configurations of sql...");

        this.channelsMetaTable = sqlConfig.getChannelsMetaTable();
        this.hikariDataSource = sqlConfig.getHikariDataSource();
        this.saveService = new AbstractScheduledService() {
            @Override
            protected void runOneIteration() {
                save();
            }

            @Override
            protected @NotNull Scheduler scheduler() {
                return Scheduler.newFixedRateSchedule(0, sqlConfig.getAutosaveMinutes(), TimeUnit.MINUTES);
            }
        };
    }

    @Override
    public void load() {
        log.debug("Connecting to SQL...");

        this.channelMetaDatabaseSQL.load();

        this.saveService.startAsync();
    }

    @Override
    public void save() {
        log.debug("Saving SQL...");

    }

    @Override
    public void close() {
        this.save();

        log.debug("Closing SQL");

        this.hikariDataSource.close();
    }

    @Override
    public @NotNull List<ChannelMeta> getChannelsMetas() {
        return this.channelMetaDatabaseSQL.getChannelsMetas();
    }
}