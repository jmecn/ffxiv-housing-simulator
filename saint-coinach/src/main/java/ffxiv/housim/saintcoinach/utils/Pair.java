package ffxiv.housim.saintcoinach.utils;

public class Pair<K, V> {
    K key;
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