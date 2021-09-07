package ffxiv.housim.db.mapper;

import ffxiv.housim.db.DBHelper;
import ffxiv.housim.db.XivDatabase;
import ffxiv.housim.db.annotations.Schema;
import ffxiv.housim.db.entity.Terr;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * Terrain
 */
@Schema(XivDatabase.FFXIV)
public interface TerrMapper {
    int saveAll(@Param("list") Collection<Terr> list);
}
