package de.aelpecyem.runes.common.reg;

import de.aelpecyem.runes.common.entity.JeraRuneEntity;
import de.aelpecyem.runes.common.entity.ThrownRockEntity;
import de.aelpecyem.runes.util.RegistryUtil;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;

public class RunesEntities {
    public static final EntityType<ThrownRockEntity> ROCK = FabricEntityTypeBuilder.<ThrownRockEntity>create(SpawnGroup.MISC, ThrownRockEntity::new)
            .dimensions(EntityDimensions.fixed(0.5F, 0.5F)).trackRangeBlocks(4).trackedUpdateRate(10).build();
    public static final EntityType<JeraRuneEntity> JERA_RUNE = FabricEntityTypeBuilder.<JeraRuneEntity>create(SpawnGroup.MISC, JeraRuneEntity::new)
            .dimensions(EntityDimensions.fixed(0.6F, 0.6F)).trackRangeBlocks(10).build();

    public static void init(){
        RegistryUtil.register(Registry.ENTITY_TYPE, "rock", ROCK);
        RegistryUtil.register(Registry.ENTITY_TYPE, "jera_rune", JERA_RUNE);
    }
}
