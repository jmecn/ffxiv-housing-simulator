package ffxiv.housim.saintcoinach.ex;

public enum ColumnType {
    XivString,
    Bool,
    SignedByte,
    Byte,
    Int16,
    UInt16,
    Int32,
    UInt32,
    SingleFloat,
    Int64_OR_Quad,
    MaskedBool_01,
    MaskedBool_02,
    MaskedBool_04,
    MaskedBool_08,
    MaskedBool_10,
    MaskedBool_20,
    MaskedBool_40,
    MaskedBool_80;

    public static ColumnType of(int type) {
        switch(type) {
            case 0x00:
                return XivString;
            case 0x01:
                return Bool;
            case 0x02:
                return SignedByte;
            case 0x03:
                return Byte;
            case 0x04:
                return Int16;
            case 0x05:
                return UInt16;
            case 0x06:
                return Int32;
            case 0x07:
                return UInt32;
            case 0x09:
                return SingleFloat;
            case 0x0B:
                return Int64_OR_Quad;
            case 0x19:
                return MaskedBool_01;
            case 0x1a:
                return MaskedBool_02;
            case 0x1b:
                return MaskedBool_04;
            case 0x1c:
                return MaskedBool_08;
            case 0x1d:
                return MaskedBool_10;
            case 0x1e:
                return MaskedBool_20;
            case 0x1f:
                return MaskedBool_40;
            case 0x20:
                return MaskedBool_80;
            default:
                throw new IllegalArgumentException("Unknown type " + type);
        }
    }
}
