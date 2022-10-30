package com.plr.yaif.event;

import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderBlockScreenEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.minecraft.world.effect.MobEffects.FIRE_RESISTANCE;
import static net.minecraft.world.item.Items.MILK_BUCKET;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class RenderHandler {
    @SubscribeEvent
    public static void onRenderFire(RenderBlockScreenEffectEvent event) {
        if (event.getOverlayType() != RenderBlockScreenEffectEvent.OverlayType.FIRE) return;
        final Player player = event.getPlayer();
        if (Minecraft.getInstance().options.hideGui || player.isCreative()) event.setCanceled(true);
        else if (player.hasEffect(FIRE_RESISTANCE)) {
            final MobEffectInstance effect = player.getEffect(FIRE_RESISTANCE);
            if (effect == null) return;
            if (effect.getDuration() <= 200 || player.isHolding(MILK_BUCKET)) {
                if (effect.getDuration() % 20 < 10) event.setCanceled(true);
                return;
            }
            if (effect.getDuration() > 200) event.setCanceled(true);
        }

    }
}
