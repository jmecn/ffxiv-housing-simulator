package ffxiv.housim.db.entity;

import lombok.Data;

/**
 * Territory
 *
 * @author yanmaoyuan
 * @date 2021/9/6
 */
@Data
public class Terr {
    private int id;
    private String name;// primary key
    private String placeName;
    private String model;
}
