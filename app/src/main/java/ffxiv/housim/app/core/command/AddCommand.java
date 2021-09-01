package ffxiv.housim.app.core.command;

import com.jme3.math.Vector3f;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/9/1
 */
public class AddCommand implements ICommand {

    private Long id;

    private Integer objId;

    private Vector3f translation;

    private float rotateY;

    @Override
    public void execute() {

    }

    @Override
    public void undo() {

    }
}
