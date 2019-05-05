package com.offz.spigot.custommobs.Mobs.Hostile;

import com.offz.spigot.custommobs.Builders.MobBuilder;
import com.offz.spigot.custommobs.Loading.CustomType;
import com.offz.spigot.custommobs.Mobs.CustomMob;
import com.offz.spigot.custommobs.Mobs.Passive.Neritantan;
import com.offz.spigot.custommobs.Pathfinders.PathfinderGoalLookAtPlayerPitchLock;
import com.offz.spigot.custommobs.Pathfinders.PathfinderGoalMeleeAttackPitchLock;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_13_R2.event.CraftEventFactory;
import org.bukkit.entity.Monster;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class HostileMob extends EntityMonster implements CustomMob {
    private static final UUID a = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
    private int bI;

    private MobBuilder builder = getBuilder();


    //TODO eventually have a builder class for passing parameters here
    public HostileMob(World world, MobBuilder builder) {
        super(CustomType.getType(builder), world);
        this.setSize(0.6F, 1.95F);

        this.addScoreboardTag("customMob");
        this.setCustomNameVisible(true);

        Monster asMonster = ((Monster) this.getBukkitEntity());
        asMonster.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
        asMonster.setCustomName(builder.getName());

        this.addScoreboardTag(builder.getName());

        //create an item based on model ID in head slot
        ItemStack is = new ItemStack(builder.getModelMaterial(), (short) builder.getModelID());
        ItemMeta meta = is.getItemMeta();
        meta.setUnbreakable(true);
        is.setItemMeta(meta);
        asMonster.getEquipment().setHelmet(is);
        asMonster.setRemoveWhenFarAway(false);

        DisguiseAPI.disguiseEntity(this.getBukkitEntity(), new MobDisguise(builder.getDisguiseAs()/*getBuilder()*/));
    }

    public EntityTypes getType(Entity e) {
        return CustomType.getType(CustomType.toEntityTypeID(getBuilder().getName()));
    }

    protected void n() {
        createPathfinders();
    }

    protected void createPathfinders() {
        this.goalSelector.a(2, new PathfinderGoalMeleeAttackPitchLock(this, 1.0D, false));
//        this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayerPitchLock(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));

        //Hostile attributes
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityPlayer.class, true));
        this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(this, Neritantan.class, true));
        this.goalSelector.a(7, new PathfinderGoalRandomStrollLand(this, 1.0D));
    }

    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(10.0D);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.25D);
    }

    @Nullable
    public Entity bO() {
        return this.bP().isEmpty() ? null : this.bP().get(0);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
//        if (this.isBaby()) {
//            nbttagcompound.setBoolean("IsBaby", true);
//        }
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);

//        if (nbttagcompound.getBoolean("IsBaby")) {
//            this.setBaby(true);
//        }
        CraftEntity asEntity = this.getBukkitEntity();

        if (!DisguiseAPI.isDisguised(asEntity)) { //if not disguised
            DisguiseAPI.disguiseEntity(asEntity, new MobDisguise(builder.getDisguiseAs()));
        }
    }

    protected SoundEffect D() {
        return soundAmbient();
    }

    protected SoundEffect d(DamageSource damagesource) {
        return soundHurt();
    }

    protected SoundEffect cs() {
        return soundDeath();
    }

    protected void a(BlockPosition blockposition, IBlockData iblockdata) {
        this.a(soundStep(), 0.15F, 1.0F);
    }

    //TODO have a builder for sounds?
    protected SoundEffect soundAmbient() {
        return SoundEffects.ENTITY_PIG_AMBIENT;
    }

    protected SoundEffect soundHurt() {
        return SoundEffects.ENTITY_PIG_HURT;
    }

    protected SoundEffect soundDeath() {
        return SoundEffects.ENTITY_PIG_DEATH;
    }

    protected SoundEffect soundStep() {
        return SoundEffects.ENTITY_PIG_STEP;
    }

    public void die(DamageSource damagesource) {
        if (!this.killed) {
            Entity entity = damagesource.getEntity();
            EntityLiving entityliving = this.cv();
            if (this.be >= 0 && entityliving != null)
                entityliving.a(this, this.be, damagesource);

            /*if (entity != null)
                entity.b(this);*/

            this.killed = true;
            this.getCombatTracker().g();
            if (!this.world.isClientSide) {
                if (this.isDropExperience() && this.world.getGameRules().getBoolean("doMobLoot")) {
                    boolean flag = this.lastDamageByPlayerTime > 0;
                    this.a(flag, 0, damagesource);
                    CraftEventFactory.callEntityDeathEvent(this, builder.getDrops());
                } else {
                    CraftEventFactory.callEntityDeathEvent(this);
                }
            }

            this.world.broadcastEntityEffect(this, (byte) 3);
        }
    }

    @Nullable
    protected MinecraftKey getDefaultLootTable() {
        return LootTables.L;
    }
}
