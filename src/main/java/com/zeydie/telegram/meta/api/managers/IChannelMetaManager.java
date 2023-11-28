package com.zeydie.telegram.meta.api.managers;

import com.zeydie.telegram.meta.data.SupergroupMeta;
import com.zeydie.telegrambot.api.modules.interfaces.IInitialize;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IChannelMetaManager extends IInitialize {
    boolean isExist(final long channelId);

    @Nullable SupergroupMeta getChannelMeta(final long channelId);

    @NotNull SupergroupMeta getOrCreate(final long channelId);

    void putOrUpdate(@NonNull final SupergroupMeta channelMeta);

    @NotNull
    List<SupergroupMeta> getChannelsMetas();
}