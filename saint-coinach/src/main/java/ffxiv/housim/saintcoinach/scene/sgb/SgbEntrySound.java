package ffxiv.housim.saintcoinach.scene.sgb;

import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.math.Vector3;
import ffxiv.housim.saintcoinach.math.Vector4;
import ffxiv.housim.saintcoinach.sound.ScdFile;
import ffxiv.housim.saintcoinach.utils.ByteBufferStr;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

@Slf4j
public class SgbEntrySound implements ISgbEntry {

    // size 0xCC = 202 bytes
    @Getter
    private SgbEntryType type;
    @Getter
    private int gimmickId;
    private int nameOffset;
    @Getter
    private Vector3 translation;
    @Getter
    private Vector3 rotation;
    @Getter
    private Vector3 scale;
    private int unknonw1;
    private int scdNameOffset;
    private int unknonw2;
    private int unknonw3;
    private int unknonw4;
    private int unknonw5;
    private int unknonw6;
    private short unknonw7;
    private short unknonw8;
    private int unknonw9;
    private int unknonw10;
    private int unknonw11;

    private Vector4 rotateVec1;// alawyas 1,1,1,1
    private Vector4 transVec2;// same as translation
    private Vector4 scaleVec3;// looks like changing with scale
    private Vector4 unknownVec1;
    private Vector4 unknownVec2;

    // following 32 0x00 for padding

    @Getter
    private final String name;
    @Getter
    private final String scdFilePath;
    @Getter
    private ScdFile scdFile;

    public SgbEntrySound(PackCollection packs, ByteBuffer buffer, int offset) {
        buffer.position(offset);

        // read data
        type = SgbEntryType.of(buffer.getInt());
        gimmickId = buffer.getInt();
        nameOffset = buffer.getInt();
        translation = new Vector3(buffer);
        rotation = new Vector3(buffer);
        scale = new Vector3(buffer);

        unknonw1 = buffer.getInt();
        scdNameOffset = buffer.getInt();
        unknonw2 = buffer.getInt();
        unknonw3 = buffer.getInt();

        unknonw4 = buffer.getInt();
        unknonw5 = buffer.getInt();
        unknonw6 = buffer.getInt();
        unknonw7 = buffer.getShort();
        unknonw8 = buffer.getShort();

        unknonw9 = buffer.getInt();
        unknonw10 = buffer.getInt();
        unknonw11 = buffer.getInt();

        rotateVec1 = new Vector4(buffer);
        transVec2 = new Vector4(buffer);
        scaleVec3 = new Vector4(buffer);
        unknownVec1 = new Vector4(buffer);
        unknownVec2 = new Vector4(buffer);

        // skip 32 padding

        // read name
        name = ByteBufferStr.getString(buffer, offset + nameOffset);
        scdFilePath = ByteBufferStr.getString(buffer, offset + scdNameOffset);
        if (!scdFilePath.isEmpty()) {
            PackFile file = packs.tryGetFile(scdFilePath);
            if (file != null) {
                scdFile = new ScdFile(file);
            }
        }
    }

    private static float eaxDbToAmp(float eaxDb){
        float dB = eaxDb / 2000f;
        return (float) Math.pow(10f, dB);
    }
}
