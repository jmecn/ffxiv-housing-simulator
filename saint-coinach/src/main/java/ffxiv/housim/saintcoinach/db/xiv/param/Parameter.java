package ffxiv.housim.saintcoinach.db.xiv.param;

import ffxiv.housim.saintcoinach.db.xiv.entity.BaseParam;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// TODO
public class Parameter implements Iterable<ParameterValue> {
    @Getter
    private List<ParameterValue> values = new ArrayList<>();
    @Getter
    private BaseParam baseParam;

    public Parameter(BaseParam baseParam) {
        this.baseParam = baseParam;
    }

    public void addValue(ParameterValue value) {
        values.add(value);
    }

    @Override
    public Iterator<ParameterValue> iterator() {
        return values.iterator();
    }
}
