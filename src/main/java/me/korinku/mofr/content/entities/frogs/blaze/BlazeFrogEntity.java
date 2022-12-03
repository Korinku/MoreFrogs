package me.korinku.mofr.content.entities.frogs.blaze;

import java.util.Random;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.pathing.AmphibiousPathNodeMaker;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.ai.pathing.PathNodeNavigator;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.AxolotlSwimNavigation;
import net.minecraft.entity.passive.FrogBrain;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.entity.passive.FrogVariant;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BiomeTags;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.BlockView;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;

public class BlazeFrogEntity extends FrogEntity {

	public BlazeFrogEntity(EntityType<? extends AnimalEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public boolean isFireImmune() {
		return true;
	}

	@Override
	protected void mobTick() {
		this.world.getProfiler().push("frogBrain");
		this.getBrain().tick((ServerWorld) this.world, this);
		this.world.getProfiler().pop();
		this.world.getProfiler().push("frogActivityUpdate");
		FrogBrain.updateActivities(this);
		this.world.getProfiler().pop();
		super.mobTick();
	}

	private int ticks;
	private int ticks2;

	@Override
	public void tick() {

		if (ticks2 > 200) {

			if (world.getClosestPlayer(this, 20) != null) {

				PlayerEntity closest = world.getClosestPlayer(this, 20);

				double d = this.squaredDistanceTo(closest);

				double h = Math.sqrt(Math.sqrt(d)) * 0.5;

				double e = closest.getX() - this.getX();
				double f = closest.getBodyY(0.5) - this.getBodyY(0.5);
				double g = closest.getZ() - this.getZ();

				SmallFireballEntity smallFireballEntity = new SmallFireballEntity(world, this,
						this.getRandom().nextTriangular(e, 2.297 * h), f,
						this.getRandom().nextTriangular(g, 2.297 * h));
				smallFireballEntity.setPosition(smallFireballEntity.getX(), this.getBodyY(0.5) + 0.1,
						smallFireballEntity.getZ());
				this.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);
				world.spawnEntity(smallFireballEntity);
			}

			ticks2 = 0;
		}

		if (ticks > 100) {

			int rnd = new Random().nextInt(99);

			ItemEntity itemEntity;

			if (rnd < 89) {
				itemEntity = new ItemEntity(world, this.getX(), this.getY(),
						this.getZ(), new ItemStack(Items.BLAZE_ROD, 1));
				itemEntity.setPickupDelay(30);
			} else {

				ItemStack item = new ItemStack(Items.BOW);
				item.addEnchantment(Enchantments.INFINITY, 1);
				item.addEnchantment(Enchantments.FLAME, 1);
				item.setCount(1);

				itemEntity = new ItemEntity(world, this.getX(), this.getY(),
						this.getZ(), item);
				itemEntity.setPickupDelay(30);
			}

			world.spawnEntity(itemEntity);

			ticks = 0;
		}

		if (this.world.isClient()) {

			if (this.shouldWalk()) {
				this.walkingAnimationState.startIfNotRunning(this.age);
			} else {
				this.walkingAnimationState.stop();
			}
			if (this.shouldSwim()) {
				this.idlingInWaterAnimationState.stop();
				this.swimmingAnimationState.startIfNotRunning(this.age);
			} else if (this.isInsideWaterOrBubbleColumn()) {
				this.swimmingAnimationState.stop();
				this.idlingInWaterAnimationState.startIfNotRunning(this.age);
			} else {
				this.swimmingAnimationState.stop();
				this.idlingInWaterAnimationState.stop();
			}
		}

		ticks++;
		ticks2++;
		super.tick();
	}

	private AttributeContainer attributeContainer;

