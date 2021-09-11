package ffxiv.housim.db.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 家具目录
 *
 * @author yanmaoyuan
 * @date 2021/9/6
 */
@Getter
@Setter
public class FurnitureCatalog {
    private Integer id;
    private Integer category;
    private String name;
    private Integer order;
    @Override
    public String toString() {
        return name;
    }
}
