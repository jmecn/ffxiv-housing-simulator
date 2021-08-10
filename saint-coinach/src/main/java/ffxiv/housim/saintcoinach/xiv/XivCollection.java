package ffxiv.housim.saintcoinach.xiv;

import ffxiv.housim.saintcoinach.ex.relational.RelationalExCollection;
import ffxiv.housim.saintcoinach.io.PackCollection;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class XivCollection extends RelationalExCollection {

    private Map<String, Type> sheetNameToTypeMap = new ConcurrentHashMap<>();

    public XivCollection(PackCollection packCollection) {
        this(packCollection, null);
    }

    public XivCollection(PackCollection packCollection, File libraDatabase) {
        super(packCollection);

        if (libraDatabase != null && libraDatabase.exists()) {

        }
    }

    public <T extends IXivRow> IXivSheet<T> getSheet(Class<T> t) {
        String name = t.getSimpleName();
        log.info("get sheet:{}", name);
        return getSheet(name, t);
    }

    public <T extends IXivRow> IXivSheet<T> getSheet(String name, Class<T> t) {
        return (IXivSheet<T>) getSheet(name);
    }
}
