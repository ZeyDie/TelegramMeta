package com.zeydie.telegram.meta;

import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.common.util.concurrent.Service;
import com.zeydie.tdlib.TDLib;
import com.zeydie.telegram.meta.api.managers.IChannelMetaManager;
import com.zeydie.telegram.meta.api.managers.IDatabaseManager;
import com.zeydie.telegram.meta.configs.SQLConfig;
import com.zeydie.telegram.meta.configs.TDLibConfig;
import com.zeydie.telegram.meta.data.SupergroupMeta;
import com.zeydie.telegram.meta.managers.ChannelMetaManager;
import com.zeydie.telegram.meta.managers.DatabaseSQLManager;
import lombok.Getter;
import lombok.NonNull;
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

    @Getter
    @NonNull
    private final TDLibConfig TDLibConfig = new TDLibConfig();
    @Getter
    @NonNull
    private final SQLConfig sqlConfig = new SQLConfig();

    @Getter
    @NonNull
    private final IDatabaseManager databaseSQLManager = new DatabaseSQLManager();
    @Getter
    @NonNull
    private final IChannelMetaManager channelMetaManager = new ChannelMetaManager();

    private final @NotNull Service metricService = new AbstractScheduledService() {
        @Override
        protected void runOneIteration() {
            channelMetaManager.getChannelsMetas()
                    .forEach(
                            channelMeta -> {
                                val channelId = channelMeta.getChannelId();

                                @Nullable val chat = tdLib.getChat(channelId);

                                if (chat != null) {
                                    @Nullable val chatStatisticsChannel = tdLib.getChatStatistics(channelId);

                                    if (chatStatisticsChannel != null) {
                                        log.debug("Updating statistics channel {} ({})", chat.title, chat.id);

                                        channelMeta.copyOf(chatStatisticsChannel);
                                    }

                                    @Nullable val chatStatisticsSupergroup = tdLib.getChatStatisticsSupergroup(channelId);

                                    if (chatStatisticsSupergroup != null && channelMeta instanceof @NonNull SupergroupMeta supergroupMeta) {
                                        log.debug("Updating statistics supergroup {} ({})", chat.title, chat.id);

                                        supergroupMeta.copyOf(chatStatisticsSupergroup);
                                    }
                                }
                            }
                    );
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

        this.databaseSQLManager.setup(this.sqlConfig);
    }

    public void init() {
        this.databaseSQLManager.load();
        this.channelMetaManager.load();

        this.metricService.startAsync();
    }

    public void stop() {
        this.metricService.stopAsync();

        this.databaseSQLManager.close();
    }
}