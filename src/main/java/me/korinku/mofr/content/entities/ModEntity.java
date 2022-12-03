package me.korinku.mofr.content.entities;

import me.korinku.mofr.MoreFrogs;
import me.korinku.mofr.content.entities.frogs.apple.AppleFrogEntity;
import me.korinku.mofr.content.entities.frogs.blaze.BlazeFrogEntity;
import me.korinku.mofr.content.entities.frogs.diamond.DiamondFrogEntity;
import me.korinku.mofr.content.entities.frogs.op.OpFrogEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEntity {

	public static final EntityType<AppleFrogEntity> APPLE_FROG = FabricEntityTypeBuilder
			.createMob().spawnGroup(SpawnGroup.CREATURE)
			.entityFactory(
					AppleFrogEntity::new)
			.dimensions(EntityDimensions.fixed(0.5f,
					0.55f))
			.build();

	public static final EntityType<DiamondFrogEntity> DIAMOND_FROG = FabricEntityTypeBuilder
			.createMob().spawnGroup(SpawnGroup.CREATURE)
			.entityFactory(
					DiamondFrogEntity::new)
			.dimensions(EntityDimensions.fixed(0.5f,
					0.55f))
			.build();

	public static final EntityType<BlazeFrogEntity> BLAZE_FROG = FabricEntityTypeBuilder
			.createMob().spawnGroup(SpawnGroup.CREATURE)
			.entityFactory(
					BlazeFrogEntity::new)
			.dimensions(EntityDimensions.fixed(0.5f,
					0.55f))
			.build();

	public static final EntityType<OpFrogEntity> OP_FROG = FabricEntityTypeBuilder
			.createMob().spawnGroup(SpawnGroup.CREATURE)
			.entityFactory(
					OpFrogEntity::new)
			.dimensions(EntityDimensions.fixed(0.5f,
					0.55f))
			.build();

	public static void registerEntities() {

		Registry.register(Registry.ENTITY_TYPE, new Identifier(MoreFrogs.MOD_ID, "apple_frog"),
				APPLE_FROG);

		Registry.register(Registry.ENTITY_TYPE, new Identifier(MoreFrogs.MOD_ID, "diamond_frog"),
				DIAMOND_FROG);

		Registry.register(Registry.ENTITY_TYPE, new Identifier(MoreFrogs.MOD_ID, "blaze_frog"),
				BLAZE_FROG);

		Registry.register(Registry.ENTITY_TYPE, new Identifier(MoreFrogs.MOD_ID, "op_frog"),
				OP_FROG);

	}
}
