package com.zeydie.telegram.meta.configs;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public final class TDLibConfig {
    private int apiId = 123456798;
    private @NotNull String apiHash = "apiHash";
    private @NotNull String phoneNumber = "+100000000";
    private boolean useTestServer;
    private int price = 300;
}