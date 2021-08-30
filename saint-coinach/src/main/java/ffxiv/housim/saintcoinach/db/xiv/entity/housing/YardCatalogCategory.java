package ffxiv.housim.saintcoinach.db.xiv.entity.housing;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;

public class YardCatalogCategory extends XivRow {

    public YardCatalogCategory(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public String getCategory() {
        return asString("Category");// Name
    }

    public short getHousingItemCategory() {
        return asInt16("HousingItemCategory");// 17-庭具
    }

    public short getOrder() {
        return asInt16("Order");
    }

    @Override
    public String toString() {
        return getCategory();
    }
}
