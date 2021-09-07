package ffxiv.housim.db.mapper;

import ffxiv.housim.db.XivDatabase;
import ffxiv.housim.db.annotations.Schema;
import org.apache.ibatis.annotations.Param;

/**
 * Preference
 */
@Schema({XivDatabase.CACHE, XivDatabase.FFXIV})
public interface PreferenceMapper {

    String get(@Param("key") String key);
    void put(@Param("key") String key, @Param("value") String value);
}