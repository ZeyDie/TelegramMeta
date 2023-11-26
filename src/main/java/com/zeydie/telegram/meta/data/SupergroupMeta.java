package com.zeydie.telegram.meta.data;

import com.zeydie.telegram.meta.TelegramMeta;
import lombok.*;
import lombok.experimental.NonFinal;
import org.drinkless.tdlib.TdApi;

import java.sql.ResultSet;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class SupergroupMeta extends ChannelMeta {
    @NonFinal
    private TdApi.StatisticalValue messageCount;
    @NonFinal
    private TdApi.StatisticalValue viewerCount;
    @NonFinal
    private TdApi.StatisticalValue senderCount;
    @NonFinal
    private GraphData messageContentGraph;
    @NonFinal
    private GraphData actionGraph;
    @NonFinal
    private GraphData dayGraph;
    @NonFinal
    private GraphData weekGraph;
    @NonFinal
    private TdApi.ChatStatisticsMessageSenderInfo[] topSenders;
    @NonFinal
    private TdApi.ChatStatisticsAdministratorActionsInfo[] topAdministrators;
    @NonFinal
    private TdApi.ChatStatisticsInviterInfo[] topInviters;

    @SneakyThrows
    public SupergroupMeta(@NonNull final ResultSet resultSet) {
        super(resultSet);

        if (super.isSupergroup()) {
            @NonNull val table = TelegramMeta.getInstance().getDatabaseSQLManager().getChannelsMetaTable();

            this.setMessageCount(this.parse(resultSet.getString(table.getMessageCountColumn()), new TdApi.StatisticalValue()));
            this.setViewerCount(this.parse(resultSet.getString(table.getViewerCountColumn()), new TdApi.StatisticalValue()));
            this.setSenderCount(this.parse(resultSet.getString(table.getSenderCountColumn()), new TdApi.StatisticalValue()));
            this.setMessageContentGraph(this.parse(resultSet.getString(table.getMessageContentGraphColumn()), new GraphData()));
            this.setActionGraph(this.parse(resultSet.getString(table.getActionGraphColumn()), new GraphData()));
            this.setDayGraph(this.parse(resultSet.getString(table.getDayGraphColumn()), new GraphData()));
            this.setWeekGraph(this.parse(resultSet.getString(table.getWeekGraphColumn()), new GraphData()));
            this.setTopSenders(this.parse(resultSet.getString(table.getTopSendersColumn()), new TdApi.ChatStatisticsMessageSenderInfo[]{}));
            this.setTopAdministrators(this.parse(resultSet.getString(table.getTopAdministratorsColumn()), new TdApi.ChatStatisticsAdministratorActionsInfo[]{}));
            this.setTopInviters(this.parse(resultSet.getString(table.getTopInvitersColumn()), new TdApi.ChatStatisticsInviterInfo[]{}));
        }
    }

    @Override
    public void copyOf(@NonNull final ChannelMeta channelMeta) {
        super.copyOf(channelMeta);

        if (channelMeta instanceof @NonNull SupergroupMeta supergroupMeta) {
            this.setMessageCount(supergroupMeta.getMessageCount());
            this.setViewerCount(supergroupMeta.getViewerCount());
            this.setSenderCount(supergroupMeta.getSenderCount());
            this.setMessageContentGraph(supergroupMeta.getMessageContentGraph());
            this.setActionGraph(supergroupMeta.getActionGraph());
            this.setDayGraph(supergroupMeta.getDayGraph());
            this.setWeekGraph(supergroupMeta.getWeekGraph());
            this.setTopSenders(supergroupMeta.getTopSenders());
            this.setTopAdministrators(supergroupMeta.getTopAdministrators());
            this.setTopInviters(supergroupMeta.getTopInviters());
        }
    }

    @Override
    public void copyOf(@NonNull final TdApi.ChatStatistics chatStatistics) {
        super.copyOf(chatStatistics);

        if (chatStatistics instanceof @NonNull TdApi.ChatStatisticsSupergroup chatStatisticsSupergroup) {
            this.setSupergroup(true);
            this.setPeriod(chatStatisticsSupergroup.period);
            this.setMemberCount(chatStatisticsSupergroup.memberCount);
            this.setMessageCount(chatStatisticsSupergroup.messageCount);
            this.setViewerCount(chatStatisticsSupergroup.viewerCount);
            this.setSenderCount(chatStatisticsSupergroup.senderCount);
            this.setMessageContentGraph(this.parseGraphObject(chatStatisticsSupergroup.messageContentGraph));
            this.setActionGraph(this.parseGraphObject(chatStatisticsSupergroup.actionGraph));
            this.setDayGraph(this.parseGraphObject(chatStatisticsSupergroup.dayGraph));
            this.setWeekGraph(this.parseGraphObject(chatStatisticsSupergroup.weekGraph));
            this.setTopSenders(chatStatisticsSupergroup.topSenders);
            this.setTopAdministrators(chatStatisticsSupergroup.topAdministrators);
            this.setTopInviters(chatStatisticsSupergroup.topInviters);

            if (this.getMessageContentGraph() != null)
                this.getMessageContentGraph().setTitle("message_content_graph");
            if (this.getActionGraph() != null)
                this.getActionGraph().setTitle("action_graph");
            if (this.getDayGraph() != null)
                this.getDayGraph().setTitle("day_graph");
            if (this.getWeekGraph() != null)
                this.getWeekGraph().setTitle("week_graph");
        }
    }
}