package ffxiv.housim.saintcoinach.io;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestPackIdentifier {

    @Test
    public void test000000() {
        PackIdentifier id = PackIdentifier.tryGet("common/graphics/texture");
        assertEquals("common", id.getType());
        assertEquals(0, id.getTypeKey());
        assertEquals("ffxiv", id.getExpansion());
        assertEquals(0, id.getExpansionKey());
        assertEquals(0, id.getNumber());
        System.out.println(id);
    }

    @Test
    public void test010103() {
        PackIdentifier id = PackIdentifier.tryGet("bgcommon/ex1/03_/texture");
        assertEquals("bgcommon", id.getType());
        assertEquals(1, id.getTypeKey());
        assertEquals("ex1", id.getExpansion());
        assertEquals(1, id.getExpansionKey());
        assertEquals(3, id.getNumber());
        System.out.println(id);
    }
}
