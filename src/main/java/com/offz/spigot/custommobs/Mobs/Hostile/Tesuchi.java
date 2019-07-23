package com.offz.spigot.custommobs.Mobs.Hostile;

import com.offz.spigot.custommobs.Mobs.Behaviours.HitBehaviour;
import com.offz.spigot.custommobs.Mobs.Types.HostileMob;
import com.offz.spigot.custommobs.Pathfinders.PathfinderGoalWalkingAnimation;
import net.minecraft.server.v1_13_R2.World;

public class Tesuchi extends HostileMob implements HitBehaviour {
    public Tesuchi(World world) {
        super(world, "Tesuchi");
        this.setSize(0.6F, 0.6F);
    }

    @Override
    public void createPathfinders() {
        super.createPathfinders();
        this.goalSelector.a(0, new PathfinderGoalWalkingAnimation(this, getStaticBuilder().getModelID()));
    }
}