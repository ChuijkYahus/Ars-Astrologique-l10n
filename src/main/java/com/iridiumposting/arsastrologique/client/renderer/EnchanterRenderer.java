package com.iridiumposting.arsastrologique.client.renderer;

import com.iridiumposting.arsastrologique.common.entity.EnchanterEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.RenderUtil;

public class EnchanterRenderer extends GeoEntityRenderer<EnchanterEntity> {
    public EnchanterRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new EnchanterModel());
    }

    @Override
    public void renderRecursively(PoseStack stack, EnchanterEntity enchanter, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int color) {
        super.renderRecursively(stack, enchanter, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, color);
        if (bone.getName().equals("item")) {
            stack.pushPose();
            RenderUtil.translateToPivotPoint(stack, bone);
            stack.translate(0, -0.10, 0);
            stack.scale(0.75f, 0.75f, 0.75f);
            ItemStack itemstack = enchanter.getMainHandItem();
            if (!itemstack.isEmpty()) {
                Minecraft.getInstance().getItemRenderer().renderStatic(itemstack, ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, stack, bufferSource, enchanter.level(), (int) enchanter.getOnPos().asLong());
            }
            stack.popPose();
        }
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(EnchanterEntity enchanter) {
        if (enchanter instanceof EnchanterEntity var) return var.getTexture();
        return super.getTextureLocation(enchanter);
    }

    @Override
    public RenderType getRenderType(EnchanterEntity entity, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(texture);
    }
}
