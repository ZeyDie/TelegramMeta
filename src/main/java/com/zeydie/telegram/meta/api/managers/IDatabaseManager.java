package com.zeydie.telegram.meta.api.managers;

import com.zeydie.telegram.meta.api.IDatabase;
import com.zeydie.telegram.meta.api.metas.IChannelMeta;

public interface IDatabaseManager extends IDatabase, IChannelMeta {
    void close();
}