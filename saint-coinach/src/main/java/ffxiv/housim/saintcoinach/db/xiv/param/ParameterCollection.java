package ffxiv.housim.saintcoinach.db.xiv.param;

import ffxiv.housim.saintcoinach.db.xiv.entity.BaseParam;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ParameterCollection implements Iterable<Parameter> {

    private Map<BaseParam, Parameter> parameters = new HashMap<>();

    public Collection<Parameter> getParameters() {
        return parameters.values();
    }

    public void addParameterValue(BaseParam baseParam, ParameterValue value) {
        Parameter param = parameters.get(baseParam);
        if (param == null) {
            parameters.put(baseParam, param = new Parameter(baseParam));
        }

        param.addValue(value);
    }

    public void addRange(Collection<Parameter> other) {
        for (Parameter p : other) {
            for (ParameterValue v : p) {
                addParameterValue(p.getBaseParam(), v);
            }
        }
    }

    public Iterator<Parameter> iterator() {
        return parameters.values().iterator();
    }
}
