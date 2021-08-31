package ffxiv.housim.saintcoinach.scene.sgb;

import ffxiv.housim.saintcoinach.io.PackFile;
import lombok.Getter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class SgbFile {

    @Getter
    private PackFile file;
    @Getter
    private ISgbData[] data;

    public SgbFile(PackFile file) {
        this.file = file;
        byte[] bytes = file.getData();

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        int magic1 = buffer.getInt();// SGB1
        int fileSize = buffer.getInt();
        int unknow = buffer.getInt();
        int magic2 = buffer.getInt();// SCN1

        if (magic1 != 0x31424753 || magic2 != 0x314E4353) {    // SGB1 & SCN1
            throw new IllegalArgumentException("Not sgb file");
        }

        List<ISgbData> data = new ArrayList<>();

        int sharedOffset = buffer.getInt(0x14);
        int offset1C = buffer.getInt(0x1C);
        int statesOffset = buffer.getInt(0x24);

        buffer.position(0x58);

        try {
            data.add(new SgbGroup(this, buffer, 0x14 + sharedOffset));
            data.add(new SgbGroup(this, buffer, 0x14 + offset1C, true));
        } catch (Exception e) {

        }
        this.data = data.toArray(new ISgbData[0]);
    }
}
