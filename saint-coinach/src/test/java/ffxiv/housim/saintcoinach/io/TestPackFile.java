package ffxiv.housim.saintcoinach.io;

import static org.junit.Assert.*;

import ffxiv.housim.saintcoinach.SqPack;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Slf4j
public class TestPackFile {

    private String dataDir;

    @Before
    public void getGameDir() {
        String gameDir = System.getenv("FFXIV_HOME");
        assertNotNull(gameDir);

        SqPack sqPack = new SqPack(gameDir);
        dataDir = sqPack.getPackDir();
    }

    @Test
    public void testBinaryFile() {
        String file = "common/font/axis_12_lobby.fdt";

        PackCollection collection = new PackCollection(dataDir);
        PackFile packFile = collection.tryGetFile(file);
        byte[] data = packFile.getData();

        assertEquals(122400, data.length);
    }

    @Test
    public void testImageFile() {
        String file = "common/graphics/texture/-fresnel.tex";

        PackCollection collection = new PackCollection(dataDir);
        PackFile packFile = collection.tryGetFile(file);

        byte[] data = packFile.getData();

        assertEquals(4096, data.length);
    }

    @Test
    public void testImageFile2() {
        String file = "bgcommon/hou/dyna/opt/wl/0002/texture/opt_wl_m0002_0a_d.tex";

        PackCollection collection = new PackCollection(dataDir);
        PackFile packFile = collection.tryGetFile(file);

        byte[] data = packFile.getData();

        assertEquals(174776, data.length);
    }

    @Test
    public void testModelFile() {
        String file = "bgcommon/hou/common/general/0000/bgparts/com_b0_m0000.mdl";

        PackCollection collection = new PackCollection(dataDir);
        PackFile packFile = collection.tryGetFile(file);

        byte[] data = packFile.getData();

        assertEquals(66986, data.length);
    }
}
