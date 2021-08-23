package ffxiv.housim.saintcoinach.db.xiv;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface XivName {

    String value();
}

