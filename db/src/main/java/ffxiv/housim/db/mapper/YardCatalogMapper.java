package ffxiv.housim.db.mapper;

import ffxiv.housim.db.XivDatabase;
import ffxiv.housim.db.annotations.Schema;
import ffxiv.housim.db.entity.FurnitureCatalog;
import ffxiv.housim.db.entity.YardCatalog;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * Furniture Catalog
 */
@Schema(XivDatabase.FFXIV)
public interface YardCatalogMapper {
    int saveAll(@Param("list")Collection<YardCatalog> list);
}
