package ffxiv.housim.saintcoinach;

public class Pair<K, V> {
    K key;
    V val;

    public Pair(K k, V v) {
        key = k;
        val = v;
    }

    @Override
    public int hashCode() {
        return key.hashCode() * 13 + val.hashCode();
    }
}