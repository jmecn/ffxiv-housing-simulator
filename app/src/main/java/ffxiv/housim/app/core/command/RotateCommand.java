package ffxiv.housim.app.core.command;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/9/1
 */
public class RotateCommand implements ICommand{

    private Long id;

    private Integer objId;

    private float rotateY;// in rad

    @Override
    public void execute() {

    }

    @Override
    public void undo() {

    }
}
