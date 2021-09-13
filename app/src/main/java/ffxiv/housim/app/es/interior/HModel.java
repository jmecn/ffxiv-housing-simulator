package ffxiv.housim.app.es.interior;

import com.simsilica.es.EntityComponent;
import lombok.Getter;

/**
 * desc: HouseModel
 *
 * @author yanmaoyuan
 * @date 2021/9/13
 */
public class HModel implements EntityComponent {
    @Getter
    private final String area;
    @Getter
    private final String path;

    public HModel(String area, String path) {
        this.area = area;
        this.path = path;
    }
}
