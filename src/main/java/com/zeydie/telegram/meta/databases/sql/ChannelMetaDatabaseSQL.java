package com.zeydie.telegram.meta.databases.sql;

import com.zeydie.telegram.meta.TelegramMeta;
import com.zeydie.telegram.meta.api.IDatabase;
import com.zeydie.telegram.meta.api.managers.IDatabaseManager;
import com.zeydie.telegram.meta.api.metas.IChannelMeta;
import com.zeydie.telegram.meta.builders.QueryBuilder;
import com.zeydie.telegram.meta.configs.MetaSQLConfig;
import com.zeydie.telegram.meta.data.SupergroupMeta;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public final class ChannelMetaDatabaseSQL implements IDatabase, IChannelMeta {
    private IDatabaseManager databaseManager;
    private MetaSQLConfig.ChannelsMetaTable channelsMetaTable;

    @Override
    public @NonNull Connection getConnection() {
        return this.databaseManager.getConnection();
    }

    @Override
    public void load() {
        log.info("Loading...");

        @NonNull val instance = TelegramMeta.getInstance();

        this.databaseManager = instance.getDatabaseSQLManager();
        this.channelsMetaTable = this.databaseManager.getChannelsMetaTable();

        this.getChannelsMetas().forEach(channelMeta -> instance.getChannelMetaManager().putOrUpdate(channelMeta));

        log.info("Successfully!");
    }

    @Override
    public void save() {
        log.info("Saving channels metas SQL...");

        TelegramMeta.getInstance()
                .getChannelMetaManager()
                .getChannelsMetas()
                .forEach(
                        channelMeta -> {
                            try {
                                log.info("Saving meta for {}", channelMeta.getChannelId());

                                @Cleanup val connection = getConnection();
                                @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(
                                        QueryBuilder.builder()
                                                .table(this.channelsMetaTable.getNameTable())
                                                .build()
                                                .select("*")
                                                .where(this.channelsMetaTable.getChannelIdColumn(), channelMeta.getChannelId())
                                                .query()
                                );

                                @Cleanup val resultSet = preparedStatement.executeQuery();
                                @NonNull var query = resultSet.next() ?
                                        QueryBuilder.builder()
                                                .table(this.channelsMetaTable.getNameTable())
                                                .build()
                                                .update(this.channelsMetaTable.getSupergroupColumn(), channelMeta.isSupergroup())
                                                .update(this.channelsMetaTable.getPeriodColumn(), channelMeta.parseObject(channelMeta.getPeriod()))
                                                .update(this.channelsMetaTable.getMemberCountColumn(), channelMeta.parseObject(channelMeta.getMemberCount()))
                                                .update(this.channelsMetaTable.getMeanViewCountColumn(), channelMeta.parseObject(channelMeta.getMeanViewCount()))
                                                .update(this.channelsMetaTable.getMeanShareCountColumn(), channelMeta.parseObject(channelMeta.getMeanShareCount()))
                                                .update(this.channelsMetaTable.getEnabledNotificationsPercentageColumn(), channelMeta.getEnabledNotificationsPercentage())
                                                .update(this.channelsMetaTable.getMemberCountGraphColumn(), channelMeta.parseObject(channelMeta.getMemberCountGraph()))
                                                .update(this.channelsMetaTable.getJoinGraphColumn(), channelMeta.parseObject(channelMeta.getJoinGraph()))
                                                .update(this.channelsMetaTable.getMuteGraphColumn(), channelMeta.parseObject(channelMeta.getMuteGraph()))
                                                .update(this.channelsMetaTable.getViewCountByHourGraphColumn(), channelMeta.parseObject(channelMeta.getViewCountByHourGraph()))
                                                .update(this.channelsMetaTable.getViewCountBySourceGraphColumn(), channelMeta.parseObject(channelMeta.getViewCountBySourceGraph()))
                                                .update(this.channelsMetaTable.getJoinBySourceGraphColumn(), channelMeta.parseObject(channelMeta.getJoinBySourceGraph()))
                                                .update(this.channelsMetaTable.getLanguageGraphColumn(), channelMeta.parseObject(channelMeta.getLanguageGraph()))
                                                .update(this.channelsMetaTable.getMessageInteractionGraphColumn(), channelMeta.parseObject(channelMeta.getMessageInteractionGraph()))
                                                .update(this.channelsMetaTable.getInstantViewInteractionGraphColumn(), channelMeta.parseObject(channelMeta.getInstantViewInteractionGraph()))
                                                .update(this.channelsMetaTable.getRecentMessageInteractionsColumn(), channelMeta.parseObject(channelMeta.getRecentMessageInteractions()))
                                                .update(this.channelsMetaTable.getMessageCountColumn(), channelMeta.parseObject(channelMeta.getMessageCount()))
                                                .update(this.channelsMetaTable.getViewerCountColumn(), channelMeta.parseObject(channelMeta.getViewerCount()))
                                                .update(this.channelsMetaTable.getSenderCountColumn(), channelMeta.parseObject(channelMeta.getSenderCount()))
                                                .update(this.channelsMetaTable.getMessageContentGraphColumn(), channelMeta.parseObject(channelMeta.getMessageContentGraph()))
                                                .update(this.channelsMetaTable.getActionGraphColumn(), channelMeta.parseObject(channelMeta.getActionGraph()))
                                                .update(this.channelsMetaTable.getDayGraphColumn(), channelMeta.parseObject(channelMeta.getDayGraph()))
                                                .update(this.channelsMetaTable.getWeekGraphColumn(), channelMeta.parseObject(channelMeta.getWeekGraph()))
                                                .update(this.channelsMetaTable.getTopSendersColumn(), channelMeta.parseObject(channelMeta.getTopSenders()))
                                                .update(this.channelsMetaTable.getTopAdministratorsColumn(), channelMeta.parseObject(channelMeta.getTopAdministrators()))
                                                .update(this.channelsMetaTable.getTopInvitersColumn(), channelMeta.parseObject(channelMeta.getTopInviters()))
                                                .where(this.channelsMetaTable.getChannelIdColumn(), channelMeta.getChannelId())
                                                .query() :
                                        QueryBuilder.builder()
                                                .table(this.channelsMetaTable.getNameTable())
                                                .build()
                                                .insert(this.channelsMetaTable.getSupergroupColumn(), channelMeta.isSupergroup())
                                                .insert(this.channelsMetaTable.getPeriodColumn(), channelMeta.parseObject(channelMeta.getPeriod()))
                                                .insert(this.channelsMetaTable.getChannelIdColumn(), channelMeta.getChannelId())
                                                .insert(this.channelsMetaTable.getMemberCountColumn(), channelMeta.parseObject(channelMeta.getMemberCount()))
                                                .insert(this.channelsMetaTable.getMeanViewCountColumn(), channelMeta.parseObject(channelMeta.getMeanViewCount()))
                                                .insert(this.channelsMetaTable.getMeanShareCountColumn(), channelMeta.parseObject(channelMeta.getMeanShareCount()))
                                                .insert(this.channelsMetaTable.getEnabledNotificationsPercentageColumn(), channelMeta.getEnabledNotificationsPercentage())
                                                .insert(this.channelsMetaTable.getMemberCountGraphColumn(), channelMeta.parseObject(channelMeta.getMemberCountGraph()))
                                                .insert(this.channelsMetaTable.getJoinGraphColumn(), channelMeta.parseObject(channelMeta.getJoinGraph()))
                                                .insert(this.channelsMetaTable.getMuteGraphColumn(), channelMeta.parseObject(channelMeta.getMuteGraph()))
                                                .insert(this.channelsMetaTable.getViewCountByHourGraphColumn(), channelMeta.parseObject(channelMeta.getViewCountByHourGraph()))
                                                .insert(this.channelsMetaTable.getViewCountBySourceGraphColumn(), channelMeta.parseObject(channelMeta.getViewCountBySourceGraph()))
                                                .insert(this.channelsMetaTable.getJoinBySourceGraphColumn(), channelMeta.parseObject(channelMeta.getJoinBySourceGraph()))
                                                .insert(this.channelsMetaTable.getLanguageGraphColumn(), channelMeta.parseObject(channelMeta.getLanguageGraph()))
                                                .insert(this.channelsMetaTable.getMessageInteractionGraphColumn(), channelMeta.parseObject(channelMeta.getMessageInteractionGraph()))
                                                .insert(this.channelsMetaTable.getInstantViewInteractionGraphColumn(), channelMeta.parseObject(channelMeta.getInstantViewInteractionGraph()))
                                                .insert(this.channelsMetaTable.getRecentMessageInteractionsColumn(), channelMeta.parseObject(channelMeta.getRecentMessageInteractions()))
                                                .insert(this.channelsMetaTable.getMessageCountColumn(), channelMeta.parseObject(channelMeta.getMessageCount()))
                                                .insert(this.channelsMetaTable.getViewerCountColumn(), channelMeta.parseObject(channelMeta.getViewerCount()))
                                                .insert(this.channelsMetaTable.getSenderCountColumn(), channelMeta.parseObject(channelMeta.getSenderCount()))
                                                .insert(this.channelsMetaTable.getMessageContentGraphColumn(), channelMeta.parseObject(channelMeta.getMessageContentGraph()))
                                                .insert(this.channelsMetaTable.getActionGraphColumn(), channelMeta.parseObject(channelMeta.getActionGraph()))
                                                .insert(this.channelsMetaTable.getDayGraphColumn(), channelMeta.parseObject(channelMeta.getDayGraph()))
                                                .insert(this.channelsMetaTable.getWeekGraphColumn(), channelMeta.parseObject(channelMeta.getWeekGraph()))
                                                .insert(this.channelsMetaTable.getTopSendersColumn(), channelMeta.parseObject(channelMeta.getTopSenders()))
                                                .insert(this.channelsMetaTable.getTopAdministratorsColumn(), channelMeta.parseObject(channelMeta.getTopAdministrators()))
                                                .insert(this.channelsMetaTable.getTopInvitersColumn(), channelMeta.parseObject(channelMeta.getTopInviters()))
                                                .query();

                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.executeUpdate();
                            } catch (final SQLException exception) {
                                log.error(exception.getMessage(), exception);
                            }
                        }
                );

        log.info("Channels metas saved!");
    }

    @SneakyThrows
    @Override
    public @NotNull List<SupergroupMeta> getChannelsMetas() {
        @NonNull val channelsMetas = new ArrayList<SupergroupMeta>();

        @Cleanup val connection = this.getConnection();
        @Cleanup val preparedStatement = connection.prepareStatement(
                QueryBuilder.builder()
                        .table(this.channelsMetaTable.getNameTable())
                        .build()
                        .select("*")
                        .query()
        );

        @Cleanup val resultSet = preparedStatement.executeQuery();

        while (resultSet.next())
            channelsMetas.add(new SupergroupMeta(resultSet));
        
        return channelsMetas;
    }
}