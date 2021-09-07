package ffxiv.housim.db.entity;

import lombok.Data;

@Data
public class Exterior {
    private int id;
    private String name;
    private int itemId;
    private int category;
    private int order;
    private int size;
    private String path;
    private String icon;
    private int isUnited;
}
