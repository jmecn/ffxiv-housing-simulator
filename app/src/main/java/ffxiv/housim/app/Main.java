package ffxiv.housim.app;


import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        Config cfg = Config.INSTANCE;

        boolean isGameDirValid;
        try {
            cfg.validate();
            isGameDirValid = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            isGameDirValid = false;
        }

        if (!isGameDirValid) {

        }
    }
}
