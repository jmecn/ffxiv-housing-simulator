package ffxiv.housim.saintcoinach.io;

import org.junit.Assert;
import org.junit.Test;

public class TestHash {

    @Test
    public void testFolderHash() {
        int hash = Hash.compute("common/graphics/texture");
        Assert.assertEquals(hash, 0x0686C56E);
    }

    @Test
    public void testFileNameHash() {
        int hash = Hash.compute("-fresnel.tex");
        Assert.assertEquals(hash, 0xF671F5B0);
    }
}