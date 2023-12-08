package com.zeydie.tdlib;

import com.zeydie.tdlib.handlers.UpdateAuthorizationStateResultHandler;
import com.zeydie.telegrambot.api.modules.interfaces.IInitialize;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
public final class TDLib implements IInitialize {
    @Getter
    private Client client;
    @Setter
    @Getter
    private boolean started;
    @Setter
    @Getter
    private int verbosityLevelConsole = 2;
    @Setter
    @Getter
    private int verbosityLevelFile = 3;

    private static @NotNull Path TDLIB = Paths.get("tdlib");
    private static @NotNull Path WINDOWS_TDLIB = TDLIB.resolve("windows");
    private static @NotNull Path LINUX_TDLIB = TDLIB.resolve("linux");

    public static @NotNull Path WINDOWS_LIBCRYPTO_X64 = WINDOWS_TDLIB.resolve("libcrypto-1_1-x64.dll");
    public static @NotNull Path WINDOWS_LIBSSL_X64 = WINDOWS_TDLIB.resolve("libssl-1_1-x64.dll");
    public static @NotNull Path WINDOWS_ZLIB = WINDOWS_TDLIB.resolve("zlib1.dll");
    public static @NotNull Path WINDOWS_TDJNI = WINDOWS_TDLIB.resolve("tdjni.dll");

    public static @NotNull Path LINUX_TDJNI = LINUX_TDLIB.resolve("tdjni.so");

    @Override
    public void preInit() {
        @NonNull val os = this.getOs();
        @NonNull val arch = this.getArch();

        log.debug("{} - {}", os, arch);

        if (os != null) {
            if (os.toLowerCase(Locale.ROOT).startsWith("windows")) {
                extractAndLoadDll(WINDOWS_LIBCRYPTO_X64);
                extractAndLoadDll(WINDOWS_LIBSSL_X64);
                extractAndLoadDll(WINDOWS_ZLIB);
                extractAndLoadDll(WINDOWS_TDJNI);
            } else if (os.toLowerCase(Locale.ROOT).startsWith("linux"))
                extractAndLoadDll(LINUX_TDJNI);
        }
    }

    public @NotNull String getOs() {
        return System.getProperty("os.name");
    }

    public @NotNull String getArch() {
        return System.getProperty("os.arch");
    }

    private void extractAndLoadDll(@NonNull final Path path) {
        this.extractDll(path);
        this.loadDll(path);
    }

    @SneakyThrows
    public void extractDll(@NonNull final Path path) {
        @NonNull var name = path.toString();

        while (name.contains("\\"))
            name = name.replace("\\", "/");

        @Cleanup val inputStream = TDLib.class.getClassLoader().getResourceAsStream(name);

        if (inputStream != null) {
            path.getParent().toFile().mkdirs();

            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);

            log.debug("Extracted {}", name);
        } else log.warn("Not found {} ({})!", name, inputStream);
    }

    private void loadDll(@NonNull final Path path) {
        @NonNull val lib = path.toFile().getName()
                .replaceAll(".so", "")
                .replaceAll(".dll", "");

        System.loadLibrary(lib);
    }

    @Override
    @SneakyThrows
    public void init() {
        Client.setLogMessageHandler(this.verbosityLevelConsole, (verbosityLevel, message) -> log.debug(message));

        Client.execute(new TdApi.SetLogVerbosityLevel(this.verbosityLevelFile));
        Client.execute(new TdApi.SetLogStream(new TdApi.LogStreamFile("logs/tdlib.log", Integer.MAX_VALUE, false)));

        this.client = Client.create(new UpdateAuthorizationStateResultHandler(), exception -> log.error(exception.getMessage(), exception), exception -> log.error(exception.getMessage(), exception));
    }

    @Override
    public void postInit() {
        this.loadChats();
    }

    public void stop() {
        this.client.send(new TdApi.Close(), object -> log.debug(object));
    }

    public void loadChats() {
        this.client.send(
                new TdApi.LoadChats(
                        new TdApi.ChatListMain(),
                        1000
                ),
                log::debug
        );
        this.client.send(
                new TdApi.GetChatHistory(),
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