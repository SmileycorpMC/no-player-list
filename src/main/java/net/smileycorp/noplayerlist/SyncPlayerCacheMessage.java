package net.smileycorp.noplayerlist;

import com.google.common.collect.Lists;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;

import java.util.List;
import java.util.UUID;

public class SyncPlayerCacheMessage implements Packet<PacketListener> {
    
    protected final boolean hideWhitelist, invertWhitelist;
    protected final List<UUID> playerList;
    
    public SyncPlayerCacheMessage(FriendlyByteBuf buf) {
        hideWhitelist = buf.readBoolean();
        invertWhitelist = buf.readBoolean();
        playerList = Lists.newArrayList();
        while (buf.isReadable()) playerList.add(buf.readUUID());
    }
    
    public SyncPlayerCacheMessage(boolean hideWhitelist, boolean invertWhitelist, List<UUID> playerList) {
        this.hideWhitelist = hideWhitelist;
        this.invertWhitelist = invertWhitelist;
        this.playerList = playerList;
    }
    
    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(hideWhitelist);
        buf.writeBoolean(invertWhitelist);
        for (UUID uuid : playerList) buf.writeUUID(uuid);
    }
    
    @Override
    public void handle(PacketListener packet) {}
    
}
