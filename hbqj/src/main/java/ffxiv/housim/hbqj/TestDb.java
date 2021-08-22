package ffxiv.housim.hbqj;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TestDb {
    public static void main(String[] args) {
        ByteBuffer bb = ByteBuffer.allocate(8);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        bb.putDouble(0.000000007450580152834618);
        byte[] data = bb.array();
        for (byte b : data) {
            System.out.printf("%02X ", 0xFF & b);
        }
    }
}
