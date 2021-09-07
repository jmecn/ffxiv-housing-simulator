package ffxiv.housim.db.mapper;

import ffxiv.housim.db.XivDatabase;
import ffxiv.housim.db.annotations.Schema;
import ffxiv.housim.db.entity.Furniture;
import ffxiv.housim.db.entity.YardObject;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * Yard Object
 */
@Schema(XivDatabase.FFXIV)
public interface YardObjectMapper {
    int saveAll(@Param("list") Collection<YardObject> list);
}
