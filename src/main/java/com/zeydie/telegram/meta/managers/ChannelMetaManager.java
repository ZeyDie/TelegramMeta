package com.zeydie.telegram.meta.managers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zeydie.telegram.meta.TelegramMeta;
import com.zeydie.telegram.meta.api.managers.IChannelMetaManager;
import com.zeydie.telegram.meta.data.SupergroupMeta;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Log4j2
public final class ChannelMetaManager implements IChannelMetaManager {
    private final @NotNull Cache<Long, SupergroupMeta> channelMetaCache = CacheBuilder
            .newBuilder()
            .build();

    @Override
    public void preInit() {
    }

    @Override
    public void init() {
        log.info("Loading...");

        TelegramMeta.getInstance()
                .getDatabaseSQLManager()
                .getChannelsMetas()
                .forEach(this::putOrUpdate);

        log.info("Successfully!");
    }

    @Override
    public void postInit() {
    }

    @Override
    public boolean isExist(final long channelId) {
        return this.channelMetaCache.asMap().containsKey(channelId);
    }

    @Override
    public @Nullable SupergroupMeta getChannelMeta(final long channelId) {
        return this.channelMetaCache.getIfPresent(channelId);
    }

    @Override
    public @NonNull SupergroupMeta getOrCreate(final long channelId) {
        @Nullable var channelMeta = this.getChannelMeta(channelId);

        if (channelMeta == null) {
            channelMeta = new SupergroupMeta();

            channelMeta.setChannelId(channelId);

            this.channelMetaCache.put(channelId, channelMeta);
        }

        return channelMeta;
    }

    @Override
    public void putOrUpdate(@NonNull final SupergroupMeta channelMeta) {
        this.getOrCreate(channelMeta.getChannelId()).copyOf(channelMeta);
    }

    @Override
    public @NotNull List<SupergroupMeta> getChannelsMetas() {
        return this.channelMetaCache.asMap().values().stream().toList();
    }
}