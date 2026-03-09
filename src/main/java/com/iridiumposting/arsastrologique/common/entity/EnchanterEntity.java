package com.iridiumposting.arsastrologique.common.entity;

import com.hollingsworth.arsnouveau.api.entity.IDispellable;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.hollingsworth.arsnouveau.common.advancement.ANCriteriaTriggers;
import com.hollingsworth.arsnouveau.common.items.data.ICharmSerializable;
import com.hollingsworth.arsnouveau.common.items.data.PersistentFamiliarData;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.hollingsworth.arsnouveau.common.network.PacketANEffect;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import com.hollingsworth.arsnouveau.setup.registry.DataComponentRegistry;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;

import com.iridiumposting.arsastrologique.ArsAstrologique;

import com.iridiumposting.arsastrologique.common.entity.ai.goal.EatSnackGoal;
import com.iridiumposting.arsastrologique.common.entity.ai.goal.LookAtCustomerGoal;

import com.iridiumposting.arsastrologique.common.entity.ai.goal.TradeWithPlayerGoal;
import com.iridiumposting.arsastrologique.common.entity.trades.TradeOffers;

import com.iridiumposting.arsastrologique.setup.registry.AstroDataComponents;
import com.iridiumposting.arsastrologique.setup.registry.AstroEntities;
import com.iridiumposting.arsastrologique.setup.registry.AstroItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.item.ItemEntity;
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

import javax.annotation.ParametersAreNonnullByDefault;

public class EnchanterEntity extends AbstractCasterEntity implements GeoEntity, Merchant, Npc, IDispellable, ICharmSerializable {

    private @Nullable Player customer;
    public @Nullable MerchantOffers offers;
    private long lastRestockGameTime;
    private long lastRestockCheckDayTime;
    private int numberOfRestocksToday;
    int tamingTime;

    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.enchanter.idle");
    protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.enchanter.walk");
    protected static final RawAnimation EAT = RawAnimation.begin().thenPlay("animation.enchanter.eat");
    protected static final RawAnimation INSPECT = RawAnimation.begin().thenPlay("animation.enchanter.inspect");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public EnchanterEntity(EntityType<? extends PathfinderMob> entityType, Level world) {
        super(entityType, world);
    }

