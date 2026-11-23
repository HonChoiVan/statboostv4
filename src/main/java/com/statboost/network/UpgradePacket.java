package com.statboost.network;

import com.statboost.StatManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UpgradePacket {
    public final int stat;

    public UpgradePacket(int stat) {
        this.stat = stat;
    }

    public static void encode(UpgradePacket m, FriendlyByteBuf buf) {
        buf.writeVarInt(m.stat);
    }

    public static UpgradePacket decode(FriendlyByteBuf buf) {
        return new UpgradePacket(buf.readVarInt());
    }

    public static void handle(UpgradePacket m, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context c = ctx.get();
        c.enqueueWork(() -> {
            ServerPlayer p = c.getSender();
            if (p != null) StatManager.tryUpgrade(p, m.stat);
        });
        c.setPacketHandled(true);
    }
}
