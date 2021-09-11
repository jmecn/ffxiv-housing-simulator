package ffxiv.housim.app.plugins;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author yanmaoyuan
 * @date 2021/9/2
 */
@Slf4j
public class SqpackLoader implements AssetLoader {

    @Override
    public Object load(AssetInfo assetInfo) throws IOException {
        if (assetInfo instanceof SqpackAssetInfo sqpack) {
            return sqpack.getPackFile();
        }
        return null;
    }
}
