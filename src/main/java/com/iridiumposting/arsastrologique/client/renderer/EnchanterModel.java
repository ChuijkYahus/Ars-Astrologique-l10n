package com.iridiumposting.arsastrologique.client.renderer;

import com.iridiumposting.arsastrologique.ArsAstrologique;
import com.iridiumposting.arsastrologique.common.entity.EnchanterEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class EnchanterModel extends GeoModel<EnchanterEntity> {
    ResourceLocation MODEL = ArsAstrologique.prefix("geo/enchanter.geo.json");
    ResourceLocation TEXTURE = ArsAstrologique.prefix("textures/entity/enchanter.png");
    ResourceLocation ANIMATIONS = ArsAstrologique.prefix("animations/enchanter_animations.json");

    @Override
    public ResourceLocation getModelResource(EnchanterEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(EnchanterEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(EnchanterEntity animatable) {
        return ANIMATIONS;
    }

    @Override
    public void setCustomAnimations(EnchanterEntity animatable, long instanceId, AnimationState<EnchanterEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if(animationState == null || animatable.getEntityData().get(EnchanterEntity.EATING)) return;

        GeoBone head = getAnimationProcessor().getBone("main");
        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
        head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
    }
}
