package ffxiv.housim.saintcoinach.ex.relational.definition;

import com.google.gson.JsonObject;
import ffxiv.housim.saintcoinach.ex.IDataRow;

import java.lang.reflect.Type;

public interface IDataDefinition extends Cloneable {

    int getLength();

    /**
     *
     * @param row The row to convert a value of.
     * @param value Raw value as read from the file.
     * @param index Index in the definition on which the method is called.
     * @return The converted value
     */
    Object convert(IDataRow row, Object value, int index);

    /**
     *
     * @param index Index in the definition on which the method is called.
     * @return The name
     */
    String getName(int index);

    /**
     *
     * @param index Index in the definition on which the method is called.
     * @return The value type name.
     */
    String getValueTypeName(int index);

    /**
     *
     * @param index Index in the definition on which the method is called.
     * @return The value type.
     */
    Type getValueType(int index);

    IDataDefinition clone();

    JsonObject toJson();

    void resolveReferences(SheetDefinition sheetDef);
}