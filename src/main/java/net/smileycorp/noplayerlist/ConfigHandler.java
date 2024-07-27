package net.smileycorp.noplayerlist;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;

public class ConfigHandler {
    
    public static PlayerCache serverCache;
    public static PlayerCache clientCache;
    
    public static ForgeConfigSpec.ConfigValue<Boolean> hideWhitelist;
    public static ForgeConfigSpec.ConfigValue<Boolean> invertWhitelist;
    public static ForgeConfigSpec.ConfigValue<List<String>> playerList;
    
    public static void init() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("General");
        hideWhitelist = builder.comment("Should a defined list of players be hidden instead of everyone?").define("hideWhitelist", false);
        invertWhitelist = builder.comment("Should the player list be a blacklist instead of a whitelist, hiding all players except those specified?")
                .define("invertWhitelist", false);
        playerList = builder.comment("A list of players to be hidden/not to be hidden (accepts usernames or uuids), only works when hideWhitelist is true")
                .define("playerList", Lists.newArrayList());
        builder.pop();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, builder.build());
    }
    
    public static void rebuildCaches() {
        serverCache = PlayerCache.fromConfig();
        clientCache = PlayerCache.fromConfig();
    }
    
    public static boolean shouldHidePlayer(Level level, GameProfile profile) {
        return (level.isClientSide ? clientCache : serverCache).shouldHidePlayer(profile);
    }
    
}
