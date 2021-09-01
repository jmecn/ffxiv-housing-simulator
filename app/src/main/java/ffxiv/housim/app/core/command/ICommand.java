package ffxiv.housim.app.core.command;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/9/1
 */
public interface ICommand {

    void execute();

    void undo();
}