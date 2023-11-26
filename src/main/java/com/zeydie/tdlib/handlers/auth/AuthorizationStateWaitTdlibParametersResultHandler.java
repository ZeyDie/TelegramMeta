package com.zeydie.tdlib.handlers.auth;

import com.zeydie.tdlib.handlers.connection.ConnectionStateReadyResultHandler;
import com.zeydie.telegram.meta.TelegramMeta;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;

@Log4j2
public final class AuthorizationStateWaitTdlibParametersResultHandler implements Client.ResultHandler, IResultHandler {

    @Override
    public int getConstructor() {
        return TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR;
    }

    @Override
    public void onResult(@NonNull final TdApi.Object object) {
        log.debug("{}", object);

        @NonNull val instance = TelegramMeta.getInstance();
        @NonNull val settingsConfig = instance.getTDLibConfig();
        @NonNull val parameters = new TdApi.SetTdlibParameters();

        parameters.apiId = settingsConfig.getApiId();
        parameters.apiHash = settingsConfig.getApiHash();
        parameters.useTestDc = settingsConfig.isUseTestServer();

        parameters.databaseDirectory = "tdlib_database";
        parameters.useMessageDatabase = true;
        parameters.useSecretChats = true;
        parameters.systemLanguageCode = "en";
        parameters.deviceModel = "Desktop";
        parameters.applicationVersion = "1.0";
        parameters.enableStorageOptimizer = true;

        instance.getTdLib()
                .getClient()
                .send(parameters, new ConnectionStateReadyResultHandler());
    }
}