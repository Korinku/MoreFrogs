package me.korinku.mofr.client;

import me.korinku.mofr.content.entities.ModEntity;
import me.korinku.mofr.content.entities.frogs.apple.AppleFrogRenderer;
import me.korinku.mofr.content.entities.frogs.blaze.BlazeFrogRenderer;
import me.korinku.mofr.content.entities.frogs.diamond.DiamondFrogRenderer;
import me.korinku.mofr.content.entities.frogs.op.OpFrogRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class MoreFrogsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {

		EntityRendererRegistry.register(
				ModEntity.APPLE_FROG,
				(context) -> new AppleFrogRenderer(context));

		EntityRendererRegistry.register(
				ModEntity.DIAMOND_FROG,
				(context) -> new DiamondFrogRenderer(context));

		EntityRendererRegistry.register(
				ModEntity.BLAZE_FROG,
				(context) -> new BlazeFrogRenderer(context));

		EntityRendererRegistry.register(
				ModEntity.OP_FROG,
				(context) -> new OpFrogRenderer(context));

	}

}
