package com.mineinabyss.mobzy.ecs.systems

import com.mineinabyss.geary.ecs.engine.Engine
import com.mineinabyss.geary.ecs.engine.forEach
import com.mineinabyss.geary.ecs.systems.TickingSystem
import com.mineinabyss.geary.minecraft.components.BukkitEntityComponent
import com.mineinabyss.mobzy.api.nms.aliases.toNMS
import com.mineinabyss.mobzy.ecs.components.initialization.Model
import net.minecraft.server.v1_16_R2.EnumItemSlot
import net.minecraft.server.v1_16_R2.Vec3D
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack
import org.bukkit.entity.Mob

object WalkingAnimationSystem : TickingSystem(interval = 10) {
    override fun tick() = Engine.forEach<Model, BukkitEntityComponent> { model, (mob) ->
        if (mob !is Mob) return@forEach

        val headItem = mob.toNMS().getEquipment(EnumItemSlot.HEAD)
        val meta = CraftItemStack.getItemMeta(headItem) ?: return@forEach
        val modelId = meta.customModelData
        if (modelId != model.hitId) {
            if (mob.toNMS().mot.lengthSqr > 0.007) {
                if (modelId != model.walkId)
                    CraftItemStack.setItemMeta(headItem, meta.apply { setCustomModelData(model.walkId) })
            } else if (modelId != model.id)
                CraftItemStack.setItemMeta(headItem, meta.apply { setCustomModelData(model.id) })
        }
    }

}

//TODO move
val Vec3D.lengthSqr get() = x * x + y * y + z * z
