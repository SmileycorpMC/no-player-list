package net.smileycorp.noplayerlist.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.chat.Component;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerList.class)
public class MixinPlayerList {
    
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"), method = "placeNewPlayer")
    public void noplayerlist$placeNewPlayer$broadcastSystemMessage(PlayerList instance, Component p_240618_, boolean p_240644_, Operation<Void> original) {}

}
