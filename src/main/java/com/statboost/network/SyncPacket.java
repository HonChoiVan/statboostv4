package com.statboost.network;

import com.statboost.ClientStatData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncPacket {
    public final int[] points;
    public final int[] costs;
    public final int max;
    public final float percent;
    public final float maxBonus;

    public SyncPacket(int[] points, int[] costs, int max, float percent, float maxBonus) {
        this.points = points;
        this.costs = costs;
        this.max = max;
        this.percent = percent;
        this.maxBonus = maxBonus;
    }

    public static void encode(SyncPacket m, FriendlyByteBuf buf) {
        buf.writeVarIntArray(m.points);
        buf.writeVarIntArray(m.costs);
        buf.writeVarInt(m.max);
        buf.writeFloat(m.percent);
        buf.writeFloat(m.maxBonus);
    }

    public static SyncPacket decode(FriendlyByteBuf buf) {
        return new SyncPacket(buf.readVarIntArray(), buf.readVarIntArray(), buf.readVarInt(),
                buf.readFloat(), buf.readFloat());
    }

    public static void handle(SyncPacket m, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context c = ctx.get();
        c.enqueueWork(() -> ClientStatData.update(m.points, m.costs, m.max, m.percent, m.maxBonus));
        c.setPacketHandled(true);
    }
}
