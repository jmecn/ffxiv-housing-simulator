package ffxiv.housim.saintcoinach.db.libra;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/29
 */
public class Entities {
    @Getter
    Set<ENpcResident> eNpcResidents = new HashSet<>();
}
