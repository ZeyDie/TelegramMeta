package com.zeydie.telegram.meta.api.metas;

import com.zeydie.telegram.meta.data.ChannelMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IChannelMeta {
    @NotNull
    List<ChannelMeta> getChannelsMetas();
}