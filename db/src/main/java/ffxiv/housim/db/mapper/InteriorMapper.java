package ffxiv.housim.db.mapper;

import ffxiv.housim.db.XivDatabase;
import ffxiv.housim.db.annotations.Schema;
import ffxiv.housim.db.entity.Furniture;
import ffxiv.housim.db.entity.Interior;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * Housing Interior
 */
@Schema(XivDatabase.FFXIV)
public interface InteriorMapper {
    int saveAll(@Param("list") Collection<Interior> list);
}
