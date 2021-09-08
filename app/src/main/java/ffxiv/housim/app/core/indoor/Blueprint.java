package ffxiv.housim.app.core.indoor;

import ffxiv.housim.app.core.command.ICommand;
import ffxiv.housim.app.core.enums.HouseType;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.UUID;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/9/1
 */
@Getter
@Setter
public class Blueprint {

    private UUID uuid;

    enum Area {
        wil, sea, fst, est
    }

    public HouseType houseType = HouseType.Small;

    public Area area = Area.wil;

    private boolean has2nd = false;
    private Interior _2nd = null;
    private boolean has1st = true;
    private Interior _1st = new Interior();
    private boolean hasBase = true;
    private Interior _base = new Interior();

    public int limit = 200;

    private LinkedList<Furniture> list = new LinkedList<>();

    private LinkedList<ICommand> history = new LinkedList<>();

    private LinkedList<ICommand> redo = new LinkedList<>();
}