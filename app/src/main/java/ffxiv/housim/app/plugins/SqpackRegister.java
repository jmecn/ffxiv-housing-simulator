package ffxiv.housim.app.plugins;

import com.jme3.asset.AssetManager;
import ffxiv.housim.app.Constants;
import ffxiv.housim.app.plugins.loader.ScdLoader;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/9/2
 */
public class SqpackRegister {

    public static void register(AssetManager assetManager, String gameDir) {
        assetManager.registerLocator(gameDir, SqpackLocator.class);

        assetManager.registerLoader(ScdLoader.class, "scd");
        assetManager.registerLoader(SqpackLoader.class, "exh", "exd", "tex", "sgb", "lgb", "mtrl", "mdl");
    }
}
