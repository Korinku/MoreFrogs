package me.korinku.mofr.content.items;

import me.korinku.mofr.MoreFrogs;
import me.korinku.mofr.content.entities.ModEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class ModItems {

	public static final Item APPLE_FROG_EGG = registerItem("apple_frog_spawn_egg",
			new SpawnEggItem(ModEntity.APPLE_FROG, 7960171, 15263976,
					new FabricItemSettings().group(MoreFrogs.CUSTOM_GROUP).rarity(Rarity.UNCOMMON)));

	public static final Item DIAMOND_FROG = registerItem("diamond_frog_spawn_egg",
			new SpawnEggItem(ModEntity.DIAMOND_FROG, 7960171, 15263976,
					new FabricItemSettings().group(MoreFrogs.CUSTOM_GROUP).rarity(Rarity.UNCOMMON)));

	public static final Item BLAZE_FROG = registerItem("blaze_frog_spawn_egg",
			new SpawnEggItem(ModEntity.BLAZE_FROG, 7960171, 15263976,
					new FabricItemSettings().group(MoreFrogs.CUSTOM_GROUP).rarity(Rarity.UNCOMMON)));

	public static final Item OP_FROG = registerItem("op_frog_spawn_egg",
			new SpawnEggItem(ModEntity.OP_FROG, 7960171, 15263976,
					new FabricItemSettings().group(MoreFrogs.CUSTOM_GROUP).rarity(Rarity.UNCOMMON)));

	public static final Item DEFUNTO = registerItem("defunto",
			new Item(new FabricItemSettings().group(MoreFrogs.CUSTOM_GROUP).rarity(Rarity.UNCOMMON)));

	private static Item registerItem(String name, Item item) {
		return Registry.register(Registry.ITEM, new Identifier(MoreFrogs.MOD_ID, name), item);
	}

	public static void init() {
	}
}
