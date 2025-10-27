package com.smilezyd.trimmability.mixin.client;

import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

// SlotAccessor.java
@Mixin(Slot.class)
public interface SlotAccessor {
    @Mutable @Accessor("x") void setX(int x);
    @Mutable @Accessor("y") void setY(int y);
}
