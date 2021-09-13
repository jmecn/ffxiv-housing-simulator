package ffxiv.housim.app.es;

import com.simsilica.es.EntityComponent;
import lombok.Getter;

public class Model implements EntityComponent {

    @Getter
    private final String path;

    public Model(String path) {
        this.path = path;
    }
}
