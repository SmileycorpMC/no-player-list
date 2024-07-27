package net.smileycorp.noplayerlist;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod("noplayerlist")
@Mod.EventBusSubscriber(modid = "noplayerlist")
public class NoPlayerList {
    
    public static SimpleChannel NETWORK_INSTANCE;
    
    public NoPlayerList() {
        ConfigHandler.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::constructMod);
    }
    
    private void constructMod(FMLConstructModEvent event) {
        ConfigHandler.rebuildCaches();
        NETWORK_INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation("noplayerlist:channel"), () -> "1", "1"::equals, "1"::equals);
        NETWORK_INSTANCE.registerMessage(0, SyncPlayerCacheMessage.class, SyncPlayerCacheMessage::write, SyncPlayerCacheMessage::new, (pkt, ctx) -> {
            ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> ConfigHandler.clientCache = PlayerCache.fromPacket(pkt)));
            ctx.get().setPacketHandled(true);
        });
    }
    
    @SubscribeEvent
    public static void joinLevel(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer) NETWORK_INSTANCE.sendTo(ConfigHandler.serverCache.createPacket(),
                ((ServerPlayer) event.getEntity()).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
    
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
    
    }
    
}
