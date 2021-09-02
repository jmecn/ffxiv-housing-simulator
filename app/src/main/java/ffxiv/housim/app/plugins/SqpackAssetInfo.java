package ffxiv.housim.app.plugins;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import ffxiv.housim.saintcoinach.io.PackFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/9/2
 */
public class SqpackAssetInfo extends AssetInfo {

    private PackFile file;

    public SqpackAssetInfo(AssetManager manager, AssetKey key, PackFile file) {
        super(manager, key);
        this.file = file;
    }

    @Override
    public InputStream openStream() {
        return new ByteArrayInputStream(file.getData());
    }

    public ByteBuffer getByteBuffer() {
        byte[] bytes = file.getData();

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        return buffer;
    }

    public PackFile getPackFile() {
        return file;
    }

}
