package com.plr.yaif.mixin;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.world.effect.MobEffects.FIRE_RESISTANCE;
import static net.minecraft.world.item.Items.MILK_BUCKET;

@Mixin(Entity.class)
public abstract class MixinEntity {

    @Shadow
    public abstract AABB getBoundingBox();

    @Shadow
    public Level level;

    @Shadow
    private int remainingFireTicks;

    @Inject(method = "baseTick", at = @At("TAIL"))
    private void inject$baseTick(CallbackInfo ci) {
        extinguishIfResistantToFire();
    }

    private void extinguishIfResistantToFire() {
        if (((Entity) (Object) this) instanceof LivingEntity) {
            boolean isCreative = false;
            boolean hasFireResistance = false;
            int remainingDuration = 0;
            if (((LivingEntity) (Object) this).hasEffect(FIRE_RESISTANCE)) {
                remainingDuration = ((LivingEntity) (Object) this).getEffect(FIRE_RESISTANCE).getDuration();
                hasFireResistance = true;
            }
            if (((Entity) (Object) this) instanceof Player && ((Player) (Object) this).isCreative())
                isCreative = true;

            if (((Entity) (Object) this).getRemainingFireTicks() > 0 && hasFireResistance && !isCreative
                    && !(this.level.getBlockStatesIfLoaded(this.getBoundingBox().expandTowards(-0.001D, -0.001D, -0.001D)).anyMatch((blockStatex)
                    -> blockStatex.is(BlockTags.FIRE) || blockStatex.is(Blocks.LAVA))
                    && ((hasFireResistance && remainingDuration <= 200) || ((LivingEntity) (Object) this).isHolding(MILK_BUCKET))
            )
            ) {
                this.remainingFireTicks = 0;
            } else if (isCreative) {
                this.remainingFireTicks = 0;
            }
        }
    }
}
