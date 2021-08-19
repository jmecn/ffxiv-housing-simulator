package ffxiv.housim.saintcoinach.graphics.model;

import java.nio.ByteBuffer;

public class ModelStruct6 {// 5 in hsl
    public int unknown;
    public int unknownStruct4Count;// 6 in hsl
    public int unknownStruct4Offset;

    ModelStruct6(ByteBuffer buffer) {
        unknown = buffer.getInt();
        unknownStruct4Count = buffer.getInt();
        unknownStruct4Offset = buffer.getInt();
    }
}
