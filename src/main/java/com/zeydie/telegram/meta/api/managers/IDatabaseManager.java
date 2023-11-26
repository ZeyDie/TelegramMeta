package com.zeydie.telegram.meta.api.managers;

import com.zeydie.telegram.meta.api.IDatabase;
import com.zeydie.telegram.meta.api.metas.IChannelMeta;
import com.zeydie.telegram.meta.configs.SQLConfig;
import lombok.NonNull;

public interface IDatabaseManager extends IDatabase, IChannelMeta {
    @NonNull SQLConfig.ChannelsMetaTable getChannelsMetaTable();

    void setup(@NonNull final SQLConfig sqlConfig);

    void close();
}