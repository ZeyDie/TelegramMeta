package com.zeydie.tdlib.handlers.auth;

import lombok.NonNull;
import org.drinkless.tdlib.TdApi;

public interface IResultHandler {
    int getConstructor();

    void onResult(@NonNull TdApi.Object object);
}
