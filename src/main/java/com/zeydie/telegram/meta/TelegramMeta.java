package com.zeydie.telegram.meta;

import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.common.util.concurrent.Service;
import com.zeydie.tdlib.TDLib;
import com.zeydie.telegram.meta.api.managers.IChannelMetaManager;
import com.zeydie.telegram.meta.api.managers.IDatabaseManager;
import com.zeydie.telegram.meta.configs.MetaSQLConfig;
import com.zeydie.telegram.meta.configs.TDLibConfig;
import com.zeydie.telegram.meta.managers.ChannelMetaManager;
import com.zeydie.telegram.meta.managers.DatabaseSQLManager;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

@Log4j2
public final class TelegramMeta {
    @Getter
    private static TelegramMeta instance;

    @Getter
    @NonNull
    private final TDLib tdLib = new TDLib();

    @Setter
    @Getter
    @NonNull
    private TDLibConfig TDLibConfig = new TDLibConfig();
    @Setter
    @Getter
    @NonNull
    private MetaSQLConfig metaSqlConfig = new MetaSQLConfig();

    @Getter
    @NonNull
    private final IDatabaseManager databaseSQLManager = new DatabaseSQLManager();
    @Getter
    @NonNull
    private final IChannelMetaManager channelMetaManager = new ChannelMetaManager();

    private final @NotNull Service metricService = new AbstractScheduledService() {
        @Override
        protected void runOneIteration() {
            updateMeta();
        }

        @Override
        protected @NotNull Scheduler scheduler() {
            return Scheduler.newFixedRateSchedule(0, 1, TimeUnit.MINUTES);
        }
    };

    public TelegramMeta() {
        instance = this;
    }

    public void start() {
        this.tdLib.start();

        this.databaseSQLManager.setup(this.metaSqlConfig);
    }

    public void init() {
        this.metricService.startAsync();

        this.databaseSQLManager.load();
        this.channelMetaManager.load();

        this.updateMeta();
    }

    public void stop() {
        this.metricService.stopAsync();

        this.databaseSQLManager.close();
    }

    public void updateMeta() {
        this.channelMetaManager.getChannelsMetas()
                .forEach(
                        channelMeta -> {
                            val channelId = channelMeta.getChannelId();

                            @Nullable val chat = this.tdLib.getChat(channelId);

                            if (chat != null) {
                                @Nullable val chatStatisticsChannel = this.tdLib.getChatStatistics(channelId);

                                if (chatStatisticsChannel != null) {
                                    log.debug("Updating statistics channel {} ({})", chat.title, chat.id);

                                    channelMeta.copyOf(chatStatisticsChannel);

                                    log.debug("Statistics channel updated!");
                                } else log.debug("Channel haven't statistics {} ({})", chat.title, chat.id);

                                @Nullable val chatStatisticsSupergroup = this.tdLib.getChatStatisticsSupergroup(channelId);

                                if (chatStatisticsSupergroup != null) {
                                    log.debug("Updating statistics supergroup {} ({})", chat.title, chat.id);

                                    channelMeta.copyOf(chatStatisticsSupergroup);

                                    log.debug("Statistics channel updated!");
                                } else log.debug("Supergroup haven't statistics {} ({})", chat.title, chat.id);
                            } else log.debug("Can't load chat {}", channelId);
                        }
                );

        this.databaseSQLManager.save();
    }
}