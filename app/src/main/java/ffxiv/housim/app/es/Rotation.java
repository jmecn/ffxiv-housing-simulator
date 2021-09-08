package ffxiv.housim.app.es;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityComponent;
import lombok.Getter;

public class Rotation implements EntityComponent {
    @Getter
    private Quaternion rotation;

    public Rotation(float angleY) {
        rotation = new Quaternion().fromAngleAxis(angleY, Vector3f.UNIT_Y);
    }
}
