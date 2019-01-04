package com.offz.spigot.custommobs.Mobs;

import com.offz.spigot.custommobs.Behaviours.WalkingBehaviour;
import com.offz.spigot.custommobs.Mobs.Type.MobType;
import net.minecraft.server.v1_13_R2.EntityZombie;
import net.minecraft.server.v1_13_R2.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_13_R2.World;
import org.bukkit.entity.Zombie;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Neritantan extends EntityZombie {
    public Neritantan(World world) {
        super(world);
        Zombie neritantan = (Zombie) this.getBukkitEntity();

        this.setBaby(true);

        neritantan.setMaxHealth(10);
        this.setHealth(10);

        this.targetSelector.a(0, new PathfinderGoalNearestAttackableTarget<Fuwagi>(this, Fuwagi.class, true));

        this.addScoreboardTag("customMob");
        neritantan.setCustomName("Neritantan");
        this.setCustomNameVisible(false);
        this.setSilent(true);
        this.setNoAI(false);

        neritantan.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, true));

        MobType type = MobType.getRegisteredMobType(neritantan);
        WalkingBehaviour.registerMob(neritantan, type, type.getModelID());

        this.getWorld().addEntity(this);
    }
}
