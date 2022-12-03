package me.korinku.mofr;

import me.korinku.mofr.content.entities.ModEntity;
import me.korinku.mofr.content.items.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class MoreFrogs implements ModInitializer {

	public static final String MOD_ID = "mofr";

	public static final ItemGroup CUSTOM_GROUP = FabricItemGroupBuilder
			.build(new Identifier(MOD_ID, "custom_group"), () -> new ItemStack(ModItems.DIAMOND_FROG));

	@Override
	public void onInitialize() {

		ModEntity.registerEntities();

		ModItems.init();
	}
}
