package com.github.xiavic.essentials.Utils.Misc;

import com.github.xiavic.essentials.Main;
import org.bukkit.Material;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public enum everythingElse {

    INSTANCE;

    public static boolean isFrozen(Player player) {
        return player.getWalkSpeed() == 0;
    }

    public void onIGDeath(EntityDeathEvent de) {
        LivingEntity le = de.getEntity();
        if (le instanceof IronGolem) {
            if (!Main.mainConfig.getBoolean("IronGolems.IGRoses")) {
                de.getDrops().remove(Material.POPPY);
            }
            if (!Main.mainConfig.getBoolean("IronGolems.IGIron")) {
                de.getDrops().remove(Material.IRON_INGOT);
            }
            if (Main.mainConfig.getBoolean("IronGolems.CustomIron") && !Main.mainConfig.getBoolean("IronGolems.IGIron")) {
                Random r = new Random();
                int min = Main.mainConfig.getInt("IronGolems.CustomIron.amountMin");
                int max = Main.mainConfig.getInt("IronGolems.CustomIron.amountMax");
                int i = r.nextInt((max - min) + 1) + min;
                de.getDrops().add(i, new ItemStack(Material.IRON_INGOT));
            }
        }
    }
}
