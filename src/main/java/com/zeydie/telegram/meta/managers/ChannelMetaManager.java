package com.zeydie.telegram.meta.managers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zeydie.telegram.meta.TelegramMeta;
import com.zeydie.telegram.meta.api.managers.IChannelMetaManager;
import com.zeydie.telegram.meta.data.ChannelMeta;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class ChannelMetaManager implements IChannelMetaManager {
    private final @NotNull Cache<Long, ChannelMeta> channelMetaCache = CacheBuilder
            .newBuilder()
            .build();

    @Override
    public void load() {
        TelegramMeta.getInstance()
                .getDatabaseSQLManager()
                .getChannelsMetas()
                .forEach(this::putOrUpdate);
    }

    public boolean isExist(final long channelId) {
        return this.channelMetaCache.asMap().containsKey(channelId);
    }

    public @Nullable ChannelMeta getChannelMeta(final long channelId) {
        return this.channelMetaCache.getIfPresent(channelId);
    }

    public @NonNull ChannelMeta getOrCreate(final long channelId) {
        @Nullable var channelMeta = this.getChannelMeta(channelId);

        if (channelMeta == null) {
            channelMeta = new ChannelMeta();

            channelMeta.setChannelId(channelId);

            this.channelMetaCache.put(channelId, channelMeta);
        }

        return channelMeta;
    }

    public void putOrUpdate(@NonNull final ChannelMeta channelMeta) {
        this.getOrCreate(channelMeta.getChannelId()).copyOf(channelMeta);
    }

    @Override
    public @NotNull List<ChannelMeta> getChannelsMetas() {
        return this.channelMetaCache.asMap().values().stream().toList();
    }
}