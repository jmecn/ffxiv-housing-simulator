package ffxiv.housim.app.indoor;

import lombok.NonNull;

import java.util.LinkedList;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/9/1
 */
public class Unite {

    private Long id;

    private Long mainId;

    private LinkedList<Long> ids;

    public Unite(@NonNull Long id, @NonNull Long mainId) {
        this.id = id;
        this.mainId = mainId;

        ids = new LinkedList<>();
        ids.add(mainId);
    }

    public void add(@NonNull Long objId) {
        if (ids.contains(objId)) {
            return;
        }

        ids.add(objId);
    }

    public void remove(@NonNull Long objId) {

        if (ids.remove(objId)) {

            if (mainId.longValue() == objId.longValue()) {
                // TODO
                if (ids.isEmpty()) {
                    mainId = null;
                } else {
                    mainId = ids.getFirst();
                }
            }
        }
    }
}