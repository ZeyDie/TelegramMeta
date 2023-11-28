package com.zeydie.telegram.meta.data;

import com.zeydie.sgson.SGsonBase;
import com.zeydie.telegram.meta.configs.ConfigStore;
import lombok.*;
import lombok.experimental.NonFinal;
import org.drinkless.tdlib.TdApi;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;

@Data
@NoArgsConstructor
public class ChannelMeta {
    @NonFinal
    private int id;
    @NonFinal
    private long channelId;
    @NonFinal
    private boolean supergroup;
    @NonFinal
    private TdApi.DateRange period;
    @NonFinal
    private TdApi.StatisticalValue memberCount;
    @NonFinal
    private TdApi.StatisticalValue meanViewCount;
    @NonFinal
    private TdApi.StatisticalValue meanShareCount;
    @NonFinal
    private double enabledNotificationsPercentage;
    @NonFinal
    private GraphData memberCountGraph;
    @NonFinal
    private GraphData joinGraph;
    @NonFinal
    private GraphData muteGraph;
    @NonFinal
    private GraphData viewCountByHourGraph;
    @NonFinal
    private GraphData viewCountBySourceGraph;
    @NonFinal
    private GraphData joinBySourceGraph;
    @NonFinal
    private GraphData languageGraph;
    @NonFinal
    private GraphData messageInteractionGraph;
    @NonFinal
    private GraphData instantViewInteractionGraph;
    @NonFinal
    private TdApi.ChatStatisticsMessageInteractionInfo[] recentMessageInteractions;

    @SneakyThrows
    public ChannelMeta(@NonNull final ResultSet resultSet) {
        @NonNull val table = ConfigStore.getMetaSQLConfig().getChannelsMetaTable();

        this.setId(resultSet.getInt(table.getIdColumn()));
        this.setChannelId(resultSet.getLong(table.getChannelIdColumn()));
        this.setSupergroup(resultSet.getBoolean(table.getSupergroupColumn()));
        this.setPeriod(this.parse(resultSet.getString(table.getPeriodColumn()), new TdApi.DateRange()));
        this.setMemberCount(this.parse(resultSet.getString(table.getMemberCountColumn()), new TdApi.StatisticalValue()));
        this.setMeanViewCount(this.parse(resultSet.getString(table.getMeanViewCountColumn()), new TdApi.StatisticalValue()));
        this.setMeanShareCount(this.parse(resultSet.getString(table.getMeanShareCountColumn()), new TdApi.StatisticalValue()));
        this.setEnabledNotificationsPercentage(resultSet.getDouble(table.getEnabledNotificationsPercentageColumn()));
        this.setMemberCountGraph(this.parse(resultSet.getString(table.getMemberCountGraphColumn()), new GraphData()));
        this.setJoinGraph(this.parse(resultSet.getString(table.getJoinGraphColumn()), new GraphData()));
        this.setMuteGraph(this.parse(resultSet.getString(table.getMuteGraphColumn()), new GraphData()));
        this.setViewCountByHourGraph(this.parse(resultSet.getString(table.getViewCountByHourGraphColumn()), new GraphData()));
        this.setViewCountBySourceGraph(this.parse(resultSet.getString(table.getViewCountBySourceGraphColumn()), new GraphData()));
        this.setJoinBySourceGraph(this.parse(resultSet.getString(table.getJoinGraphColumn()), new GraphData()));
        this.setLanguageGraph(this.parse(resultSet.getString(table.getLanguageGraphColumn()), new GraphData()));
        this.setMessageInteractionGraph(this.parse(resultSet.getString(table.getMessageInteractionGraphColumn()), new GraphData()));
        this.setInstantViewInteractionGraph(this.parse(resultSet.getString(table.getInstantViewInteractionGraphColumn()), new GraphData()));
        this.setRecentMessageInteractions(this.parse(resultSet.getString(table.getRecentMessageInteractionsColumn()), new TdApi.ChatStatisticsMessageInteractionInfo[]{}));
    }

