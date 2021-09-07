package ffxiv.housim.db.entity;

import lombok.Data;

@Data
public class Preference {
    private String key;
    private String content;
}