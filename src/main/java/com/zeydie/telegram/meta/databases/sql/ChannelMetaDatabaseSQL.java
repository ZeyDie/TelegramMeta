package com.zeydie.telegram.meta.databases.sql;

import com.zeydie.telegram.meta.TelegramMeta;
import com.zeydie.telegram.meta.api.IDatabase;
import com.zeydie.telegram.meta.api.managers.IDatabaseManager;
import com.zeydie.telegram.meta.api.metas.IChannelMeta;
import com.zeydie.telegram.meta.builders.QueryBuilder;
import com.zeydie.telegram.meta.configs.SQLConfig;
import com.zeydie.telegram.meta.data.ChannelMeta;
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
    private SQLConfig.ChannelsMetaTable channelsMetaTable;

    @Override
    public @NonNull Connection getConnection() {
        return this.databaseManager.getConnection();
    }

    @Override
    public void load() {
        this.databaseManager = TelegramMeta.getInstance().getDatabaseSQLManager();
        this.channelsMetaTable = this.databaseManager.getChannelsMetaTable();
    }

    @Override
    public void save() {
        this.databaseManager.getChannelsMetas()
                .forEach(
                        channelMeta -> {
                            try {
                                @NonNull val supergroupMeta = (SupergroupMeta) channelMeta;

                                @Cleanup val connection = getConnection();
                                @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(
                                        QueryBuilder.builder()
                                                .table(this.channelsMetaTable.getNameTable())
                                                .build()
                                                .select("*")
                                                .where(this.channelsMetaTable.getChannelIdColumn(), supergroupMeta.getChannelId())
                                                .query()
                                );

                                @Cleanup val resultSet = preparedStatement.executeQuery();
                                @NonNull var query = resultSet.next() ?
                                        QueryBuilder.builder()
                                                .table(this.channelsMetaTable.getNameTable())
                                                .build()
                                                .update(this.channelsMetaTable.getSupergroupColumn(), supergroupMeta.isSupergroup())
                                                .update(this.channelsMetaTable.getPeriodColumn(), supergroupMeta.parseObject(supergroupMeta.getPeriod()))
                                                .update(this.channelsMetaTable.getMemberCountColumn(), supergroupMeta.parseObject(supergroupMeta.getMemberCount()))
                                                .update(this.channelsMetaTable.getMeanViewCountColumn(), supergroupMeta.parseObject(supergroupMeta.getMeanViewCount()))
                                                .update(this.channelsMetaTable.getMeanShareCountColumn(), supergroupMeta.parseObject(supergroupMeta.getMeanShareCount()))
                                                .update(this.channelsMetaTable.getEnabledNotificationsPercentageColumn(), supergroupMeta.getEnabledNotificationsPercentage())
                                                .update(this.channelsMetaTable.getMemberCountGraphColumn(), supergroupMeta.parseObject(supergroupMeta.getMemberCountGraph()))
                                                .update(this.channelsMetaTable.getJoinGraphColumn(), supergroupMeta.parseObject(supergroupMeta.getJoinGraph()))
                                                .update(this.channelsMetaTable.getMuteGraphColumn(), supergroupMeta.parseObject(supergroupMeta.getMuteGraph()))
                                                .update(this.channelsMetaTable.getViewCountByHourGraphColumn(), supergroupMeta.parseObject(supergroupMeta.getViewCountByHourGraph()))
                                                .update(this.channelsMetaTable.getViewCountBySourceGraphColumn(), supergroupMeta.parseObject(supergroupMeta.getViewCountBySourceGraph()))
                                                .update(this.channelsMetaTable.getJoinBySourceGraphColumn(), supergroupMeta.parseObject(supergroupMeta.getJoinBySourceGraph()))
                                                .update(this.channelsMetaTable.getLanguageGraphColumn(), supergroupMeta.parseObject(supergroupMeta.getLanguageGraph()))
                                                .update(this.channelsMetaTable.getMessageInteractionGraphColumn(), supergroupMeta.parseObject(supergroupMeta.getMessageInteractionGraph()))
                                                .update(this.channelsMetaTable.getInstantViewInteractionGraphColumn(), supergroupMeta.parseObject(supergroupMeta.getInstantViewInteractionGraph()))
                                                .update(this.channelsMetaTable.getRecentMessageInteractionsColumn(), supergroupMeta.parseObject(supergroupMeta.getRecentMessageInteractions()))
                                                .update(this.channelsMetaTable.getMessageCountColumn(), supergroupMeta.parseObject(supergroupMeta.getMessageCount()))
                                                .update(this.channelsMetaTable.getViewerCountColumn(), supergroupMeta.parseObject(supergroupMeta.getViewerCount()))
                                                .update(this.channelsMetaTable.getSenderCountColumn(), supergroupMeta.parseObject(supergroupMeta.getSenderCount()))
                                                .update(this.channelsMetaTable.getMessageContentGraphColumn(), supergroupMeta.parseObject(supergroupMeta.getMessageContentGraph()))
                                                .update(this.channelsMetaTable.getActionGraphColumn(), supergroupMeta.parseObject(supergroupMeta.getActionGraph()))
                                                .update(this.channelsMetaTable.getDayGraphColumn(), supergroupMeta.parseObject(supergroupMeta.getDayGraph()))
                                                .update(this.channelsMetaTable.getWeekGraphColumn(), supergroupMeta.parseObject(supergroupMeta.getWeekGraph()))
                                                .update(this.channelsMetaTable.getTopSendersColumn(), supergroupMeta.parseObject(supergroupMeta.getTopSenders()))
                                                .update(this.channelsMetaTable.getTopAdministratorsColumn(), supergroupMeta.parseObject(supergroupMeta.getTopAdministrators()))
                                                .update(this.channelsMetaTable.getTopInvitersColumn(), supergroupMeta.parseObject(supergroupMeta.getTopInviters()))
                                                .where(this.channelsMetaTable.getChannelIdColumn(), supergroupMeta.getChannelId())
                                                .query() :
                                        QueryBuilder.builder()
                                                .table(this.channelsMetaTable.getNameTable())
                                                .build()
                                                .insert(this.channelsMetaTable.getSupergroupColumn(), supergroupMeta.isSupergroup())
                                                .insert(this.channelsMetaTable.getPeriodColumn(), supergroupMeta.parseObject(supergroupMeta.getPeriod()))
                                                .insert(this.channelsMetaTable.getChannelIdColumn(), supergroupMeta.getChannelId())
                                                .insert(this.channelsMetaTable.getMemberCountColumn(), supergroupMeta.parseObject(supergroupMeta.getMemberCount()))
                                                .insert(this.channelsMetaTable.getMeanViewCountColumn(), supergroupMeta.parseObject(supergroupMeta.getMeanViewCount()))
                                                .insert(this.channelsMetaTable.getMeanShareCountColumn(), supergroupMeta.parseObject(supergroupMeta.getMeanShareCount()))
                                                .insert(this.channelsMetaTable.getEnabledNotificationsPercentageColumn(), supergroupMeta.getEnabledNotificationsPercentage())
                                                .insert(this.channelsMetaTable.getMemberCountGraphColumn(), supergroupMeta.parseObject(supergroupMeta.getMemberCountGraph()))
                                                .insert(this.channelsMetaTable.getJoinGraphColumn(), supergroupMeta.parseObject(supergroupMeta.getJoinGraph()))
                                                .insert(this.channelsMetaTable.getMuteGraphColumn(), supergroupMeta.parseObject(supergroupMeta.getMuteGraph()))
                                                .insert(this.channelsMetaTable.getViewCountByHourGraphColumn(), supergroupMeta.parseObject(supergroupMeta.getViewCountByHourGraph()))
                                                .insert(this.channelsMetaTable.getViewCountBySourceGraphColumn(), supergroupMeta.parseObject(supergroupMeta.getViewCountBySourceGraph()))
                                                .insert(this.channelsMetaTable.getJoinBySourceGraphColumn(), supergroupMeta.parseObject(supergroupMeta.getJoinBySourceGraph()))
                                                .insert(this.channelsMetaTable.getLanguageGraphColumn(), supergroupMeta.parseObject(supergroupMeta.getLanguageGraph()))
                                                .insert(this.channelsMetaTable.getMessageInteractionGraphColumn(), supergroupMeta.parseObject(supergroupMeta.getMessageInteractionGraph()))
                                                .insert(this.channelsMetaTable.getInstantViewInteractionGraphColumn(), supergroupMeta.parseObject(supergroupMeta.getInstantViewInteractionGraph()))
                                                .insert(this.channelsMetaTable.getRecentMessageInteractionsColumn(), supergroupMeta.parseObject(supergroupMeta.getRecentMessageInteractions()))
                                                .insert(this.channelsMetaTable.getMessageCountColumn(), supergroupMeta.parseObject(supergroupMeta.getMessageCount()))
                                                .insert(this.channelsMetaTable.getViewerCountColumn(), supergroupMeta.parseObject(supergroupMeta.getViewerCount()))
                                                .insert(this.channelsMetaTable.getSenderCountColumn(), supergroupMeta.parseObject(supergroupMeta.getSenderCount()))
                                                .insert(this.channelsMetaTable.getMessageContentGraphColumn(), supergroupMeta.parseObject(supergroupMeta.getMessageContentGraph()))
                                                .insert(this.channelsMetaTable.getActionGraphColumn(), supergroupMeta.parseObject(supergroupMeta.getActionGraph()))
                                                .insert(this.channelsMetaTable.getDayGraphColumn(), supergroupMeta.parseObject(supergroupMeta.getDayGraph()))
                                                .insert(this.channelsMetaTable.getWeekGraphColumn(), supergroupMeta.parseObject(supergroupMeta.getWeekGraph()))
                                                .insert(this.channelsMetaTable.getTopSendersColumn(), supergroupMeta.parseObject(supergroupMeta.getTopSenders()))
                                                .insert(this.channelsMetaTable.getTopAdministratorsColumn(), supergroupMeta.parseObject(supergroupMeta.getTopAdministrators()))
                                                .insert(this.channelsMetaTable.getTopInvitersColumn(), supergroupMeta.parseObject(supergroupMeta.getTopInviters()))
                                                .query();

                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.executeUpdate();
                            } catch (final SQLException exception) {
                                log.error(exception.getMessage(), exception);
                            }
                        }
                );
    }

    @SneakyThrows
    @Override
    public @NotNull List<ChannelMeta> getChannelsMetas() {
        @NonNull val channelsMetas = new ArrayList<ChannelMeta>();

        @Cleanup val connection = this.getConnection();
        @Cleanup val preparedStatement = connection.prepareStatement(
                QueryBuilder.builder()
                        .table(this.channelsMetaTable.getNameTable())
                        .build()
                        .select("*")
                        .query()
        );

        @Cleanup val resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            @NonNull val channelMeta = new ChannelMeta(resultSet);

            if (channelMeta.isSupergroup())
                channelsMetas.add(new SupergroupMeta(resultSet));
            else
                channelsMetas.add(new ChannelMeta(resultSet));
        }

        return channelsMetas;
    }
}