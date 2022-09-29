package ffxiv.housim.saintcoinach.db.xiv.entity;

import ffxiv.housim.saintcoinach.db.ex.relational.IRelationalRow;
import ffxiv.housim.saintcoinach.db.xiv.IXivSheet;
import ffxiv.housim.saintcoinach.db.xiv.XivRow;

import java.util.ArrayList;
import java.util.List;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2022/9/28
 */
public class ClassJobCategory extends XivRow {

    private static final int COLUMN_OFFSET = 1;

    private List<ClassJob> classJobs;

    public ClassJobCategory(IXivSheet sheet, IRelationalRow sourceRow) {
        super(sheet, sourceRow);
    }

    public String getName() {
        return asString("Name");
    }

    public List<ClassJob> getClassJobs() {
        if (classJobs == null) {
            classJobs = buildClassJobs();
        }
        return classJobs;
    }

    private List<ClassJob> buildClassJobs() {
        ArrayList<ClassJob> classJobs = new ArrayList<>();
        IXivSheet<ClassJob> sheet = getSheet().getCollection().getSheet(ClassJob.class);
        for (ClassJob classJob : sheet) {
            Boolean isValid = (Boolean) get(COLUMN_OFFSET + classJob.getKey());
            if (isValid) {
                classJobs.add(classJob);
            }
        }
        return classJobs;
    }

    @Override
    public String toString() {
        return getName();
    }
}
