package com.zeydie.tdlib.handlers.auth;

import com.zeydie.telegram.meta.TelegramMeta;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;

import java.util.Scanner;

@Log4j2
public final class AuthorizationStateWaitCodeResultHandler implements Client.ResultHandler, IResultHandler {
    @Override
    public int getConstructor() {
        return TdApi.AuthorizationStateWaitCode.CONSTRUCTOR;
    }

    @Override
    public void onResult(@NonNull final TdApi.Object object) {
        @Cleanup Scanner scanner = new Scanner(System.in);

        log.info("Write code: ");

        @NonNull val code = scanner.nextLine();

        TelegramMeta.getInstance()
                .getTdLib()
                .getClient()
                .send(new TdApi.CheckAuthenticationCode(code), log::debug);
    }
}
