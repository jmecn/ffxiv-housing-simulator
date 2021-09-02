package ffxiv.housim.ui.gui;

import com.jme3.math.Vector3f;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GridPanel;
import com.simsilica.lemur.ListBox;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.component.BorderLayout;
import com.simsilica.lemur.component.InsetsComponent;
import com.simsilica.lemur.core.VersionedList;
import ffxiv.housim.ui.lemur.window.JmeWindow;
import org.checkerframework.checker.units.qual.C;

/**
 * desc: 家具目录
 *
 * @author yanmaoyuan
 * @date 2021/9/1
 */
public class FurnitureCatalog extends JmeWindow {

    public FurnitureCatalog() {
        super("Furniture");
        setContent(getMainPanel());
    }

    private Panel getMainPanel() {
        Container main = new Container();
        main.setLayout(new BorderLayout());

        VersionedList<String> list = new VersionedList<>();
        list.add("南洋拐沙发");
        ListBox<String> listBox = new ListBox<>(list);
        listBox.setPreferredSize(new Vector3f(300, 500, 1));

        Panel panel = new Panel(400, 500);
        panel.setBorder(new InsetsComponent(3, 1, 3, 3));
        Panel left = new Panel(200, 500);
        left.setBorder(new InsetsComponent(3, 3, 3, 1));
        main.addChild(listBox, BorderLayout.Position.East);
        main.addChild(left, BorderLayout.Position.West);

        return main;
    }
}
