package com.zeydie.telegram.meta.builders;

import lombok.Builder;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Builder
public final class QueryBuilder {
    private @NotNull String table;

    private boolean insertMode;
    private final @NotNull Map<String, Object> insertMap = new HashMap<>();

    private boolean selectMode;
    private final @NotNull List<String> selectColumns = new ArrayList<>();

    private boolean updateMode;
    private final @NotNull Map<String, Object> updateMap = new HashMap<>();

    private boolean deleteMode;

    private boolean whereMode;
    private final @NotNull Map<String, Object> whereMap = new HashMap<>();

    public @NotNull QueryBuilder insert(
            @NonNull final String column,
            @Nullable final Object value
    ) {
        this.insertMode = true;

        if (this.insertMap.containsKey(column))
            this.insertMap.replace(column, value);
        else
            this.insertMap.put(column, value);

        return this;
    }

    public @NotNull QueryBuilder select(@NonNull final String... columns) {
        this.selectMode = true;

        this.selectColumns.clear();
        this.selectColumns.addAll(Arrays.stream(columns).toList());

        return this;
    }

    public @NotNull QueryBuilder update(
            @NonNull final String column,
            @Nullable final Object value
    ) {
        this.updateMode = true;

        if (this.updateMap.containsKey(column))
            this.updateMap.replace(column, value);
        else
            this.updateMap.put(column, value);

        return this;
    }

    public @NotNull QueryBuilder delete() {
        this.deleteMode = true;

        return this;
    }

    public @NotNull QueryBuilder where(
            @NonNull final String column,
            @Nullable final Object value
    ) {
        this.whereMode = true;

        if (this.whereMap.containsKey(column))
            this.whereMap.replace(column, value);
        else
            this.whereMap.put(column, value);

        return this;
    }

    public @NotNull String query() {
        @NonNull val builder = new StringBuilder();

        if (this.insertMode) {
            builder.append("INSERT INTO `").append(this.table).append("` ");

            for (int i = 0; i < this.insertMap.size(); i++) {
                if (i == 0)
                    builder.append("(");

                builder.append("`").append(this.insertMap.keySet().toArray()[i]).append("`");

                if (i < this.insertMap.size() - 1)
                    builder.append(", ");
                else builder.append(")");
            }

            builder.append(" VALUES ");

            for (int i = 0; i < this.insertMap.size(); i++) {
                if (i == 0)
                    builder.append("(");

                builder.append("'").append(this.transformObject(this.insertMap.values().toArray()[i])).append("'");

                if (i < this.insertMap.size() - 1)
                    builder.append(", ");
                else builder.append(");");
            }

            return builder.toString();
        } else if (this.selectMode) {
            builder.append("SELECT ");

            for (int i = 0; i < this.selectColumns.size(); i++) {
                builder.append("`").append(this.selectColumns.get(i)).append("`");

                if (i < this.insertMap.size() - 1)
                    builder.append(", ");
            }

            builder.append(" FROM `").append(this.table).append("`");

        } else if (this.updateMode) {
            builder.append("UPDATE `").append(this.table).append("`").append(" ");
            builder.append("SET ");

            for (int i = 0; i < this.updateMap.size(); i++) {
                @NonNull final String column = this.updateMap.keySet().toArray(new String[]{})[i];
                @Nullable final Object value = this.transformObject(this.updateMap.values().toArray()[i]);

                builder.append("`").append(column).append("` = '").append(value).append("'");

                if (i < this.updateMap.size() - 1)
                    builder.append(", ");
            }
        } else if (this.deleteMode)
            builder.append("DELETE FROM `").append(this.table).append("`");

        if (this.whereMode) {
            builder.append(" WHERE ");

            for (int i = 0; i < this.whereMap.size(); i++) {
                @NonNull final String column = this.whereMap.keySet().toArray(new String[]{})[i];
                @Nullable final Object value = this.transformObject(this.whereMap.values().toArray()[i]);

                builder.append("`").append(column).append("` = '").append(value).append("'");

                if (i < this.whereMap.size() - 1)
                    builder.append(" AND ");
            }
        }

        return builder.toString();
    }

    private @Nullable Object transformObject(@Nullable final Object object) {
        if (object == null)
            return null;
        if (object instanceof @NonNull final Boolean bool)
            return bool ? 1 : 0;

        return object;
    }
}