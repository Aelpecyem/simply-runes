package de.aelpecyem.runes;

import de.aelpecyem.runes.common.reg.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;
import java.util.stream.Collectors;

public class RunesMod implements ModInitializer {
    public static final String MOD_ID = "runes";
    public static final ItemGroup GROUP = ItemGroup.MISC;
    @Override
    public void onInitialize() {
        RunesObjects.init();
        RunesEntities.init();
        RunesSounds.init();
        RunesParticles.init();
        RunesLootTables.init();
    }

    public static Identifier id(String path){
        return new Identifier(MOD_ID, path);
    }
}