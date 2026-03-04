package com.iridiumposting.arsastrologique.common.entity;

import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;

import com.iridiumposting.arsastrologique.ArsAstrologique;

import com.iridiumposting.arsastrologique.common.entity.ai.goal.EatSnackGoal;
import com.iridiumposting.arsastrologique.common.entity.ai.goal.LookAtCustomerGoal;

import com.iridiumposting.arsastrologique.common.entity.ai.goal.TradeWithPlayerGoal;
import com.iridiumposting.arsastrologique.common.entity.trades.TradeOffers;

import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class EnchanterEntity extends PathfinderMob implements GeoEntity, Merchant, Npc {

    public EnchanterEntity(EntityType<? extends PathfinderMob> entityType, Level world) {
        super(entityType, world);
    }

    private @Nullable Player customer;
    public @Nullable MerchantOffers offers;

    public static AttributeSupplier createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 14.0D)
                .add(Attributes.ARMOR, 12)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 16D)
                .build();
    }

    /* AI & GOALS */

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new TradeWithPlayerGoal(this));
        this.goalSelector.addGoal(2, new LookAtCustomerGoal(this));
        this.goalSelector.addGoal(3, new EatSnackGoal(this));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if(!this.level().isClientSide){
            if(this.shouldRestock())
                this.restock();
        }
    }

    /* TEXTURES, MODELS & ANIMATIONS */

    public ResourceLocation getTexture() {
        return ArsAstrologique.prefix("textures/entity/enchanter.png");
    }

    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.enchanter.idle");
    protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.enchanter.walk");
    protected static final RawAnimation EAT = RawAnimation.begin().thenPlay("animation.enchanter.eat");

    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Controller", 0,
                state -> state.isMoving()
                        ? state.setAndContinue(WALK)
                        : state.setAndContinue(IDLE)
        )
                .triggerableAnim("Eat", EAT));
    }

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    /* INTERACTION */

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if(canEat(stack)) {
            if (this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
                this.setHeldStack(player.hasInfiniteMaterials() ? stack.copyWithCount(1) : stack.split(1));
                return InteractionResult.SUCCESS;
            } else return InteractionResult.PASS;
        } else if(this.isAlive() && !this.isTrading()) {
            if(this.getOffers().isEmpty()) return InteractionResult.PASS;
            if(!this.isClientSide() && !this.offers.isEmpty())
                this.startTrading(player);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public void setHeldStack(ItemStack stack) {
        this.setItemSlot(EquipmentSlot.MAINHAND, stack == null ? ItemStack.EMPTY : stack);
    }

    /* DATA */

    public static final EntityDataAccessor<Boolean> EATING = SynchedEntityData.defineId(EnchanterEntity.class, EntityDataSerializers.BOOLEAN);

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(EATING, false);
    }

    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Eating", this.entityData.get(EATING));

        if (!this.level().isClientSide) {
            MerchantOffers offers = this.getOffers();
            if (!offers.isEmpty()) {
                tag.put("Offers", MerchantOffers.CODEC
                        .encodeStart(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), offers)
                        .getOrThrow());
            }
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(EATING, tag.getBoolean("Eating"));

        if (tag.contains("Offers", CompoundTag.TAG_COMPOUND)) {
            MerchantOffers.CODEC
                    .parse(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), tag.get("Offers"))
                    .resultOrPartial(Util.prefix("Failed to load offers: ", ArsAstrologique.LOGGER::warn))
                    .ifPresent(offers -> this.offers = offers);
        }
    }

    /* TRADING */

    public void reloadOffers() {
        this.offers = new MerchantOffers();
        TradeOffers.updateTrades(this);
    }

    @Override
    public @Nullable Player getTradingPlayer() {
        return this.customer;
    }

    @Override
    public @Nullable MerchantOffers getOffers() {
        if(this.offers == null){
            reloadOffers();
        }
        return this.offers;
    }

    @Override
    public void overrideOffers(MerchantOffers merchantOffers) { }

    @Override
    public void notifyTrade(MerchantOffer merchantOffer) {
        merchantOffer.increaseUses();
        this.ambientSoundTime = -this.getAmbientSoundInterval();
        this.rewardTradeXp(merchantOffer);
    }

    protected void rewardTradeXp(MerchantOffer merchantOffer) {
        int i = 3 + this.random.nextInt(4);

        if(merchantOffer.shouldRewardExp())
            this.level().addFreshEntity(
                    new ExperienceOrb(this.level(), this.getX(), this.getY() + 0.5, this.getZ(), i)
            );
    }

    @Override
    public void notifyTradeUpdated(ItemStack itemStack) {
        if(!this.level().isClientSide && this.ambientSoundTime > -this.getAmbientSoundInterval() + 20)
            this.ambientSoundTime = -this.getAmbientSoundInterval();
    }

    private void startTrading(Player player){
        this.setTradingPlayer(player);
        this.openTradingScreen(player, this.getDisplayName(), 0);
    }

    @Override
    public void setTradingPlayer(@Nullable Player player) {
        this.customer = player;
    }

    @Override
    public boolean canRestock() {
        return true;
    }

    public void restock() {
        this.updateDemand();
        for(MerchantOffer merchantOffer : this.getOffers()) {
            merchantOffer.resetUses();
        }
        this.resendOffers();
    }

    private void resendOffers() {
        MerchantOffers merchantOffers = this.getOffers();
        Player player = this.getTradingPlayer();
        if(player != null && !merchantOffers.isEmpty())
            player.sendMerchantOffers(player.containerMenu.containerId, merchantOffers, 0, this.getVillagerXp(), this.showProgressBar(), this.canRestock());
    }

    public boolean shouldRestock() {
        return (this.tickCount % 60 == 0);
    }

    private void updateDemand() {
        for(MerchantOffer merchantOffer : this.getOffers()) {
            merchantOffer.updateDemand();
        }
    }

    public boolean isTrading() {
        return this.customer != null;
    }

    @Override
    public int getVillagerXp() { return 0; }

    @Override
    public void overrideXp(int i) { }

    @Override
    public boolean showProgressBar() { return false; }

    @Override
    public SoundEvent getNotifyTradeSound() { return null; }

    /* MISC? */

    @Override
    public boolean isClientSide() { return this.level().isClientSide(); }

    public boolean canEat(ItemStack stack){
        return stack.is(ItemsRegistry.SOURCE_BERRY_ROLL.asItem()) || stack.is(BlockRegistry.SOURCEBERRY_BUSH.asItem()) || stack.is(BlockRegistry.BASTION_POD.asItem());
    }

    public void eatSnack() {
        this.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F+random.nextFloat()*0.125F);
        this.triggerAnim("Controller", "Eat");
    }

}
