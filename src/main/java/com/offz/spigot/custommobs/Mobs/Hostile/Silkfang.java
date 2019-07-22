package com.offz.spigot.custommobs.Mobs.Hostile;

import com.offz.spigot.custommobs.Builders.MobBuilder;
import com.offz.spigot.custommobs.Mobs.Behaviours.HitBehaviour;
import com.offz.spigot.custommobs.Mobs.MobDrop;
import com.offz.spigot.custommobs.Mobs.Types.HostileMob;
import com.offz.spigot.custommobs.Pathfinders.PathfinderGoalWalkingAnimation;
import net.minecraft.server.v1_13_R2.World;
import org.bukkit.Material;

public class Silkfang extends HostileMob implements HitBehaviour {
    static MobBuilder builder = new MobBuilder("Silkfang", 26)
            .setDrops(new MobDrop(Material.STRING, 4));

    public Silkfang(World world) {
        super(world, builder);
        this.setSize(2F, 2F);
    }

    @Override
    public void createPathfinders() {
        super.createPathfinders();
        this.goalSelector.a(0, new PathfinderGoalWalkingAnimation(this, builder.getModelID()));
    }
}