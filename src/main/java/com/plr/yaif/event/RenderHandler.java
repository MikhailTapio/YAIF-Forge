package com.plr.yaif.event;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.minecraft.item.Items.MILK_BUCKET;
import static net.minecraft.potion.Effects.FIRE_RESISTANCE;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class RenderHandler {
    @SubscribeEvent
    public static void onRenderFire(RenderBlockOverlayEvent event) {
        if (event.getOverlayType() != RenderBlockOverlayEvent.OverlayType.FIRE) return;
        final PlayerEntity player = event.getPlayer();
        if (player == null) return;
        if (Minecraft.getInstance().options.hideGui || player.isCreative()) event.setCanceled(true);
        else if (player.hasEffect(FIRE_RESISTANCE)) {
            final EffectInstance effect = player.getEffect(FIRE_RESISTANCE);
            if (effect == null) return;
            if (effect.getDuration() <= 200 || player.isHolding(MILK_BUCKET)) {
                if (effect.getDuration() % 20 < 10) event.setCanceled(true);
                return;
            }
            if (effect.getDuration() > 200) event.setCanceled(true);
        }

    }
}
