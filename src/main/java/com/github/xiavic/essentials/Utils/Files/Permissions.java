package com.github.xiavic.essentials.Utils.Files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Permissions {

    private static File file;
    private static FileConfiguration permissionsFile;

    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("XiavicCore").getDataFolder(), "/Resources/permissions.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
        }
        permissionsFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return permissionsFile;
    }

    public static void save() {
        try {
            permissionsFile.save(file);
        } catch (IOException e) {
            System.out.println("The permissions.yml File could NOT be saved.");
        }
    }

    public static void reload() {
        permissionsFile = YamlConfiguration.loadConfiguration(file);
    }

}
