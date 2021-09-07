package ffxiv.housim.db.mapper;

import ffxiv.housim.db.XivDatabase;
import ffxiv.housim.db.annotations.Schema;
import ffxiv.housim.db.entity.Version;

import java.util.List;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/9/1
 */
@Schema(XivDatabase.CACHE)
public interface VersionMapper {

    List<Version> selectAll();

    int count();

    void saveAll(List<Version> list);
}
