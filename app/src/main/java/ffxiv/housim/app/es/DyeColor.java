package ffxiv.housim.app.es;

import com.jme3.math.ColorRGBA;
import com.simsilica.es.EntityComponent;
import lombok.Getter;

public class DyeColor implements EntityComponent {
    @Getter
    private final ColorRGBA color;

    public DyeColor(ColorRGBA color) {
        this.color = color;
    }
}
