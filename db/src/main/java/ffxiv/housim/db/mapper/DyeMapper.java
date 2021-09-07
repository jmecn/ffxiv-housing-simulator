package ffxiv.housim.db.mapper;

import ffxiv.housim.db.XivDatabase;
import ffxiv.housim.db.annotations.Schema;
import ffxiv.housim.db.entity.Dye;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

@Schema(XivDatabase.FFXIV)
public interface DyeMapper {

    int saveAll(@Param("list")Collection<Dye> list);
}
