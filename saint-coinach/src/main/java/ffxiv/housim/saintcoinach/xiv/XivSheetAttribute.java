package ffxiv.housim.saintcoinach.xiv;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface XivSheetAttribute {
    String sheetName();
}

