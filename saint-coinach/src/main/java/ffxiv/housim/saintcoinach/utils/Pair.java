package ffxiv.housim.saintcoinach.utils;

import lombok.Getter;

public class Pair<K, V> {
    @Getter
    K key;
    @Getter
    V val;

    public Pair(K k, V v) {
        key = k;
        val = v;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "key=" + key +
                ", val=" + val +
                '}';
    }

    @Override
    public int hashCode() {
        return key.hashCode() * 13 + val.hashCode();
    }
}