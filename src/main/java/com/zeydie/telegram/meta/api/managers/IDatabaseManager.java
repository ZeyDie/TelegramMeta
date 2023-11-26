package com.zeydie.telegram.meta.api.managers;

import com.zeydie.telegram.meta.api.IDatabase;
import com.zeydie.telegram.meta.api.metas.IChannelMeta;
import com.zeydie.telegram.meta.configs.MetaSQLConfig;
import lombok.NonNull;

public interface IDatabaseManager extends IDatabase, IChannelMeta {
    @NonNull MetaSQLConfig.ChannelsMetaTable getChannelsMetaTable();

    void setup(@NonNull final MetaSQLConfig metaSqlConfig);

    void close();
}