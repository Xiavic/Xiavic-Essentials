package com.github.xiavic.essentials.Utils;

import com.github.xiavic.essentials.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtils {

    public static Location getLocation(String locName) {
        String test = Main.mainConfig.getString(locName);
        String[] list = test.split(",");
        World world = Bukkit.getWorld(list[0]);
        double x = Double.parseDouble(list[1]);
        double y = Double.parseDouble(list[2]);
        double z = Double.parseDouble(list[3]);

        float yaw = Float.parseFloat(list[4]);
        float pitch = Float.parseFloat(list[5]);

        return new Location(world, x, y, z, yaw, pitch);
    }

}
