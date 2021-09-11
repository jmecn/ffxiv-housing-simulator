package ffxiv.housim.app.plugins.loader;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetLoader;
import ffxiv.housim.app.factory.TextureFactory;
import ffxiv.housim.app.plugins.SqpackAssetInfo;
import ffxiv.housim.saintcoinach.io.PackFile;
import ffxiv.housim.saintcoinach.texture.ImageFile;
import ffxiv.housim.saintcoinach.utils.IconHelper;

import java.io.IOException;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/9/11
 */
public class TexLoader implements AssetLoader {

    @Override
    public Object load(AssetInfo assetInfo) throws IOException {
        if (assetInfo instanceof SqpackAssetInfo pack) {
            return load(pack.getPackFile(), pack.getKey());
        }
        return null;
    }

    private Object load(PackFile packFile, AssetKey key) {
        ImageFile imageFile = (ImageFile) packFile;
        return TextureFactory.get(imageFile).getImage();
    }
}
