package ffxiv.housim.saintcoinach.ex.relational.valueconverters;

import com.google.gson.JsonObject;
import ffxiv.housim.saintcoinach.ex.IDataRow;
import ffxiv.housim.saintcoinach.ex.IDataSheet;
import ffxiv.housim.saintcoinach.ex.relational.IValueConverter;
import ffxiv.housim.saintcoinach.ex.relational.definition.SheetDefinition;
import ffxiv.housim.saintcoinach.imaging.IconHelper;
import ffxiv.housim.saintcoinach.imaging.ImageFile;

import java.lang.reflect.Type;

public class IconConverter implements IValueConverter<ImageFile> {
    @Override
    public String getTargetTypeName() {
        return "Image";
    }

    @Override
    public Type getTargetType() {
        return ImageFile.class;
    }

    @Override
    public ImageFile convert(IDataRow row, Object rawValue) {
        int nr = (int) rawValue;
        if (nr <= 0 || nr > 999999) {
            return null;
        }

        IDataSheet<?> sheet = row.getSheet();
        return IconHelper.getIcon(sheet.getCollection().getPackCollection(), sheet.getLanguage(), nr);
    }

    @Override
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "icon");
        return obj;
    }

    @Override
    public void resolveReferences(SheetDefinition sheetDef) {

    }

    public static IconConverter fromJson(JsonObject obj) {
        return new IconConverter();
    }
}
