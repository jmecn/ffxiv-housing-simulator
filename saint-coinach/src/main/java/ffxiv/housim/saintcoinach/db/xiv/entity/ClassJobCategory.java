package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;

import java.util.ArrayList;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/28
 */
public class ClassJobCategory extends XivRow {

    private static final int ColumnOffset = 1;

    private ClassJob[] classJobs;

    public ClassJobCategory(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public String getName() {
        return asString("Name");
    }

    public ClassJob[] getClassJobs() {
        if (classJobs == null) {
            classJobs = buildClassJobs();
        }
        return classJobs;
    }

    private ClassJob[] buildClassJobs() {
        ArrayList<ClassJob> cjs = new ArrayList<>();
        IXivSheet<ClassJob> cjSheet = getSheet().getCollection().getSheet(ClassJob.class);
        for (ClassJob cj : cjSheet) {
            Boolean isValid = (Boolean) get(ColumnOffset + cj.getKey());
            if (isValid) {
                cjs.add(cj);
            }
        }

        classJobs = new ClassJob[cjs.size()];
        return cjs.toArray(classJobs);
    }

    @Override
    public String toString() {
        return getName();
    }
}
