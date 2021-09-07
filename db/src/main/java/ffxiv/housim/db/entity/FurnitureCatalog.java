package ffxiv.housim.db.entity;

import lombok.Data;

/**
 * 家具目录
 *
 * @author yanmaoyuan
 * @date 2021/9/6
 */
@Data
public class FurnitureCatalog {
    private int id;
    private int category;
    private String name;
    private int order;
}
