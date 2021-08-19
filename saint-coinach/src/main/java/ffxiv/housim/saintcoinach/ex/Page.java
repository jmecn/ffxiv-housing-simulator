package ffxiv.housim.saintcoinach.ex;

import com.google.common.base.Objects;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Page {
    @Getter
    private int start;
    @Getter
    private int length;
    @Getter
    private int end;

    public Page(int start, int length) {
        this.start = start;
        this.length = length;
        this.end = start + length;
    }

    public boolean contains(int value) {
        return value >= start && value < end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Page page = (Page) o;
        return start == page.start && length == page.length;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(start, length);
    }

    @Override
    public String toString() {
        return "Page{" +
                "start=" + start +
                ", length=" + length +
                '}';
    }

    public boolean intersects(Page other) {
        return contains(other.start)
                || contains(other.end - 1)
                || other.contains(start)
                || other.contains(end - 1);
    }

    public static boolean contains(Collection<Page> pages, final int value) {
        return pages.stream().anyMatch(it -> it.contains(value));
    }

    public static Page[] combine(List<Page> pages) {
        if (pages == null || pages.size() == 0) {
            return new Page[0];
        }

        List<Page> combined = new ArrayList<>();

        List<Page> ordered = pages.stream()//
                .sorted(Comparator.comparingInt(it -> it.start))//
                .collect(Collectors.toList());

        int currentStart = ordered.get(0).start;
        int currentEnd = currentStart;
        for (Page page : ordered) {
            if (page.start > currentEnd) {
                if (currentEnd > currentStart) {
                    combined.add(new Page(currentStart, currentEnd - currentStart));
                }

                currentStart = page.start;
                currentEnd = page.end;
            } else {
                currentEnd = Math.max(currentEnd, page.end);
            }
        }
        if (currentEnd > currentStart) {
            combined.add(new Page(currentStart, currentEnd - currentStart));
        }

        //
        return combined.stream()//
                .sorted(Comparator.comparingInt(it -> it.start)).toArray(Page[]::new);
    }
}
