package ffxiv.housim.app.es;

import com.jme3.math.Vector3f;
import com.simsilica.es.EntityComponent;
import lombok.Getter;

public class Position implements EntityComponent {
    @Getter
    private final Vector3f location;

    public Position(Vector3f location) {
        this.location = location;
    }

}
