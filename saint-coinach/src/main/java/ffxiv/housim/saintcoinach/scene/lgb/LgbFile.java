package ffxiv.housim.saintcoinach.scene.lgb;

import ffxiv.housim.saintcoinach.io.PackFile;
import lombok.Getter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/8/30
 */
public class LgbFile {
    @Getter
    private PackFile file;

    private int magic1;     // LGB1
    private int fileSize;
    private int unknown1;
    private int magic2;     // LGP1

    private int unknown2;
    private int unknown3;
    private int unknown4;
    private int unknown5;

    private int groupCount;
    private int[] groupOffsets;

    private LgbGroup[] groups;

    public LgbFile(PackFile file) {
        this.file = file;

        byte[] bytes = file.getData();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        magic1 = buffer.getInt();// LGB1
        fileSize = buffer.getInt();
        unknown1 = buffer.getInt();
        magic2 = buffer.getInt();// LGP1

        if (magic1 != 0x3142474C || magic2 != 0x3150474C) {    // LGB1 & LGP1
            throw new IllegalArgumentException("Not lgb file");
        }

        unknown2 = buffer.getInt();
        unknown3 = buffer.getInt();
        unknown4 = buffer.getInt();
        unknown5 = buffer.getInt();

        groupCount = buffer.getInt();

        int headerOffset = buffer.position();

        groupOffsets = new int[groupCount];
        for (int i = 0; i < groupCount; i++) {
            groupOffsets[i] = buffer.getInt();
        }

        groups = new LgbGroup[groupCount];
        for (int i = 0; i < groupCount; i++) {
            groups[i] = new LgbGroup(this, buffer, headerOffset + groupOffsets[i]);
        }
    }

}
