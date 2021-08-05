package ffxiv.housim.saintcoinach;

import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import org.junit.Test;

import java.io.IOException;

public class TestSqpack {

    @Test
    public void testInit() {
        String gameDir = "/Users/yanmaoyuan/Downloads/ffxiv";
        String file = "common/font/axis_12_lobby.fdt";
        String file2 = "common/graphics/texture/-fresnel.tex";
        String file3 = "common/graphics/texture/_zone001_y.tex";
        String file4 = "bgcommon/hou/dyna/opt/wl/0002/texture/opt_wl_m0002_0a_d.tex";

        SqPack sqPack = new SqPack(gameDir);
        PackCollection collection = new PackCollection(sqPack.getPackDir());
        PackFile packFile = collection.tryGetFile(file);
        byte[] data = packFile.getData();
        System.out.println(data.length);

        packFile = collection.tryGetFile(file2);
        data = packFile.getData();
        System.out.println(data.length);

        packFile = collection.tryGetFile(file3);
        data = packFile.getData();
        System.out.println(data.length);

        packFile = collection.tryGetFile(file4);
        data = packFile.getData();
        System.out.println(data.length);

    }

}