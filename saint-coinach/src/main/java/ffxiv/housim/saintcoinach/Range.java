package ffxiv.housim.saintcoinach;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Range {
    @Getter
    private int start;
    @Getter
    private int length;
    @Getter
    private int end;

    public Range(int start, int length) {
        this.start = start;
        this.length = length;
        this.end = start + length;
    }

    public boolean contains(int value) {
        return value >= start && value < end;
    }

    public boolean intersects(Range other) {
        return contains(other.start)
                || contains(other.end - 1)
                || other.contains(start)
                || other.contains(end - 1);
    }

    public static boolean contains(Collection<Range> ranges, final int value) {
        return ranges.stream().anyMatch(it -> {
            return it.contains(value);
        });
    }

    public static Range[] combine(List<Range> ranges) {
        if (ranges == null || ranges.size() == 0) {
            return new Range[0];
        }

        List<Range> combined = new ArrayList<>();

        List<Range> ordered = ranges.stream()//
                .sorted(Comparator.comparingInt(it -> it.start))//
                .collect(Collectors.toList());

        int currentStart = ordered.get(0).start;
        int currentEnd = currentStart;
        for (Range range : ordered) {
            if (range.start > currentEnd) {
                if (currentEnd > currentStart) {
                    combined.add(new Range(currentStart, currentEnd - currentStart));
                }

                currentStart = range.start;
                currentEnd = range.end;
            } else {
                currentEnd = Math.max(currentEnd, range.end);
            }
        }
        if (currentEnd > currentStart) {
            combined.add(new Range(currentStart, currentEnd - currentStart));
        }

        return combined.stream()//
                .sorted(Comparator.comparingInt(it -> it.start))//
                .collect(Collectors.toList()).toArray(new Range[0]);
    }
}
