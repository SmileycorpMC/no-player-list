package net.smileycorp.noplayerlist.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.smileycorp.noplayerlist.ConfigHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(PlayerTabOverlay.class)
public abstract class MixinPlayerTabOverlay {
    
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;getPlayerInfos()Ljava/util/List;"), method = "render")
    public List<PlayerInfo> noplayerlist$getListedOnlinePlayers(PlayerTabOverlay instance, Operation<List<PlayerInfo>> original) {
        LocalPlayer player = Minecraft.getInstance().player;
        return original.call(instance).stream().filter(info -> info.getProfile().equals(player.getGameProfile())
                |! ConfigHandler.shouldHidePlayer(player.level(), info.getProfile())).toList();
    }
    
}
