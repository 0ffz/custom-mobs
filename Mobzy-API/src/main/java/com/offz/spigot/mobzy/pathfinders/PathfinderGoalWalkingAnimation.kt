package com.offz.spigot.mobzy.pathfinders

import com.offz.spigot.mobzy.damage
import com.offz.spigot.mobzy.editItemMeta
import org.bukkit.entity.LivingEntity

class PathfinderGoalWalkingAnimation(val mob: LivingEntity, private val modelID: Int) : PathfinderGoal() {
    private val model
        get() = mob.equipment!!.helmet!!

    override fun execute() {
        mob.equipment!!.helmet = model.editItemMeta {
            it.damage = modelID + 1
        }
    }

    override fun init() {
    }

    override fun reset() {

    }

    override fun shouldExecute(): Boolean {
        val velocity = mob.velocity
        return model.damage != modelID + 2 && !(velocity.x in -0.001..0.001 && velocity.z in -0.001..0.001)
    }

    override fun shouldTerminate(): Boolean {
        if (!shouldExecute() && model.damage != modelID + 2)
            mob.equipment!!.helmet = model.editItemMeta {
                it.damage = modelID
            }
        return false
    }

}