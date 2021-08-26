package ffxiv.housim.saintcoinach.shader;

import com.google.common.io.Files;
import ffxiv.housim.saintcoinach.io.*;
import ffxiv.housim.saintcoinach.material.shpk.Parameter;
import ffxiv.housim.saintcoinach.material.shpk.ShPkFile;
import ffxiv.housim.saintcoinach.material.shpk.Shader;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class TestShader {

    PackCollection packs;

    @Before
    public void getGameDir() {
        String gameDir = System.getenv("FFXIV_HOME");
        assertNotNull(gameDir);
        packs = new PackCollection(gameDir + "/game/sqpack");
    }

    @Test
    public void testIris() {
        PackFile file = packs.tryGetFile("shader/shpk/iris.shpk");
        ShPkFile shpk = new ShPkFile(file);
    }

    @Test
    public void testHair() {
        PackFile file = packs.tryGetFile("shader/shpk/hair.shpk");
        ShPkFile shpk = new ShPkFile(file);
    }

    @Test
    public void testSkin() {
        PackFile file = packs.tryGetFile("shader/shpk/skin.shpk");
        ShPkFile shpk = new ShPkFile(file);
    }

    @Test
    public void testCrystal() {
        PackFile file = packs.tryGetFile("shader/shpk/crystal.shpk");
        ShPkFile shpk = new ShPkFile(file);
    }

    @Test
    public void testCharacter() {
        PackFile file = packs.tryGetFile("shader/shpk/character.shpk");
        ShPkFile shpk = new ShPkFile(file);
    }

    @Test
    public void testLightShaft() {
        PackFile file = packs.tryGetFile("shader/shpk/lightshaft.shpk");
        ShPkFile shpk = new ShPkFile(file);
    }

    @Test
    public void testLineLighting() {
        PackFile file = packs.tryGetFile("shader/shpk/linelighting.shpk");
        ShPkFile shpk = new ShPkFile(file);
    }

    @Test
    public void testPlaneLighting() {
        PackFile file = packs.tryGetFile("shader/shpk/planelighting.shpk");
        ShPkFile shpk = new ShPkFile(file);
    }

    @Test
    public void testPointLighting() {
        PackFile file = packs.tryGetFile("shader/shpk/pointlighting.shpk");
        ShPkFile shpk = new ShPkFile(file);
    }

    @Test
    public void testCloud() {
        PackFile file = packs.tryGetFile("shader/shpk/cloud.shpk");
        ShPkFile shpk = new ShPkFile(file);
    }

    @Test
    public void testWeather() {
        PackFile file = packs.tryGetFile("shader/shpk/weather.shpk");
        ShPkFile shpk = new ShPkFile(file);
    }

    @Test
    public void testRiver() {
        PackFile file = packs.tryGetFile("shader/shpk/river.shpk");
        ShPkFile shpk = new ShPkFile(file);
    }

    @Test
    public void testGrass() {
        PackFile file = packs.tryGetFile("shader/shpk/grass.shpk");
        ShPkFile shpk = new ShPkFile(file);
    }

    @Test
    public void testBgDx11() {

        PackIdentifier id = new PackIdentifier(05, 00, 00);
        Pack pack = packs.tryGetPack(id);
        IPackSource source = pack.getSource();

        PackDirectory dir = source.tryGetDirectory(0xE3466A82);

        PackFile file = dir.tryGetFile("bg.shpk");
        ShPkFile shpk = new ShPkFile(file);

        for ( int pid = 0; pid < shpk.getPixelShaderCount(); pid+= 5) {
            Shader ps = shpk.getPixelShader(pid);
            byte[] psd = shpk.getDXBC(ps);
            try {
                Files.write(psd, new File("ps-dx11-" + pid + ".dxbc"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void testBg() {
        PackFile file = packs.tryGetFile("shader/shpk/bg.shpk");
        ShPkFile shpk = new ShPkFile(file);
        Shader vs = shpk.getVertexShader(0);
        Shader ps = shpk.getPixelShader(0);
        byte[] vsd = shpk.getDXBC(vs);
        byte[] psd = shpk.getDXBC(ps);
        try {
            Files.write(vsd, new File("vs-0.dxbc"));
            Files.write(psd, new File("ps-0.dxbc"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBgColorChange() {
        PackFile file = packs.tryGetFile("shader/shpk/bgcolorchange.shpk");
        ShPkFile shpk = new ShPkFile(file);
    }

    @Test
    public void testBgCrestChange() {
        PackFile file = packs.tryGetFile("shader/shpk/bgcrestchange.shpk");
        ShPkFile shpk = new ShPkFile(file);
    }

    @Test
    public void testBgUvScroll() {
        PackFile file = packs.tryGetFile("shader/shpk/bguvscroll.shpk");
        ShPkFile shpk = new ShPkFile(file);
    }

}
