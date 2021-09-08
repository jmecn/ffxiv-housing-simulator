package ffxiv.housim.app.es;

import com.simsilica.es.EntityComponent;
import lombok.Getter;

public class Model implements EntityComponent {
    @Getter
    private final String path;
    @Getter
    private final int type;

    public Model(String path, int type) {
        this.path = path;
        this.type = type;
    }
}
