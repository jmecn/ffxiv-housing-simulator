package ffxiv.housim.saintcoinach.db.xiv.entity.housing;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;

public class FurnitureCatalogCategory extends XivRow {

    public FurnitureCatalogCategory(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public String getCategory() {
        return asString("Category");// Name
    }

    public short getHousingItemCategory() {
        return asInt16("HousingItemCategory");// 12-家具 13-桌台 14-桌上 15-壁挂 16-地毯
    }

    public short getOrder() {
        return asInt16("Order");
    }

    @Override
    public String toString() {
        return getCategory();
    }
}
