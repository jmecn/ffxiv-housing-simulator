package ffxiv.housim.app.es.interior;

import com.simsilica.es.EntityComponent;
import ffxiv.housim.app.core.enums.HouseType;
import lombok.Getter;

/**
 * desc: HouseSize
 *
 * @author yanmaoyuan
 * @date 2021/9/13
 */
public class HType implements EntityComponent {
    @Getter
    private final HouseType type;

    public HType(HouseType type) {
        this.type = type;
    }
}
