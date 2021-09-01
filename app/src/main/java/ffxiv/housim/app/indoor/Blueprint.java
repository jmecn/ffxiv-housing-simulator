package ffxiv.housim.app.indoor;

import ffxiv.housim.app.core.command.ICommand;

import java.util.LinkedList;
import java.util.UUID;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/9/1
 */
public class Blueprint {

    private UUID uuid;

    enum HouseType {
        Room, Small, Medium, Large
    }

    enum Area {
        fst, wil, sea, est,
    }

    private HouseType houseType;

    private Area area;

    private Interior second;

    private Interior first;

    private Interior basement;

    private int limit;

    private LinkedList<Furniture> list = new LinkedList<>();

    private LinkedList<ICommand> history = new LinkedList<>();

    private LinkedList<ICommand> redo = new LinkedList<>();
}