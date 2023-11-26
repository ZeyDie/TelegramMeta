package com.zeydie.tdlib.handlers.auth;

import com.zeydie.telegram.meta.TelegramMeta;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;

@Log4j2
public final class AuthorizationStateWaitPhoneNumberResultHandler implements Client.ResultHandler, IResultHandler {
    @Override
    public int getConstructor() {
        return TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR;
    }

    @Override
    public void onResult(@NonNull final TdApi.Object object) {
        TelegramMeta.getInstance()
                .getTdLib()
                .getClient()
                .send(
                        new TdApi.SetAuthenticationPhoneNumber(
                                TelegramMeta.getInstance().getTDLibConfig().getPhoneNumber(),
                                null
                        ),
                        log::debug
                );
    }
}
