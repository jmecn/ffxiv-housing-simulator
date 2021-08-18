package ffxiv.housim.saintcoinach.graphics;

import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.math.XivColor;

public class ColorMap {

    public XivColor[] colors;

    public ColorMap(PackFile file) {

        byte[] data = file.getData();
        this.colors = new XivColor[data.length / 4];

        for (var i = 0; i < data.length; i += 4) {
            byte r = data[i];
            byte g = data[i + 1];
            byte b = data[i + 2];
            byte a = data[i + 3];
            this.colors[i / 4] = new XivColor(a, r, g, b);
        }
    }
}
