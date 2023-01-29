package hamsteryds.nereusopus.extensions;

import hamsteryds.nereusopus.NereusOpus;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class ExtensionLoader {
    public static File directory_1 = new File(NereusOpus.plugin.getDataFolder() + "/extensions/enchants");
    public static File directory_2 = new File(NereusOpus.plugin.getDataFolder() + "/extensions/functions");

    public static void initialize() {
        if (!directory_1.exists())
            directory_1.mkdirs();
        if (!directory_2.exists())
            directory_2.mkdirs();

        loadEnchantExtensions();
    }

    public static void loadEnchantExtensions() {
        if (directory_1.listFiles() == null) {
            return;
        }
        for (File file : directory_1.listFiles()) {
            try {
                URL url = new URL("file:" + file.getAbsolutePath());
                URLClassLoader classLoader = new URLClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader());
//                System.out.println(List.of(classLoader.get));
//
//                Class clazz = classLoader.loadClass("nereusopus.extension.Main");
//                clazz.newInstance();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
