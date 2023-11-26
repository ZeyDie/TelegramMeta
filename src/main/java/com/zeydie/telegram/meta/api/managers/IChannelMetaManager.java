package com.zeydie.telegram.meta.api.managers;

import com.zeydie.telegram.meta.data.ChannelMeta;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IChannelMetaManager extends IManager {
    boolean isExist(final long channelId);

    @Nullable ChannelMeta getChannelMeta(final long channelId);

    @NonNull ChannelMeta getOrCreate(final long channelId);

    void putOrUpdate(@NonNull final ChannelMeta channelMeta);

    @NotNull
    List<ChannelMeta> getChannelsMetas();
}