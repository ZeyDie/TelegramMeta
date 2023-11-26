package com.zeydie.telegram.meta.data;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Data
public class GraphData {
    @NonFinal
    private String title;
    @NonFinal
    private Object[][] columns;
    @NonFinal
    private Types types;
    @NonFinal
    private Names name;
    @NonFinal
    private Colors colors;
    @NonFinal
    private String[] hidden;
    @NonFinal
    private Subchart subchart;
    @NonFinal
    private int strokeWidth;
    @NonFinal
    private String xTickFormatter;
    @NonFinal
    private String xTooltipFormatter;
    @NonFinal
    private String xRangeFormatter;
    @NonFinal
    private String yTickFormatter;
    @NonFinal
    private String yTooltipFormatter;
    @NonFinal
    private boolean stacked;

    @Data
    public static class Types {
        @NonFinal
        private String y0;
        @NonFinal
        private String y1;
        @NonFinal
        private String x;
    }

    @Data
    public static class Names {
        @NonFinal
        private String y0;
        @NonFinal
        private String y1;
    }

    @Data
    public static class Colors {
        @NonFinal
        private String y0;
        @NonFinal
        private String y1;
    }

    @Data
    private static class Subchart {
        private boolean show;
        private long[] defaultZoom;
    }

    @Data
    public static class Graph {
        @NonFinal
        private List<Object> x = new ArrayList<>();
        @NonFinal
        private List<Object> y0 = new ArrayList<>();
        @NonFinal
        private List<Object> y1 = new ArrayList<>();
    }

    public @NonNull Graph getGraph() {
        @NonNull val graph = new Graph();

        for (@NonNull val column : this.columns) {
            @NonNull List<Object> list = new ArrayList<>();

            for (int indexValue = 0; indexValue < column.length; indexValue++) {
                @NonNull val value = column[indexValue];

                if (indexValue == 0) {
                    if (value instanceof @NonNull String string)
                        switch (string) {
                            case "x" -> list = graph.getX();
                            case "y0" -> list = graph.getY0();
                            case "y1" -> list = graph.getY1();
                        }
                } else list.add(value);
            }
        }

        return graph;
    }
}