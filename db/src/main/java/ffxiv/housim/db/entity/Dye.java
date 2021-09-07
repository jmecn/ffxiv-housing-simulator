package ffxiv.housim.db.entity;

import lombok.Data;

@Data
public class Dye {
    int id;
    int itemId;
    String name;

    int shade;
    int order;

    int red;
    int green;
    int blue;
}