    public void copyOf(@NonNull final ChannelMeta channelMeta) {
        this.setId(channelMeta.getId());
        this.setChannelId(channelMeta.getChannelId());
        this.setSupergroup(channelMeta.isSupergroup());
        this.setPeriod(channelMeta.getPeriod());
        this.setMemberCount(channelMeta.getMemberCount());
        this.setMeanViewCount(channelMeta.getMeanViewCount());
        this.setMeanShareCount(channelMeta.getMeanShareCount());
        this.setEnabledNotificationsPercentage(channelMeta.getEnabledNotificationsPercentage());
        this.setMemberCount(channelMeta.getMemberCount());
        this.setJoinGraph(channelMeta.getJoinGraph());
        this.setMuteGraph(channelMeta.getMuteGraph());
        this.setViewCountByHourGraph(channelMeta.getViewCountByHourGraph());
        this.setViewCountBySourceGraph(channelMeta.getViewCountBySourceGraph());
        this.setJoinBySourceGraph(channelMeta.getJoinBySourceGraph());
        this.setLanguageGraph(channelMeta.getLanguageGraph());
        this.setMessageInteractionGraph(channelMeta.getMessageInteractionGraph());
        this.setInstantViewInteractionGraph(channelMeta.getInstantViewInteractionGraph());
        this.setRecentMessageInteractions(channelMeta.getRecentMessageInteractions());
    }

    public void copyOf(@NonNull final TdApi.ChatStatistics chatStatistics) {
        if (chatStatistics instanceof @NonNull TdApi.ChatStatisticsChannel chatStatisticsChannel) {
            this.setPeriod(chatStatisticsChannel.period);
            this.setMemberCount(chatStatisticsChannel.memberCount);
            this.setMeanViewCount(chatStatisticsChannel.meanViewCount);
            this.setMeanShareCount(chatStatisticsChannel.meanShareCount);
            this.setEnabledNotificationsPercentage(chatStatisticsChannel.enabledNotificationsPercentage);
            this.setMemberCountGraph(this.parseGraphObject(chatStatisticsChannel.memberCountGraph));
            this.setJoinGraph(this.parseGraphObject(chatStatisticsChannel.joinGraph));
            this.setMuteGraph(this.parseGraphObject(chatStatisticsChannel.muteGraph));
            this.setViewCountByHourGraph(this.parseGraphObject(chatStatisticsChannel.viewCountByHourGraph));
            this.setViewCountBySourceGraph(this.parseGraphObject(chatStatisticsChannel.viewCountBySourceGraph));
            this.setJoinBySourceGraph(this.parseGraphObject(chatStatisticsChannel.joinBySourceGraph));
            this.setLanguageGraph(this.parseGraphObject(chatStatisticsChannel.languageGraph));
            this.setMessageInteractionGraph(this.parseGraphObject(chatStatisticsChannel.messageInteractionGraph));
            this.setInstantViewInteractionGraph(this.parseGraphObject(chatStatisticsChannel.instantViewInteractionGraph));
            this.setRecentMessageInteractions(chatStatisticsChannel.recentMessageInteractions);

            if (this.getMemberCountGraph() != null)
                this.getMemberCountGraph().setTitle("member_count_graph");
            if (this.getJoinGraph() != null)
                this.getJoinGraph().setTitle("join_graph");
            if (this.getMuteGraph() != null)
                this.getMuteGraph().setTitle("mute_graph");
            if (this.getViewCountByHourGraph() != null)
                this.getViewCountByHourGraph().setTitle("view_count_by_hours_graph");
            if (this.getViewCountBySourceGraph() != null)
                this.getViewCountBySourceGraph().setTitle("view_count_by_source_graph");
            if (this.getJoinBySourceGraph() != null)
                this.getJoinBySourceGraph().setTitle("join_by_source_graph");
            if (this.getLanguageGraph() != null)
                this.getLanguageGraph().setTitle("language_graph");
            if (this.getInstantViewInteractionGraph() != null)
                this.getInstantViewInteractionGraph().setTitle("instant_view_interaction_graph");
        }
    }

    public @Nullable <T> T parseGraphObject(@Nullable final Object object) {
        if (object instanceof @NonNull TdApi.StatisticalGraphData statisticalGraphData)
            return this.parseGraph(statisticalGraphData.jsonData);

        return null;
    }

    public @Nullable <T> T parseGraph(@Nullable final String json) {
        if (json == null) return null;

        return (T) this.parse(json, new GraphData());
    }

    public @Nullable <T> T parse(
            @Nullable final String json,
            @NonNull final T clazz
    ) {
        if (json == null) return null;

        return new SGsonBase().fromJsonToObject(json, clazz);
    }

    public @Nullable String parseObject(@Nullable final Object object) {
        if (object == null) return null;

        return new SGsonBase().fromObjectToJson(object);
    }
}