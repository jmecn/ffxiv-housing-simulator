package ffxiv.housim.db.mapper;

import ffxiv.housim.db.XivDatabase;
import ffxiv.housim.db.annotations.Schema;
import ffxiv.housim.db.entity.Furniture;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * Furniture
 */
@Schema(XivDatabase.FFXIV)
public interface FurnitureMapper {
    int saveAll(@Param("list") Collection<Furniture> list);
}
