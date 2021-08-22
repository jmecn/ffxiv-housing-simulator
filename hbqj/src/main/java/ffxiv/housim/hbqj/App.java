package ffxiv.housim.hbqj;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Base64;

public class App {
    public static void main(String[] args) throws IOException {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(new FileNameExtensionFilter("海滨墙架存档文件", "hbqj"));
        int ret = chooser.showOpenDialog(null);
        if (ret != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();
        byte[] base64 = Files.readAllBytes(file.toPath());

        byte[] bytes = Base64.getDecoder().decode(base64);

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        System.out.println(file.getParent());
        System.out.println(file.getName());

        File decode = new File(file.getParent() + File.separator + file.getName() + ".dat");
        Files.write(decode.toPath(), bytes, StandardOpenOption.CREATE);
    }
}
