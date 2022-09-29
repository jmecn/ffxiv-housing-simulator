package ffxiv.housim.saintcoinach.db.libra;

import lombok.Getter;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/29
 */
public class BNpcName {
    @Getter
    private long key;
    public long getNameKey() { return key % 10000000000L; }
    public long getBaseKey() { return key / 10000000000L; }
}
