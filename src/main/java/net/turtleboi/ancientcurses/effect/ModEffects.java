package net.turtleboi.ancientcurses.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.turtleboi.ancientcurses.AncientCurses;
import net.turtleboi.ancientcurses.effect.effects.CurseOfFrailtyEffect;
import net.turtleboi.ancientcurses.effect.effects.CurseOfSlothEffect;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, AncientCurses.MOD_ID);

    public static final RegistryObject<MobEffect> CURSE_OF_SLOTH = MOB_EFFECTS.register("curse_of_sloth",
            () -> new CurseOfSlothEffect(MobEffectCategory.HARMFUL, 4928256));

    public static final RegistryObject<MobEffect> CURSE_OF_FRAILTY = MOB_EFFECTS.register("curse_of_frailty",
            () -> new CurseOfFrailtyEffect(MobEffectCategory.HARMFUL, 4928256));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
