package ffxiv.housim.db.entity;

import lombok.Data;

/**
 * 亭具目录
 *
 * @author yanmaoyuan
 * @date 2021/9/6
 */
@Data
public class YardCatalog {
    private int id;
    private int category;
    private String name;
    private int order;
}
