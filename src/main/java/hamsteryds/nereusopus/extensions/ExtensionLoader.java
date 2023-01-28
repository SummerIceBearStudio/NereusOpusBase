package hamsteryds.nereusopus.extensions;

import hamsteryds.nereusopus.NereusOpus;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class ExtensionLoader {
    public static void initialize() {
        File directory = new File(NereusOpus.plugin.getDataFolder() + "/extensions/");
        if (!directory.exists())
            directory.mkdirs();
        if (directory.listFiles() == null) {
            return;
        }
        for (File file : directory.listFiles()) {
            try {
                URL url = new URL("file:" + file.getAbsolutePath());
                URLClassLoader classLoader = new URLClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader());
                Class clazz = classLoader.loadClass("nereusopus.extension.Main");
                clazz.newInstance();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
