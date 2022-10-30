package com.plr.yaif;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod("yaif")
public class WhyAmIonFire {
    public WhyAmIonFire() {
        MinecraftForge.EVENT_BUS.register(this);
    }
}
