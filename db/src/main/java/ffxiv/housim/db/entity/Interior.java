package ffxiv.housim.db.entity;

import lombok.Data;

@Data
public class Interior {
    private int id;
    private String name;
    private int itemId;
    private int category;
    private int order;
    private String path;
    private String icon;
}
