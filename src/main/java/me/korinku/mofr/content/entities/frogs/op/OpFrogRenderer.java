package me.korinku.mofr.content.entities.frogs.op;

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
public class OpFrogRenderer extends MobEntityRenderer<FrogEntity, FrogEntityModel<FrogEntity>> {

	private static final Identifier TEXTURE = new Identifier(MoreFrogs.MOD_ID,
			"textures/entity/frog/op_frog.png");

	public OpFrogRenderer(EntityRendererFactory.Context context) {
		super(context, new FrogEntityModel<>(context.getPart(EntityModelLayers.FROG)), 0.3f);
	}

	@Override
	public Identifier getTexture(FrogEntity frogEntity) {
		return TEXTURE;
	}
}
