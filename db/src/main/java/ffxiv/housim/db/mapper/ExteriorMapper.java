package ffxiv.housim.db.mapper;

import ffxiv.housim.db.XivDatabase;
import ffxiv.housim.db.annotations.Schema;
import ffxiv.housim.db.entity.Exterior;
import ffxiv.housim.db.entity.Interior;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * Housing Interior
 */
@Schema(XivDatabase.FFXIV)
public interface ExteriorMapper {
    int saveAll(@Param("list") Collection<Exterior> list);
}
