package ffxiv.housim.db.mapper;

import ffxiv.housim.db.XivDatabase;
import ffxiv.housim.db.annotations.Schema;
import ffxiv.housim.db.entity.FurnitureCatalog;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * Furniture Catalog
 */
@Schema(XivDatabase.FFXIV)
public interface FurnitureCatalogMapper {
    int saveAll(@Param("list")Collection<FurnitureCatalog> list);
}
