package com.smilezyd.trimmability.util.networking;

import com.smilezyd.trimmability.Trimmability;
import com.smilezyd.trimmability.util.screenhandlers.custom.EngravingTableScreenHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record RepositionSlots(boolean editorScreen) implements CustomPayload {
    public static final CustomPayload.Id<RepositionSlots> ID = new CustomPayload.Id<>(Identifier.of(Trimmability.MOD_ID, "reposition_slots"));
    public static final PacketCodec<PacketByteBuf, RepositionSlots> CODEC = PacketCodec.of(RepositionSlots::write, RepositionSlots::new);

    public RepositionSlots(PacketByteBuf buf) {
        this(buf.readBoolean());
    }

    public void write(PacketByteBuf buf) {
        buf.writeBoolean(this.editorScreen);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

}

