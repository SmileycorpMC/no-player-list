package net.smileycorp.noplayerlist.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import net.smileycorp.noplayerlist.ConfigHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class MixinServerGamePacketListenerImpl {
    
    @Shadow public ServerPlayer player;
    
    @Shadow public abstract void onDisconnect(Component p_9825_);
    
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"), method = "onDisconnect")
    public void noplayerlist$placeNewPlayer$broadcastSystemMessage(PlayerList instance, Component component, boolean force, Operation<Void> original) {
        if (!ConfigHandler.shouldHidePlayer(player.level(), player.getGameProfile())) original.call(instance, component, force);
    }
    
}
