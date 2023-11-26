package com.zeydie.tdlib;

import com.zeydie.tdlib.handlers.UpdateAuthorizationStateResultHandler;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
public final class TDLib {
    private static final String libcryptoDll = "tdlib/windows/libcrypto-1_1-x64";
    private static final String libsslDll = "tdlib/windows/libssl-1_1-x64";
    private static final String zlibDll = "tdlib/windows/zlib1";
    private static final String tdjniDll = "tdlib/tdjni";

    static {
        log.debug(System.getProperty("os.name"));
        log.debug(System.getProperty("os.arch"));

        @NonNull val os = System.getProperty("os.name");

        if (os != null && os.toLowerCase(Locale.ROOT).startsWith("windows")) {
            extractAndLoadDll(libcryptoDll);
            extractAndLoadDll(libsslDll);
            extractAndLoadDll(zlibDll);
        }

        extractAndLoadDll(tdjniDll);
    }

    @SneakyThrows
    private static void extractAndLoadDll(@NonNull final String name) {
        @Cleanup val inputStream = TDLib.class.getResourceAsStream(name);

        if (inputStream != null) {
            Files.copy(inputStream, Paths.get(name));

            log.debug("Extracted {}", name);
        }

        System.loadLibrary(name);
    }

    @Getter
    private Client client;
    @Setter
    @Getter
    private boolean started;

    @SneakyThrows
    public void start() {
        Client.setLogMessageHandler(0, (verbosityLevel, message) -> log.debug(message));

        Client.execute(new TdApi.SetLogVerbosityLevel(0));
        Client.execute(new TdApi.SetLogStream(new TdApi.LogStreamFile("tdlib.log", 1 << 27, false)));

        this.client = Client.create(new UpdateAuthorizationStateResultHandler(), e -> log.error(e.getMessage(), e), e -> log.error(e.getMessage(), e));

        this.loadChats();
    }

    public void loadChats() {
        this.client.send(
                new TdApi.LoadChats(
                        new TdApi.ChatListMain(),
                        1000
                ),
                log::debug
        );
    }

    public @Nullable TdApi.Chat getChat(final long id) {
        @NonNull val atomicValue = new AtomicReference<TdApi.Chat>();

        this.client.send(
                new TdApi.GetChat(id),
                object -> {
                    if (object instanceof @NonNull TdApi.Chat chat) {
                        log.debug("GetChat {} {}", chat.id, chat.title);

                        atomicValue.set(chat);
                    } else log.error(object);
                }
        );

        return atomicValue.get();
    }

    public @Nullable TdApi.ChatStatisticsChannel getChatStatistics(final long id) {
        @NonNull val atomicValue = new AtomicReference<TdApi.ChatStatisticsChannel>();

        this.client.send(
                new TdApi.GetChatStatistics(id, true),
                object -> {
                    if (object instanceof @NonNull TdApi.ChatStatisticsChannel chatStatisticsChannel) {
                        log.debug("GetChatStatistics {}", id);

                        atomicValue.set(chatStatisticsChannel);
                    } else log.error(object);
                }
        );

        return atomicValue.get();
    }

    public @Nullable TdApi.ChatStatisticsSupergroup getChatStatisticsSupergroup(final long id) {
        @NonNull val atomicValue = new AtomicReference<TdApi.ChatStatisticsSupergroup>();

        this.client.send(
                new TdApi.GetChatStatistics(id, true),
                object -> {
                    if (object instanceof @NonNull TdApi.ChatStatisticsSupergroup chatStatisticsSupergroup) {
                        log.debug("GetChatStatisticsSupergroup {}", id);

                        atomicValue.set(chatStatisticsSupergroup);
                    } else log.error(object);
                }
        );

        return atomicValue.get();
    }
}
