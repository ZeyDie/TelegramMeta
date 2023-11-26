package com.zeydie.telegram.meta.api.managers;

import com.zeydie.telegram.meta.data.ChannelMeta;
import com.zeydie.telegram.meta.data.SupergroupMeta;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IChannelMetaManager extends IManager {
    boolean isExist(final long channelId);

    @Nullable SupergroupMeta getChannelMeta(final long channelId);

    @NonNull SupergroupMeta getOrCreate(final long channelId);

    void putOrUpdate(@NonNull final SupergroupMeta channelMeta);

    @NotNull
    List<SupergroupMeta> getChannelsMetas();
}