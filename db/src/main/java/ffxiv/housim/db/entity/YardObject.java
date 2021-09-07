package ffxiv.housim.db.entity;

import lombok.Data;

/**
 * 亭具
 *
 * @author yanmaoyuan
 * @date 2021/9/6
 */
@Data
public class YardObject {
    private int id;
    private int itemId;
    private String name;
    private String model;
    private String icon;
    private int category;
    private int catalog;
    private int isDyeable;
}