    public EnchanterEntity(Level world, boolean tamed) {
        this(AstroEntities.ENCHANTER_ENTITY.get(), world);
        setTamed(tamed);
    }

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
    public void tick() {
        super.tick();
        if (!isTamed() && this.entityData.get(BEING_TAMED)) {
            triggerAnim("Controller", "Inspect");
            tamingTime++;
            if (tamingTime % 20 == 0 && !level().isClientSide())
                Networking.sendToNearbyClient(level(), this, new PacketANEffect(PacketANEffect.EffectType.TIMED_HELIX, blockPosition(), ParticleColor.ORANGE));

            if (tamingTime > 60 && !level().isClientSide) {
                dropShard();
                ANCriteriaTriggers.rewardNearbyPlayers(ANCriteriaTriggers.POOF_MOB.get(), (ServerLevel) this.level(), this.getOnPos(), 10);
                ParticleUtil.spawnPoof((ServerLevel) level(), blockPosition());
                this.remove(RemovalReason.DISCARDED);
                level().playSound(null, getX(), getY(), getZ(), SoundEvents.ILLUSIONER_MIRROR_MOVE, SoundSource.NEUTRAL, 1f, 1f);
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if(!this.level().isClientSide() && this.shouldRestock())
            this.restock();
    }

    /* TEXTURES, MODELS & ANIMATIONS */

    public ResourceLocation getTexture() {
        return ArsAstrologique.prefix("textures/entity/enchanter.png");
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Controller", 0,
                state -> state.isMoving()
                        ? state.setAndContinue(WALK)
                        : state.setAndContinue(IDLE)
        )
                .triggerableAnim("Eat", EAT)
                .triggerableAnim("Inspect", INSPECT)
        );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    /* INTERACTION */

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull InteractionResult mobInteract(Player player, InteractionHand hand) {
        if(level().isClientSide() || hand != InteractionHand.MAIN_HAND) return InteractionResult.SUCCESS;

        ItemStack stack = player.getItemInHand(hand);

        if(!isTamed()){
            if(stack.is(ItemsRegistry.BLANK_PARCHMENT.asItem())){
                this.setHeldStack(player.hasInfiniteMaterials() ? stack.copyWithCount(1) : stack.split(1));
                this.entityData.set(BEING_TAMED, true);
                return InteractionResult.SUCCESS;
            }
        } else{
            if(canEat(stack)) {
                if(this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()){
                    this.setHeldStack(player.hasInfiniteMaterials() ? stack.copyWithCount(1) : stack.split(1));
                    return InteractionResult.SUCCESS;
                } else return InteractionResult.PASS;
            }
            else if(this.isAlive() && !this.isTrading()) {
                if(this.getOffers().isEmpty()) return InteractionResult.PASS;
                else this.startTrading(player);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    public void setHeldStack(ItemStack stack) {
        this.setItemSlot(EquipmentSlot.MAINHAND, stack == null ? ItemStack.EMPTY : stack);
    }

    /* DATA */

    public static final EntityDataAccessor<Boolean> EATING = SynchedEntityData.defineId(EnchanterEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> TAMED = SynchedEntityData.defineId(EnchanterEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> BEING_TAMED = SynchedEntityData.defineId(EnchanterEntity.class, EntityDataSerializers.BOOLEAN);

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(EATING, false);
        builder.define(TAMED, false);
        builder.define(BEING_TAMED, false);
    }

    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Eating", this.entityData.get(EATING));
        tag.putBoolean("Tamed", this.entityData.get(TAMED));
        tag.putBoolean("beingTamed", this.entityData.get(BEING_TAMED));

        if (!this.level().isClientSide()) {
            MerchantOffers offers = this.getOffers();
            if (!offers.isEmpty()) {
                tag.put("Offers", MerchantOffers.CODEC
                        .encodeStart(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), offers)
                        .getOrThrow());
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(EATING, tag.getBoolean("Eating"));
        this.entityData.set(BEING_TAMED, tag.getBoolean("beingTamed"));
        setTamed(tag.getBoolean("Tamed"));

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

    public void setOffers(ItemStack stack, MerchantOffers offers){
        stack.set(AstroDataComponents.ENCHANTER_TRADES, offers);
    }

    @Override
    @MethodsReturnNonnullByDefault
    public MerchantOffers getOffers() {
        if(this.offers == null){
            reloadOffers();
        }
        return this.offers;
    }

    @Override
    @ParametersAreNonnullByDefault
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
    @ParametersAreNonnullByDefault
    public void notifyTradeUpdated(ItemStack itemStack) {
        if(!this.level().isClientSide() && this.ambientSoundTime > -this.getAmbientSoundInterval() + 20)
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
        this.lastRestockGameTime = level().getGameTime();
        ++this.numberOfRestocksToday;
    }

    private void resendOffers() {
        MerchantOffers merchantOffers = this.getOffers();
        Player player = this.getTradingPlayer();
        if(player != null && !merchantOffers.isEmpty())
            player.sendMerchantOffers(player.containerMenu.containerId, merchantOffers, 0, this.getVillagerXp(), this.showProgressBar(), this.canRestock());
    }

    public boolean shouldRestock() {
        long i = this.lastRestockGameTime + 12000L;
        long j = this.level().getGameTime();
        boolean flag = j > i;
        long k = this.level().getDayTime();
        if (this.lastRestockCheckDayTime > 0L) {
            long l = this.lastRestockCheckDayTime / 24000L;
            long i1 = k / 24000L;
            flag |= i1 > l;
        }

        this.lastRestockCheckDayTime = k;
        if (flag) {
            this.lastRestockGameTime = j;
            this.resetNumberOfRestocks();
        }

        return this.allowedToRestock() && this.needsToRestock();
    }

    private boolean allowedToRestock() {
        return this.numberOfRestocksToday == 0 || this.numberOfRestocksToday < 3 && this.level().getGameTime() > this.lastRestockGameTime + 2400L;
    }

    private boolean needsToRestock() {
        for(MerchantOffer merchantoffer : this.getOffers()) {
            if (merchantoffer.needsRestock()) {
                return true;
            }
        }

        return false;
    }

    public void resetNumberOfRestocks() {
        this.catchUpDemand();
        this.numberOfRestocksToday = 0;
    }

    private void updateDemand() {
        for(MerchantOffer merchantOffer : this.getOffers()) {
            merchantOffer.updateDemand();
        }
    }

    private void catchUpDemand() {
        int i = 2 - this.numberOfRestocksToday;
        if (i > 0) {
            for(MerchantOffer merchantoffer : this.getOffers()) {
                merchantoffer.resetUses();
            }
        }

        for(int j = 0; j < i; ++j) {
            this.updateDemand();
        }

        this.resendOffers();
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

    /* CHARM & DISPEL */

    @Override
    public void fromCharmData(PersistentFamiliarData data) {
        this.setCustomName(data.name());
    }

    @Override
    public boolean onDispel(@Nullable LivingEntity caster) {
        if(this.isRemoved() || level().isClientSide()) return false;

        if(this.isTamed()){
            dropData();
            ParticleUtil.spawnPoof((ServerLevel) level(), blockPosition());
            this.remove(RemovalReason.DISCARDED);
        }

        return this.isTamed();
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        if (!level.isClientSide() && this.isTamed()) {
            dropData();
        }
    }

    private boolean isTamed() {
        return this.entityData.get(TAMED);
    }

    private void setTamed(boolean tamed) {
        this.entityData.set(TAMED, tamed);
    }

    public void dropData(){
        ItemStack charm = new ItemStack(AstroItems.ENCHANTER_CHARM.get());
        charm.set(DataComponentRegistry.PERSISTENT_FAMILIAR_DATA, new PersistentFamiliarData().setName(getCustomName()));
        setOffers(charm, this.getOffers());
        level().addFreshEntity(new ItemEntity(level(), getX(), getY(), getZ(), charm));
    }

    public void dropShard(){
        ItemStack shard = new ItemStack(AstroItems.ENCHANTER_SHARD.get());
        shard.set(DataComponentRegistry.PERSISTENT_FAMILIAR_DATA, new PersistentFamiliarData().setName(getCustomName()));
        setOffers(shard, this.getOffers());
        level().addFreshEntity(new ItemEntity(level(), getX(), getY(), getZ(), shard));
    }
}
