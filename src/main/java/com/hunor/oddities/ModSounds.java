package com.hunor.oddities;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static final SoundEvent roomba_moving = registerSoundEvent("roomba_moving");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(HunorsOddities.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {}
}
