package com.zeydie.tdlib;

import com.zeydie.tdlib.handlers.UpdateAuthorizationStateResultHandler;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
public final class TDLib {
    private static Path tdlibPath = Paths.get("tdlib");

    static {
        log.debug(System.getProperty("os.name"));
        log.debug(System.getProperty("os.arch"));

        @NonNull val os = System.getProperty("os.name");

        if (os != null) {
            if (os.toLowerCase(Locale.ROOT).startsWith("windows")) {
                tdlibPath = tdlibPath.resolve("windows");

                extractAndLoadDll(tdlibPath.resolve("libcrypto-1_1-x64"));
                extractAndLoadDll(tdlibPath.resolve("libssl-1_1-x64"));
                extractAndLoadDll(tdlibPath.resolve("zlib1"));
            } else if (os.toLowerCase(Locale.ROOT).startsWith("linux"))
                tdlibPath = tdlibPath.resolve("linux");
        }

        extractAndLoadDll(tdlibPath.resolve("tdjni"));
    }

    @SneakyThrows
    private static void extractAndLoadDll(@NonNull final Path path) {
        @NonNull val name = path.toFile().getPath();
        @Cleanup val inputStream = TDLib.class.getResourceAsStream(name);

        if (inputStream != null) {
            Files.copy(inputStream, path);

            log.debug("Extracted {}", name);
        }

        System.loadLibrary(path.toFile().getName());
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
        @NonNull val atomicFinished = new AtomicBoolean(false);

        this.client.send(
                new TdApi.GetChat(id),
                object -> {
                    if (object instanceof @NonNull TdApi.Chat chat) {
                        log.debug("GetChat {} {}", chat.id, chat.title);

                        atomicValue.set(chat);
                    } else log.error(object);

                    atomicFinished.set(true);
                }
        );

        do {
        } while (!atomicFinished.get());

        return atomicValue.get();
    }

    public @Nullable TdApi.ChatStatisticsChannel getChatStatistics(final long id) {
        @NonNull val atomicValue = new AtomicReference<TdApi.ChatStatisticsChannel>();
        @NonNull val atomicFinished = new AtomicBoolean(false);

        this.client.send(
                new TdApi.GetChatStatistics(id, false),
                object -> {
                    if (object instanceof @NonNull TdApi.ChatStatisticsChannel chatStatisticsChannel) {
                        log.debug("GetChatStatistics {}", id);

                        atomicValue.set(chatStatisticsChannel);
                    }

                    atomicFinished.set(true);
                }
        );

        do {
        } while (!atomicFinished.get());

        return atomicValue.get();
    }

    public @Nullable TdApi.ChatStatisticsSupergroup getChatStatisticsSupergroup(final long id) {
        @NonNull val atomicValue = new AtomicReference<TdApi.ChatStatisticsSupergroup>();
        @NonNull val atomicFinished = new AtomicBoolean(false);

        this.client.send(
                new TdApi.GetChatStatistics(id, false),
                object -> {
                    if (object instanceof @NonNull TdApi.ChatStatisticsSupergroup chatStatisticsSupergroup) {
                        log.debug("GetChatStatisticsSupergroup {}", id);

                        atomicValue.set(chatStatisticsSupergroup);
                    }

                    atomicFinished.set(true);
                }
        );

        do {
        } while (!atomicFinished.get());

        return atomicValue.get();
    }
}