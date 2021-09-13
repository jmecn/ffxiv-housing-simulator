package ffxiv.housim.db.mapper;

import ffxiv.housim.db.XivDatabase;
import ffxiv.housim.db.annotations.Schema;
import ffxiv.housim.db.entity.Furniture;
import ffxiv.housim.db.entity.FurnitureCatalog;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * Furniture
 */
@Schema(XivDatabase.FFXIV)
public interface FurnitureMapper {
    int saveAll(@Param("list") Collection<Furniture> list);

    List<Furniture> query(@Param("category") Integer category, @Param("catalog") Integer catalog);

    int queryCount(@Param("category") Integer category, @Param("catalog") Integer catalog);
}
