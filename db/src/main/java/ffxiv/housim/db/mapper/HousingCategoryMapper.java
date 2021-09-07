package ffxiv.housim.db.mapper;

import ffxiv.housim.db.XivDatabase;
import ffxiv.housim.db.annotations.Schema;
import ffxiv.housim.db.entity.HousingCategory;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * Housing Item Category
 */
@Schema(XivDatabase.FFXIV)
public interface HousingCategoryMapper {

    int saveAll(@Param("list")Collection<HousingCategory> list);
}
