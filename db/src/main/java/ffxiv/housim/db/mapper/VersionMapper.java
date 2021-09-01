package ffxiv.housim.db.mapper;

import ffxiv.housim.db.entity.Version;

import java.util.List;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/9/1
 */
public interface VersionMapper {

    List<Version> selectAll();

    int count();

    void saveAll(List<Version> list);
}