	@Override
	public AttributeContainer getAttributes() {
		if (attributeContainer == null)
			attributeContainer = new AttributeContainer(MobEntity.createMobAttributes()
					.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1.5)
					.add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10.0)
					.build());
		return attributeContainer;
	}

	private boolean shouldWalk() {
		return this.onGround && this.getVelocity().horizontalLengthSquared() > 1.0E-6
				&& !this.isInsideWaterOrBubbleColumn();
	}

	private boolean shouldSwim() {
		return this.getVelocity().horizontalLengthSquared() > 1.0E-6 && this.isInsideWaterOrBubbleColumn();
	}

	@Override
	public void onTrackedDataSet(TrackedData<?> data) {
		if (POSE.equals(data)) {
			EntityPose entityPose = this.getPose();
			if (entityPose == EntityPose.LONG_JUMPING) {
				this.longJumpingAnimationState.start(this.age);
			} else {
				this.longJumpingAnimationState.stop();
			}
			if (entityPose == EntityPose.CROAKING) {
				this.croakingAnimationState.start(this.age);
			} else {
				this.croakingAnimationState.stop();
			}
			if (entityPose == EntityPose.USING_TONGUE) {
				this.usingTongueAnimationState.start(this.age);
			} else {
				this.usingTongueAnimationState.stop();
			}
		}
		super.onTrackedDataSet(data);
	}

	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
			@Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
		RegistryEntry<Biome> registryEntry = world.getBiome(this.getBlockPos());
		if (registryEntry.isIn(BiomeTags.SPAWNS_COLD_VARIANT_FROGS)) {
			this.setVariant(FrogVariant.COLD);
		} else if (registryEntry.isIn(BiomeTags.SPAWNS_WARM_VARIANT_FROGS)) {
			this.setVariant(FrogVariant.WARM);
		} else {
			this.setVariant(FrogVariant.TEMPERATE);
		}
		return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
	}

	@Override
	@Nullable
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_FROG_AMBIENT;
	}

	@Override
	@Nullable
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_FROG_HURT;
	}

	@Override
	@Nullable
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_FROG_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(SoundEvents.ENTITY_FROG_STEP, 0.15f, 1.0f);
	}

	@Override
	public boolean isPushedByFluids() {
		return false;
	}

	@Override
	protected void sendAiDebugData() {
		super.sendAiDebugData();
		DebugInfoSender.sendBrainDebugData(this);
	}

	@Override
	protected int computeFallDamage(float fallDistance, float damageMultiplier) {
		return super.computeFallDamage(fallDistance, damageMultiplier) - 5;
	}

	@Override
	public void travel(Vec3d movementInput) {
		if (this.canMoveVoluntarily() && this.isTouchingWater()) {
			this.updateVelocity(this.getMovementSpeed(), movementInput);
			this.move(MovementType.SELF, this.getVelocity());
			this.setVelocity(this.getVelocity().multiply(0.9));
		} else {
			super.travel(movementInput);
		}
	}

	@Override
	public boolean canJumpToNextPathNode(PathNodeType type) {
		return super.canJumpToNextPathNode(type) && type != PathNodeType.WATER_BORDER;
	}

	@Override
	protected EntityNavigation createNavigation(World world) {
		return new FrogSwimNavigation(this, world);
	}

	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return SLIME_BALL.test(stack);
	}

	public static boolean canSpawn(EntityType<? extends AnimalEntity> type, WorldAccess world, SpawnReason reason,
			BlockPos pos, Random random) {
		return world.getBlockState(pos.down()).isIn(BlockTags.FROGS_SPAWNABLE_ON)
				&& FrogEntity.isLightLevelValidForNaturalSpawn(world, pos);
	}

	class FrogLookControl
			extends LookControl {
		FrogLookControl(MobEntity entity) {
			super(entity);
		}

		@Override
		protected boolean shouldStayHorizontal() {
			return BlazeFrogEntity.this.getFrogTarget().isEmpty();
		}
	}

	static class FrogSwimNavigation
			extends AxolotlSwimNavigation {
		FrogSwimNavigation(FrogEntity frog, World world) {
			super(frog, world);
		}

		@Override
		protected PathNodeNavigator createPathNodeNavigator(int range) {
			this.nodeMaker = new FrogSwimPathNodeMaker(true);
			this.nodeMaker.setCanEnterOpenDoors(true);
			return new PathNodeNavigator(this.nodeMaker, range);
		}
	}

	static class FrogSwimPathNodeMaker
			extends AmphibiousPathNodeMaker {
		private final BlockPos.Mutable pos = new BlockPos.Mutable();

		public FrogSwimPathNodeMaker(boolean bl) {
			super(bl);
		}

		@Override
		@Nullable
		public PathNode getStart() {
			return this.getStart(new BlockPos(MathHelper.floor(this.entity.getBoundingBox().minX),
					MathHelper.floor(this.entity.getBoundingBox().minY),
					MathHelper.floor(this.entity.getBoundingBox().minZ)));
		}

		@Override
		public PathNodeType getDefaultNodeType(BlockView world, int x, int y, int z) {
			this.pos.set(x, y - 1, z);
			BlockState blockState = world.getBlockState(this.pos);
			if (blockState.isIn(BlockTags.FROG_PREFER_JUMP_TO)) {
				return PathNodeType.OPEN;
			}
			return super.getDefaultNodeType(world, x, y, z);
		}
	}
}
