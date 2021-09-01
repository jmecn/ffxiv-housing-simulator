package ffxiv.housim.app.core.command;

import ffxiv.housim.saintcoinach.math.Vector3;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/9/1
 */
public class RemoveCommand implements ICommand {
    private Long id;

    private Integer objId;

    private Vector3 translation;

    private float rotateY;

    @Override
    public void execute() {

    }

    @Override
    public void undo() {

    }
}
