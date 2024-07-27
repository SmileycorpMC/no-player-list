package net.smileycorp.noplayerlist.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.smileycorp.noplayerlist.ConfigHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerList.class)
public class MixinPlayerList {
    
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"), method = "placeNewPlayer")
    public void noplayerlist$placeNewPlayer$broadcastSystemMessage(PlayerList instance, Component component, boolean force, Operation<Void> original, @Local ServerPlayer player) {
        if (!ConfigHandler.shouldHidePlayer(player.level(), player.getGameProfile())) original.call(instance, component, force);
    }

}
