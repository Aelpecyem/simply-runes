package de.aelpecyem.runes.mixin.common;

import de.aelpecyem.runes.client.packet.SyncRuneRecipePacket;
import de.aelpecyem.runes.common.misc.RuneEnchantingSlot;
import de.aelpecyem.runes.common.recipe.RuneEnchantingRecipe;
import de.aelpecyem.runes.common.reg.RunesObjects;
import de.aelpecyem.runes.util.EnhancedEnchantingAccessor;
import de.aelpecyem.runes.util.RuneKnowledgeAccessor;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.*;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantmentScreenHandlerMixin extends ScreenHandler implements EnhancedEnchantingAccessor {
    @Shadow private Inventory inventory;
    private final int[] runePixels = new int[64];
    private boolean runeMode = false;
    private RuneEnchantingRecipe currentRecipe;
    private boolean hasRecipe = false;
    private EnchantmentScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @Inject(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V", at = @At("TAIL"))
    private void init(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, CallbackInfo ci){
        for (int i = 0; i < runePixels.length; i++) {
            this.addProperty(Property.create(runePixels, i));
        }
        this.slots.set(0, new RuneEnchantingSlot(playerInventory.player.world.isClient, this, inventory, 0, 15, 47));
    }

    @Inject(method = "onContentChanged", at = @At("HEAD"), cancellable = true)
    private void onContentChanged(Inventory inventory, CallbackInfo ci){
        runeMode = inventory.getStack(0).getItem() == RunesObjects.SMOOTH_SLATE;
    }

    @Inject(method = "onButtonClick", at = @At("HEAD"), cancellable = true)
    private void onButtonClick(PlayerEntity player, int id, CallbackInfoReturnable<Boolean> cir){
        if (isRuneMode()){
            cir.setReturnValue(false);
            if (id < 64 + 2 && id > 2){
                id -= 3;
                runePixels[id] = runePixels[id] == 1 ? 0 : 1;
                cir.setReturnValue(true);
            }
        }
        if (player instanceof ServerPlayerEntity sp && player instanceof RuneKnowledgeAccessor knowledge) {
            currentRecipe = RuneEnchantingRecipe.getRecipe(knowledge, runePixels).orElse(null);
            if (currentRecipe != null && !hasRecipe) {
                SyncRuneRecipePacket.send(sp, currentRecipe, syncId);
                hasRecipe = true;
            } else if (hasRecipe && currentRecipe == null) {
                SyncRuneRecipePacket.send(sp, null, syncId);
                hasRecipe = false;
            }
        }
    }

    @Override
    public boolean isRuneMode() {
        return runeMode;
    }

    @Override
    public int[] getRunePixels() {
        return runePixels;
    }

    @Override
    public void clearPixels() {
        Arrays.fill(runePixels, 0);
    }

    @Override
    public RuneEnchantingRecipe getRecipe() {
        return currentRecipe;
    }

    @Override
    public void setRecipe(RuneEnchantingRecipe recipe) {
        this.currentRecipe = recipe;
    }
}
