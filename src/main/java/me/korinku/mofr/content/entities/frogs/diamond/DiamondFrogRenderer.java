package me.korinku.mofr.content.entities.frogs.diamond;

import me.korinku.mofr.MoreFrogs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.FrogEntityModel;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class DiamondFrogRenderer extends MobEntityRenderer<FrogEntity, FrogEntityModel<FrogEntity>> {

	private static final Identifier TEXTURE = new Identifier(MoreFrogs.MOD_ID,
			"textures/entity/frog/diamond_frog.png");

	public DiamondFrogRenderer(EntityRendererFactory.Context context) {
		super(context, new FrogEntityModel<>(context.getPart(EntityModelLayers.FROG)), 0.3f);
	}

	@Override
	public Identifier getTexture(FrogEntity frogEntity) {
		return TEXTURE;
	}
}
