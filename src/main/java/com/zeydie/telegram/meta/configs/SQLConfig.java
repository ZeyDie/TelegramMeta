package com.zeydie.telegram.meta.configs;

import com.mysql.cj.jdbc.Driver;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.NonFinal;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
public final class SQLConfig {
    private int autosaveMinutes = 1;
    @NonFinal
    private @NotNull String driver = Driver.class.getName();
    @NonFinal
    private @NotNull String jdbc = "mysql";
    @NonFinal
    private @NotNull String ip = "localhost";
    @NonFinal
    private @NotNull String database = "database";
    @NonFinal
    private int port = 3306;

    @NonFinal
    private @NotNull String username = "username";
    @NonFinal
    private @NotNull String password = "password";

    @NonFinal
    private @NotNull Map<String, Object> property = new HashMap<>();

    @NonFinal
    private @NotNull ChannelsMetaTable channelsMetaTable = new ChannelsMetaTable();

    public @NotNull HikariDataSource getHikariDataSource() {
        @NonNull val hikariDataSource = new HikariDataSource();

        hikariDataSource.setDriverClassName(this.driver);
        hikariDataSource.setJdbcUrl(
                String.format(
                        "jdbc:%s://%s:%d/%s",
                        this.jdbc,
                        this.ip,
                        this.port,
                        this.database
                )
        );
        hikariDataSource.setUsername(this.username);
        hikariDataSource.setPassword(this.password);

        this.property.forEach(hikariDataSource::addDataSourceProperty);

        return hikariDataSource;
    }

    @Data
    public static class ChannelsMetaTable {
        @NonFinal
        private @NotNull String nameTable = "telegram_meta_channels";
        @NonFinal
        private @NotNull String idColumn = "id";
        @NonFinal
        private @NotNull String channelIdColumn = "channel_id";
        @NonFinal
        private @NotNull String supergroupColumn = "supergroup";
        @NonFinal
        private @NotNull String periodColumn = "period";
        @NonFinal
        private @NotNull String memberCountColumn = "memberCount";
        @NonFinal
        private @NotNull String meanViewCountColumn = "meanViewCount";
        @NonFinal
        private @NotNull String meanShareCountColumn = "meanShareCount";
        @NonFinal
        private @NotNull String enabledNotificationsPercentageColumn = "enabledNotificationsPercentage";
        @NonFinal
        private @NotNull String memberCountGraphColumn = "memberCountGraph";
        @NonFinal
        private @NotNull String joinGraphColumn = "joinGraph";
        @NonFinal
        private @NotNull String muteGraphColumn = "muteGraph";
        @NonFinal
        private @NotNull String viewCountByHourGraphColumn = "viewCountByHourGraph";
        @NonFinal
        private @NotNull String viewCountBySourceGraphColumn = "viewCountBySourceGraph";
        @NonFinal
        private @NotNull String joinBySourceGraphColumn = "joinBySourceGraph";
        @NonFinal
        private @NotNull String languageGraphColumn = "languageGraph";
        @NonFinal
        private @NotNull String messageInteractionGraphColumn = "messageInteractionGraph";
        @NonFinal
        private @NotNull String instantViewInteractionGraphColumn = "instantViewInteractionGraph";
        @NonFinal
        private @NotNull String recentMessageInteractionsColumn = "recentMessageInteractions";
        @NonFinal
        private @NotNull String messageCountColumn = "messageCount";
        @NonFinal
        private @NotNull String viewerCountColumn = "viewerCount";
        @NonFinal
        private @NotNull String senderCountColumn = "senderCount";
        @NonFinal
        private @NotNull String messageContentGraphColumn = "messageContentGraph";
        @NonFinal
        private @NotNull String actionGraphColumn = "actionGraph";
        @NonFinal
        private @NotNull String dayGraphColumn = "dayGraph";
        @NonFinal
        private @NotNull String weekGraphColumn = "weekGraph";
        @NonFinal
        private @NotNull String topSendersColumn = "topSenders";
        @NonFinal
        private @NotNull String topAdministratorsColumn = "topAdministrators";
        @NonFinal
        private @NotNull String topInvitersColumn = "topInviters";
    }
}