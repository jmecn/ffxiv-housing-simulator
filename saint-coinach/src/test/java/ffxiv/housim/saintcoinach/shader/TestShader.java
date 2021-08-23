package ffxiv.housim.saintcoinach.shader;

import ffxiv.housim.saintcoinach.material.shpk.ShPkFile;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import org.junit.Before;
import org.junit.Test;

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
    public void testBg() {
        PackFile file = packs.tryGetFile("shader/shpk/bg.shpk");
        ShPkFile shpk = new ShPkFile(file);
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
