package ffxiv.housim.saintcoinach.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ByteBufferStr {

    private final static int INIT_LEN = 67;

    public static String getString(ByteBuffer buffer, int offset) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(INIT_LEN);
        int limit = buffer.limit();

        if (offset < 0 || offset > limit) {
            log.warn("Invalid offset:{}", offset);
        } else {
            for (int ptr = offset; ptr >= 0 && ptr < limit; ptr++) {
                byte val = buffer.get(ptr);
                if (val == 0) {
                    break;
                } else {
                    bos.write(val);
                }
            }
        }

        int size = bos.size();
        if (size == 0) {
            return "";
        }

        if (INIT_LEN < size) {
            log.warn("init lens enlarged to:{}", size);
        }

        return bos.toString(StandardCharsets.UTF_8);
    }
}
