package com.zeydie.telegram.meta.api.metas;

import com.zeydie.telegram.meta.data.SupergroupMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IChannelMeta {
    @NotNull
    List<SupergroupMeta> getChannelsMetas();
}