package ffxiv.housim.app.plugins;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetLocator;
import com.jme3.asset.AssetManager;
import ffxiv.housim.saintcoinach.io.PackCollection;
import ffxiv.housim.saintcoinach.io.PackFile;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Paths;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/9/2
 */
@Slf4j
public class SqpackLocator implements AssetLocator {

    PackCollection packs;

    @Override
    public void setRootPath(String rootPath) {
        String sqpack = Paths.get(rootPath, "game", "sqpack").toString();
        packs = new PackCollection(sqpack);
    }

    @Override
    public AssetInfo locate(AssetManager manager, AssetKey key) {
        String path = key.getName().toLowerCase();
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        PackFile file = packs.tryGetFile(path);
        if (file != null) {
            log.info("locate: {}", file);
            return new SqpackAssetInfo(manager, key, file);
        }
        return null;
    }
}
