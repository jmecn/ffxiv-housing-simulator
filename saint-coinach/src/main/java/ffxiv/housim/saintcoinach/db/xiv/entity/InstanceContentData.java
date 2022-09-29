package ffxiv.housim.saintcoinach.db.xiv.entity;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import ffxiv.housim.saintcoinach.db.xiv.XivCollection;

import java.io.Console;
import java.util.Collection;
import java.util.Collections;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/29
 */
public class InstanceContentData implements IItemSource {
    @Override
    public Collection<Item> getItems() {
        return null;
    }

    public class Fight {

    }

    public class RewardItem {

    }

    public class Treasure implements IItemSource {

        @Override
        public Collection<Item> getItems() {
            return null;
        }
    }
    // TODO need implementation
}
