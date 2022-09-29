package ffxiv.housim.saintcoinach.db.libra;

import ffxiv.housim.saintcoinach.math.Vector2;
import ffxiv.housim.saintcoinach.utils.Pair;
import lombok.Getter;

import java.util.List;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/29
 */
public class ENpcResident {
    @Getter
    private long key;

    @Getter
    private List<Pair<Integer, List<Vector2>>> coordinates;
}
