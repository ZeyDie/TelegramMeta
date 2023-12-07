package com.zeydie.telegram.meta;

import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.common.util.concurrent.Service;
import com.zeydie.tdlib.TDLib;
import com.zeydie.telegram.meta.api.managers.IChannelMetaManager;
import com.zeydie.telegram.meta.api.managers.IDatabaseManager;
import com.zeydie.telegram.meta.managers.ChannelMetaManager;
import com.zeydie.telegram.meta.managers.DatabaseSQLManager;
import com.zeydie.telegrambot.api.modules.interfaces.ISubcore;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

@Log4j2
public final class TelegramMeta implements ISubcore {
    @Getter
    private static TelegramMeta instance = new TelegramMeta();

    @Getter
    private final @NotNull TDLib tdLib = new TDLib();

    @Getter
    private final @NotNull IDatabaseManager databaseSQLManager = new DatabaseSQLManager();
    @Getter
    private final @NotNull IChannelMetaManager channelMetaManager = new ChannelMetaManager();

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

    @Override
    public @NotNull String getName() {
        return this.getClass().getName();
    }

    @Override
    public void launch(@Nullable final String[] strings) {
    }

    @Override
    public void preInit() {
        this.tdLib.preInit();
        this.databaseSQLManager.preInit();
        this.channelMetaManager.preInit();
    }

    @Override
    public void init() {
        this.tdLib.init();
        this.databaseSQLManager.init();
        this.channelMetaManager.init();
    }

    @Override
    public void postInit() {
        this.tdLib.postInit();
        this.databaseSQLManager.postInit();
        this.channelMetaManager.postInit();

        this.metricService.startAsync();

        this.updateMeta();
    }

    @Override
    public void stop() {
        this.tdLib.stop();

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