package net.smileycorp.noplayerlist;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PlayerCache {
    
    private final boolean hideWhitelist, invertWhitelist;
    private final List<UUID> playerList;
    
    private PlayerCache(boolean hideWhitelist, boolean invertWhitelist, List<UUID> playerList) {
        this.hideWhitelist = hideWhitelist;
        this.invertWhitelist = invertWhitelist;
        this.playerList = playerList;
    }
    
    public boolean shouldHidePlayer(GameProfile profile) {
        if (!hideWhitelist) return true;
        return invertWhitelist ^ playerList.contains(profile.getId());
    }
    
    public SyncPlayerCacheMessage createPacket() {
        return new SyncPlayerCacheMessage(hideWhitelist, invertWhitelist, playerList);
    }
    
    public static PlayerCache fromConfig() {
        List<UUID> playerList = Lists.newArrayList();
        for (String string : ConfigHandler.playerList.get()) {
            try {
                playerList.add(UUID.fromString(string));
            } catch (Exception e) {
                Optional<GameProfile> optional = ServerLifecycleHooks.getCurrentServer().getProfileCache().get(string);
                if (optional.isPresent()) playerList.add(optional.get().getId());
            }
        }
        return new PlayerCache(ConfigHandler.hideWhitelist.get(), ConfigHandler.invertWhitelist.get(), playerList);
    }
    
    public static PlayerCache fromPacket(SyncPlayerCacheMessage message) {
        return new PlayerCache(message.hideWhitelist, message.invertWhitelist, message.playerList);
    }
    
}
