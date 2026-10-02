/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.simibubi.create.content.kinetics.base.KineticBlockEntity
 *  com.simibubi.create.foundation.blockEntity.SmartBlockEntity
 *  com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour
 *  com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform
 *  com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBehaviour$ValueSettings
 *  com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBoard
 *  com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsFormatter
 *  com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour
 *  dev.engine_room.flywheel.lib.transform.PoseTransformStack
 *  dev.engine_room.flywheel.lib.transform.TransformStack
 *  dev.ryanhcode.sable.Sable
 *  dev.ryanhcode.sable.api.block.BlockEntitySubLevelActor
 *  dev.ryanhcode.sable.api.math.OrientedBoundingBox3d
 *  dev.ryanhcode.sable.api.physics.force.ForceTotal
 *  dev.ryanhcode.sable.api.physics.handle.RigidBodyHandle
 *  dev.ryanhcode.sable.api.physics.mass.MassData
 *  dev.ryanhcode.sable.companion.math.JOMLConversion
 *  dev.ryanhcode.sable.companion.math.Pose3d
 *  dev.ryanhcode.sable.companion.math.Pose3dc
 *  dev.ryanhcode.sable.mixinterface.clip_overwrite.ClipContextExtension
 *  dev.ryanhcode.sable.physics.config.block_properties.PhysicsBlockPropertyHelper
 *  dev.ryanhcode.sable.sublevel.ServerSubLevel
 *  dev.ryanhcode.sable.sublevel.SubLevel
 *  it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
 *  net.createmod.catnip.math.AngleHelper
 *  net.createmod.catnip.math.VecHelper
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.core.Direction$AxisDirection
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.Position
 *  net.minecraft.core.Vec3i
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.util.Mth
 *  net.minecraft.world.Clearable
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.DyeColor
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.neoforged.neoforge.network.PacketDistributor
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.joml.Vector3d
 *  org.joml.Vector3dc
 */
package dev.qwxon.tracks.content.blocks.sable_track;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBoard;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsFormatter;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import dev.engine_room.flywheel.lib.transform.PoseTransformStack;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import dev.qwxon.tracks.config.TracksServerConfig;
import dev.qwxon.tracks.TracksClient;
import dev.qwxon.tracks.content.blocks.sable_track.SableTrackBlock;
import dev.qwxon.tracks.content.blocks.sable_track.SableTrackPart;
import dev.qwxon.tracks.content.blocks.sable_track.SableTrackRole;
import dev.qwxon.tracks.content.items.SuspensionKeyItem;
import dev.qwxon.tracks.index.TracksItems;
import dev.qwxon.tracks.network.SelectTrackTuningModePayload;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.api.SubLevelHelper;
import dev.ryanhcode.sable.api.block.BlockEntitySubLevelActor;
import dev.ryanhcode.sable.api.math.OrientedBoundingBox3d;
import dev.ryanhcode.sable.api.physics.force.ForceTotal;
import dev.ryanhcode.sable.api.physics.handle.RigidBodyHandle;
import dev.ryanhcode.sable.api.physics.mass.MassData;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.companion.math.Pose3d;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import dev.ryanhcode.sable.mixinterface.clip_overwrite.ClipContextExtension;
import dev.ryanhcode.sable.physics.config.block_properties.PhysicsBlockPropertyHelper;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.SubLevel;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Position;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Clearable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.joml.Vector3f;

public class SableTrackBlockEntity
extends KineticBlockEntity
implements BlockEntitySubLevelActor,
Clearable {
    private static final MutableComponent SCROLL_OPTION_TITLE = Component.translatable((String)"tracks.scroll_option.track_suspension_strength");
    private static final double MAX_LATERAL_OFFSET = 1.0;
    private static final double LATERAL_OFFSET_STEP = 0.125;
    private static final double MAX_LONGITUDINAL_OFFSET = 1.0;
    private static final double LONGITUDINAL_OFFSET_STEP = 0.125;
    private static final double MAX_HEIGHT_OFFSET = 0.75;
    private static final double HEIGHT_OFFSET_STEP = 0.125;
    private static final int DRIVE_SCAN_RANGE = 16;
    private static final double SLEEP_VELOCITY = 0.08;
    private static final double OFFROAD_SMALL_WHEEL_REST_DISTANCE = 0.65;
    private static final double TRACK_DRIVE_FORCE_SCALE = -0.45;
    private static final double TRACK_MAX_SPRING_IMPULSE_SCALE = 0.9;
    private static final double TRACK_BUMP_STOP_CLEARANCE_SCALE = 0.95;
    private static final double TRACK_BUMP_STOP_FORCE_SCALE = 4.0;
    private static final Collection<SableTrackBlockEntity> QUEUED_TRACKS = new ObjectOpenHashSet();
    private final ForceTotal forceTotal = new ForceTotal();
    private final Vector3d queuedForcePos = new Vector3d();
    private final Vector3d queuedForce = new Vector3d();
    private TrackStrengthValueBehaviour strength;
    private double extension;
    private double lastExtension = this.extension = 0.65;
    private double lateralOffset;
    private double lastLateralOffset = this.lateralOffset = 0.0;
    private double longitudinalOffset;
    private double lastLongitudinalOffset = this.longitudinalOffset = 0.0;
    private double heightOffset;
    private double lastHeightOffset = this.heightOffset = 0.0;
    private double lastAngle;
    private double angle;
    private double angularVelocity = 0.0;
    private double touchingFriction = 1.0;
    private double springMultiplier = 0.5;
    private double dampingMultiplier = 0.1;
    private double bumpClearanceMultiplier = 0.5;
    private double bumpForceMultiplier = 0.5;
    private double maxImpulseMultiplier = 0.5;
    private double driveMultiplier = 1.0;
    private double gripMultiplier = 1.0;
    private int lastPropagatedStrength = 16;
    private int protectedStrengthValue = 16;
    private String scrollTuningKey = "strength";
    private boolean liftedUp = false;
    private boolean visualSuspensionHidden = false;
    private DyeColor beltColor = null;
    private ItemStack heldItem = ItemStack.EMPTY;

    public SableTrackBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static void applyAllBatchedForces(ServerLevel level, double timeStep) {
        for (SableTrackBlockEntity blockEntity : QUEUED_TRACKS) {
            if (blockEntity.isRemoved()) continue;
            blockEntity.applyBatchedForces();
        }
        QUEUED_TRACKS.clear();
    }

    public float calculateStressApplied() {
        float stress;
        this.lastStressApplied = stress = this.effectiveRole() == SableTrackRole.DRIVE ? 8.0f : 0.0f;
        return stress;
    }

    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        this.strength = new TrackStrengthValueBehaviour((Component)SCROLL_OPTION_TITLE, this, new TrackStrengthValueBox());
        behaviours.add((BlockEntityBehaviour)this.strength);
        this.strength.withCallback(this::onStrengthChanged);
        this.strength.value = 16;
    }

    public void sable$physicsTick(ServerSubLevel subLevel, RigidBodyHandle handle, double timeStep) {
        SableTrackPart part = this.effectivePart();
        if (!part.appliesPhysics()) {
            return;
        }
        Direction facing = (Direction)this.getBlockState().getValue(SableTrackBlock.HORIZONTAL_FACING);
        Vec3 localPos = this.getTrackCenter(facing);
        this.queuedForcePos.set(localPos.x, localPos.y, localPos.z);
        MassData massData = subLevel.getMassTracker();
        double normalMass = 1.0 / massData.getInverseNormalMass((Vector3dc)this.queuedForcePos, OrientedBoundingBox3d.UP);
        double effectiveStrength = this.strength.getValue();
        double normalMassScaling = Math.min(normalMass / effectiveStrength, 1.0) * 8.0;
        double strengthMul = effectiveStrength * normalMassScaling * 2.0;
        double springStrength = effectiveStrength * normalMassScaling * 40.0 * this.springMultiplier;
        double dampingStrength = effectiveStrength * normalMassScaling * 10.0 * this.dampingMultiplier;
        Pose3d pose = subLevel.logicalPose();
        Direction.Axis axis = facing.getAxis();
        Vec3i side = Direction.get((Direction.AxisDirection)Direction.AxisDirection.POSITIVE, (Direction.Axis)axis).getNormal();
        Vector3dc sideD = this.getRotatedAxis(side);
        side = new Vec3i(side.getZ(), 0, side.getX());
        Vector3dc forwardD = this.getRotatedAxis(side);
        boolean isDrive = part.role() == SableTrackRole.DRIVE;
        boolean isSuspension = part.role() == SableTrackRole.SUSPENSION;
        boolean debugVisualize = (isDrive || isSuspension) && TracksServerConfig.terrainDebugVisualizationEnabled();
        if (debugVisualize) {
            this.tracks$spawnDebugParticle(localPos, ParticleTypes.END_ROD);
        }
        if (isDrive) {
            springStrength *= 0.4;
            dampingStrength *= 1.2;
        }
        if (!this.isSuspensionActiveForPhysics(part, facing) && !isDrive) {
            TerrainCastResult visualExtensionToTerrain = this.computeMaxExtensionToTerrain(forwardD, (Pose3dc)pose, part.contactSamples(), 0.35, false, debugVisualize);
            double visualExtension = visualExtensionToTerrain.maxExtension() - part.radius();
            this.extension = Mth.lerp((double)0.7, (double)this.extension, (double)Mth.clamp((double)visualExtension, (double)-0.45, (double)part.suspensionTravel()));
            return;
        }
        double suspensionRestDistance = isDrive ? part.radius() : 0.65;
        TerrainCastResult extensionToTerrain = this.computeMaxExtensionToTerrain(forwardD, (Pose3dc)pose, isDrive ? part.contactSamples() : 1, isDrive ? 0.35 : 1.0, isDrive, debugVisualize);
        double maxExtension = extensionToTerrain.maxExtension();
        double springHeightCompensation = 0.0;
        double springMaxExtension = maxExtension - 0.0;
        if (!isDrive) {
            this.extension = Mth.lerp((double)1.0, (double)this.extension, (double)maxExtension);
        } else {
            this.extension = 0.0;
        }
        if (springMaxExtension > suspensionRestDistance + part.radius() + 0.25) {
            if (!isDrive) {
                this.extension = 0.65;
            }
            return;
        }
        double distance = 0.10833333333333334 + springMaxExtension;
        double springLength = Mth.clamp((double)(distance - part.radius()), (double)0.0, (double)suspensionRestDistance);
        Vector3d velocity = Sable.HELPER.getVelocity(this.level, JOMLConversion.toJOML((Position)localPos));
        Vector3d localVelocity = pose.transformNormalInverse(velocity);
        double dampingForce = -localVelocity.y * dampingStrength;
        double springError = suspensionRestDistance - springLength;
        double bumpStopClearance = part.radius() * 0.95 * this.bumpClearanceMultiplier;
        boolean deeplyBuried = springMaxExtension < part.radius() * 0.35;
        double bumpStopError = 0.0;
        double bumpStopForce = 0.0 * springStrength * 4.0 * this.bumpForceMultiplier;
        double unclampedSpringForce = (springError * springStrength + bumpStopForce + dampingForce) * timeStep;
        double baseMaxSpringImpulse = isDrive ? 35.0 : 30.0;
        double buriedImpulseScale = deeplyBuried ? 0.2 : 1.0;
        double maxSpringImpulse = Math.max(baseMaxSpringImpulse, normalMass * 0.9) * this.maxImpulseMultiplier * buriedImpulseScale;
        double springForce = Mth.clamp((double)unclampedSpringForce, (double)(-maxSpringImpulse), (double)maxSpringImpulse);
        Vec3i rayHitNormal = extensionToTerrain.normal().getNormal();
        Vec3 localForce = new Vec3(springForce * (double)rayHitNormal.getX(), springForce * (double)rayHitNormal.getY(), springForce * (double)rayHitNormal.getZ());
        if (extensionToTerrain.subLevel() != null) {
            localForce = extensionToTerrain.subLevel().logicalPose().transformNormal(localForce);
        }
        localForce = pose.transformNormalInverse(localForce);
        this.queuedForce.set(localForce.x, localForce.y, localForce.z);
        this.touchingFriction = extensionToTerrain.minInteractingBlock() != null ? SableTrackBlockEntity.fudgeFriction(PhysicsBlockPropertyHelper.getFriction((BlockState)this.level.getBlockState(extensionToTerrain.minInteractingBlock()))) : 1.0;
        double brakeStrength = (double)this.level.getSignal(this.getBlockPos().above(), Direction.DOWN) / 15.0;
        double surfaceBraking = Math.min(this.touchingFriction, 1.0);
        double brakingFrictionStrength = (0.075 + brakeStrength * 0.3) * surfaceBraking * part.sideGripMultiplier();
        float kineticSpeed = Math.abs(this.getSharedTrackSpeed(facing)) < 0.05f ? 0.0f : this.getSharedTrackSpeed(facing);
        this.queuedForce.fma(localVelocity.dot(forwardD) * -brakingFrictionStrength * strengthMul * timeStep + (double)kineticSpeed * (1.0 - brakeStrength) * surfaceBraking * -0.45 * part.driveMultiplier() * this.driveMultiplier * timeStep, forwardD);
        this.queuedForce.fma(localVelocity.dot(sideD) * -0.6 * this.touchingFriction * strengthMul * part.sideGripMultiplier() * this.gripMultiplier * timeStep, sideD);
        if (this.queuedForce.lengthSquared() < 1.0E-10) {
            return;
        }
        this.forceTotal.applyImpulseAtPoint(subLevel, (Vector3dc)this.queuedForcePos, (Vector3dc)this.queuedForce);
        QUEUED_TRACKS.add(this);
    }

    public static double fudgeFriction(double realValue) {
        if (realValue < 1.0) {
            return 0.1 + 0.9 * realValue;
        }
        return realValue;
    }

    public void tick() {
        super.tick();
        this.lastLateralOffset = this.lateralOffset;
        this.lastLongitudinalOffset = this.longitudinalOffset;
        this.lastHeightOffset = this.heightOffset;
        if (!this.level.isClientSide) {
            this.syncSuspensionModelState();
        }
        this.propagateTrackStrengthIfChanged();
        SableTrackPart part = this.effectivePart();
        if (!part.appliesPhysics()) {
            return;
        }
        this.lastExtension = this.extension;
        this.extension = Mth.lerp((double)0.7, (double)this.extension, (double)this.computeMaxExtension(part));
        Direction facing = (Direction)this.getBlockState().getValue(SableTrackBlock.HORIZONTAL_FACING);
        float speed = this.getSharedTrackSpeed(facing);
        double rpt = (double)(-speed) * Math.PI * 2.0 / 60.0 / 20.0 * (double)(15 - this.level.getSignal(this.getBlockPos().above(), Direction.DOWN)) / 15.0;
        double attemptedAngularVelocity = Mth.lerp((double)0.2, (double)this.angularVelocity, (double)rpt);
        if (!this.level.isClientSide) {
            return;
        }
        SubLevel subLevel = Sable.HELPER.getContaining((BlockEntity)this);
        if (subLevel == null || this.liftedUp) {
            this.angularVelocity = attemptedAngularVelocity;
            this.lastAngle = this.angle;
            this.angle += this.angularVelocity;
            return;
        }
        Vector3d velocity = Sable.HELPER.getVelocity(this.level, JOMLConversion.toJOML((Position)this.getTrackCenter(facing)));
        Vector3d localVelocity = subLevel.logicalPose().transformNormalInverse(velocity).div(20.0);
        Direction.Axis axis = facing.getAxis();
        Vec3i forward = Direction.get((Direction.AxisDirection)Direction.AxisDirection.POSITIVE, (Direction.Axis)axis).getNormal();
        forward = new Vec3i(forward.getZ(), 0, forward.getX());
        Vector3dc forwardD = this.getRotatedAxis(forward);
        double translation = localVelocity.dot(forwardD);
        double circumference = Math.PI * part.radius() * 2.0;
        double angularDelta = translation / circumference * Math.PI * 2.0;
        if (this.touchingFriction < 1.0) {
            angularDelta = Mth.lerp((double)this.touchingFriction, (double)attemptedAngularVelocity, (double)angularDelta);
        }
        this.lastAngle = this.angle;
        this.angle += angularDelta;
        this.angularVelocity = angularDelta;
    }

    private double computeMaxExtension(SableTrackPart part) {
        if (part.role() == SableTrackRole.DRIVE) {
            return 0.0;
        }
        SubLevel subLevel = Sable.HELPER.getContaining((BlockEntity)this);
        if (subLevel == null) {
            return part.suspensionTravel();
        }
        Direction facing = (Direction)this.getBlockState().getValue(SableTrackBlock.HORIZONTAL_FACING);
        Direction.Axis axis = facing.getAxis();
        Vec3i forward = Direction.get((Direction.AxisDirection)Direction.AxisDirection.POSITIVE, (Direction.Axis)axis).getNormal();
        Vector3dc forwardD = this.getRotatedAxis(forward = new Vec3i(forward.getZ(), 0, forward.getX()));
        TerrainCastResult extensionToTerrain = this.computeMaxExtensionToTerrain(forwardD, (Pose3dc)subLevel.logicalPose(), part.contactSamples());
        double unclampedExtension = extensionToTerrain.maxExtension() - part.radius();
        this.liftedUp = unclampedExtension > part.suspensionTravel();
        this.touchingFriction = extensionToTerrain.minInteractingBlock() == null ? 1.0 : SableTrackBlockEntity.fudgeFriction(PhysicsBlockPropertyHelper.getFriction((BlockState)this.level.getBlockState(extensionToTerrain.minInteractingBlock())));
        return Mth.clamp((double)unclampedExtension, (double)-0.45, (double)part.suspensionTravel());
    }

    private TerrainCastResult computeMaxExtensionToTerrain(Vector3dc forwardD, Pose3dc pose, int contactSamples, double sampleSpacing, boolean allowWalls) {
        return this.computeMaxExtensionToTerrain(forwardD, pose, contactSamples, sampleSpacing, allowWalls, false);
    }

    private TerrainCastResult computeMaxExtensionToTerrain(Vector3dc forwardD, Pose3dc pose, int contactSamples, double sampleSpacing, boolean allowWalls, boolean debug) {
        Direction facing = (Direction)this.getBlockState().getValue(SableTrackBlock.HORIZONTAL_FACING);
        Vec3 trackPosCenter = this.getTrackCenter(facing);
        Vec3 forwardVec = JOMLConversion.toMojang((Vector3dc)forwardD);
        double minExtension = 5.0;
        Direction minNormal = Direction.UP;
        SubLevel minHitSubLevel = null;
        BlockPos minInteractingBlock = null;
        for (int i = -contactSamples; i <= contactSamples; ++i) {
            Vec3 samplePos = trackPosCenter.add(forwardVec.scale((double)i * sampleSpacing));
            TerrainCastResult sample = this.tracks$verticalSampleProbe(samplePos, trackPosCenter.y, pose, allowWalls, debug);
            if (sample.maxExtension() >= minExtension) continue;
            minExtension = sample.maxExtension();
            minNormal = sample.normal();
            minHitSubLevel = sample.subLevel();
            minInteractingBlock = sample.minInteractingBlock();
        }
        if (allowWalls) {
            for (int side = -1; side <= 1; side += 2) {
                TerrainCastResult stepResult = this.tracks$probeForwardStep(trackPosCenter, forwardVec.scale((double)side), pose, debug);
                if (stepResult.maxExtension() >= minExtension) continue;
                minExtension = stepResult.maxExtension();
                minNormal = stepResult.normal();
                minHitSubLevel = stepResult.subLevel();
                minInteractingBlock = stepResult.minInteractingBlock();
            }
        }
        return new TerrainCastResult(minExtension, minNormal, minHitSubLevel, minInteractingBlock);
    }

    // Straight-down raycasts can only sample terrain height at discrete points, so they never actually
    // intersect a step's vertical face (its normal is horizontal, parallel to the ray) - the wheel can only
    // "notice" a step once a sample has already landed on top of it. This horizontal probe casts forward at
    // axle height to hit that vertical face directly, then samples straight down just past it to read the
    // step's actual height, closing the blind spot between vertical samples.
    private TerrainCastResult tracks$probeForwardStep(Vec3 trackPosCenter, Vec3 forwardVec, Pose3dc pose, boolean debug) {
        double probeDistance = 0.75;
        Vec3 wallStart = trackPosCenter;
        Vec3 wallEnd = trackPosCenter.add(forwardVec.scale(probeDistance));
        ClipContext wallContext = new ClipContext(wallStart, wallEnd, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty());
        ((ClipContextExtension)wallContext).sable$setSubLevelIgnoring(this.tracks$connectedSubLevelIgnorePredicate());
        BlockHitResult wallResult = this.level.clip(wallContext);
        if (wallResult.getType() == HitResult.Type.MISS) {
            if (debug) {
                this.tracks$drawDebugRay(wallStart, wallEnd, false);
            }
            return new TerrainCastResult(5.0, Direction.UP, null, null);
        }
        if (debug) {
            this.tracks$drawDebugRay(wallStart, wallResult.getLocation(), true);
        }
        Vec3 pastWall = wallResult.getLocation().add(forwardVec.normalize().scale(0.15));
        return this.tracks$verticalSampleProbe(pastWall, trackPosCenter.y, pose, true, debug);
    }

    private TerrainCastResult tracks$verticalSampleProbe(Vec3 samplePos, double trackPosCenterY, Pose3dc pose, boolean allowWalls, boolean debug) {
        double dist;
        Vec3 rayStart = samplePos.add(0.0, 1.25, 0.0);
        Vec3 rayEnd = samplePos.subtract(0.0, 5.0, 0.0);
        ClipContext clipContext = new ClipContext(rayStart, rayEnd, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty());
        ((ClipContextExtension)clipContext).sable$setSubLevelIgnoring(this.tracks$connectedSubLevelIgnorePredicate());
        BlockHitResult clipResult = this.level.clip(clipContext);
        if (clipResult.getType() == HitResult.Type.MISS) {
            if (debug) {
                this.tracks$drawDebugRay(rayStart, rayEnd, false);
            }
            return new TerrainCastResult(5.0, Direction.UP, null, null);
        }
        SubLevel hitSubLevel = Sable.HELPER.getContaining(this.level, (Position)clipResult.getLocation());
        Vec3 localHitPos = pose.transformPositionInverse(hitSubLevel == null ? clipResult.getLocation() : hitSubLevel.logicalPose().transformPosition(clipResult.getLocation()));
        if (hitSubLevel != null && localHitPos.y >= trackPosCenterY - 1.0E-4 || samplePos.distanceTo(localHitPos) < 0.05 || (dist = Math.max(0.0, trackPosCenterY - localHitPos.y)) <= 1.0E-5 && localHitPos.y <= trackPosCenterY) {
            if (debug) {
                this.tracks$drawDebugRay(rayStart, clipResult.getLocation(), false);
            }
            return new TerrainCastResult(5.0, Direction.UP, null, null);
        }
        Direction dir = clipResult.getDirection();
        Vector3d hitNormal = new Vector3d((double)dir.getStepX(), (double)dir.getStepY(), (double)dir.getStepZ());
        if (hitSubLevel != null) {
            hitSubLevel.logicalPose().transformNormal(hitNormal);
        }
        pose.transformNormalInverse(hitNormal);
        if (!allowWalls && hitNormal.dot(0.0, 1.0, 0.0) < 0.5) {
            if (debug) {
                this.tracks$drawDebugRay(rayStart, clipResult.getLocation(), false);
            }
            return new TerrainCastResult(5.0, Direction.UP, null, null);
        }
        if (debug) {
            this.tracks$drawDebugRay(rayStart, clipResult.getLocation(), true);
        }
        return new TerrainCastResult(dist, clipResult.getDirection(), hitSubLevel, clipResult.getBlockPos());
    }

    private TerrainCastResult computeMaxExtensionToTerrain(Vector3dc forwardD, Pose3dc pose, int contactSamples) {
        return this.computeMaxExtensionToTerrain(forwardD, pose, contactSamples, 0.35, false);
    }

    private TerrainCastResult computeMaxExtensionToTerrain(Vector3dc forwardD, Pose3dc pose, int contactSamples, double sampleSpacing) {
        return this.computeMaxExtensionToTerrain(forwardD, pose, contactSamples, sampleSpacing, false);
    }

    // sable$setIgnoredSubLevel only excludes this block entity's own sub-level from the clip. Anything
    // joined to it via a constraint/bearing (e.g. an attached car body sitting above this wheel) lives in a
    // different SubLevel and would otherwise get hit and misread as terrain. Ignoring the whole connected
    // chain instead treats attached structures as "part of us", not ground.
    private Predicate<SubLevel> tracks$connectedSubLevelIgnorePredicate() {
        SubLevel mySubLevel = Sable.HELPER.getContaining((BlockEntity)this);
        if (mySubLevel == null) {
            return subLevel -> false;
        }
        Collection<SubLevel> connectedChain = SubLevelHelper.getConnectedChain(mySubLevel);
        return connectedChain::contains;
    }

    private void tracks$spawnDebugParticle(Vec3 pos, ParticleOptions type) {
        if (this.level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(type, pos.x, pos.y, pos.z, 1, 0.0, 0.0, 0.0, 0.0);
        }
    }

    private void tracks$drawDebugRay(Vec3 start, Vec3 end, boolean accepted) {
        if (!(this.level instanceof ServerLevel serverLevel)) {
            return;
        }
        DustParticleOptions rayDust = new DustParticleOptions(new Vector3f(0.3f, 0.75f, 1.0f), 0.6f);
        for (int step = 0; step <= 8; ++step) {
            double t = (double)step / 8.0;
            Vec3 point = start.lerp(end, t);
            serverLevel.sendParticles(rayDust, point.x, point.y, point.z, 1, 0.0, 0.0, 0.0, 0.0);
        }
        DustParticleOptions markerDust = new DustParticleOptions(accepted ? new Vector3f(0.2f, 1.0f, 0.2f) : new Vector3f(1.0f, 0.2f, 0.2f), 1.0f);
        serverLevel.sendParticles(markerDust, end.x, end.y, end.z, 1, 0.0, 0.0, 0.0, 0.0);
    }

    private void applyBatchedForces() {
        SubLevel subLevel = Sable.HELPER.getContaining((BlockEntity)this);
        if (subLevel == null) {
            return;
        }
        RigidBodyHandle.of((ServerSubLevel)((ServerSubLevel)subLevel)).applyForcesAndReset(this.forceTotal);
    }

    @NotNull
    private Vector3dc getRotatedAxis(Vec3i normal) {
        return new Vector3d((double)normal.getX(), (double)normal.getY(), (double)normal.getZ());
    }

    private Vec3 getTrackCenter(Direction facing) {
        double physicsYOffset = -0.5;
        double currentHeightOffset = this.heightOffset;
        if (this.effectiveRole() == SableTrackRole.DRIVE) {
            Direction trackSide = facing.getClockWise();
            boolean frontEnd = !this.hasTrackNeighbor(trackSide) && this.hasTrackNeighbor(trackSide.getOpposite());
            if (frontEnd) {
                physicsYOffset = 0.35;
            } else {
                physicsYOffset = 0.25;
            }
        }
        return this.getBlockPos().relative(facing).getCenter().add(Vec3.atLowerCornerOf((Vec3i)facing.getClockWise().getNormal()).scale(this.lateralOffset)).add(Vec3.atLowerCornerOf((Vec3i)facing.getNormal()).scale(this.longitudinalOffset)).add(0.0, currentHeightOffset + physicsYOffset, 0.0);
    }

    public SableTrackRole baseRole() {
        Block block = this.getBlockState().getBlock();
        if (block instanceof SableTrackBlock) {
            SableTrackBlock trackBlock = (SableTrackBlock)block;
            return trackBlock.role();
        }
        return SableTrackRole.MOUNT;
    }

    public SableTrackPart effectivePart() {
        return SableTrackPart.fromStack(this.heldItem);
    }

    public SableTrackRole effectiveRole() {
        return this.effectivePart().role();
    }

    public double adjustLateralOffset(int direction) {
        double previous = this.lateralOffset;
        this.lateralOffset = Mth.clamp((double)((double)Math.round((this.lateralOffset + (double)direction * 0.125) / 0.125) * 0.125), (double)-1.0, (double)1.0);
        if (Math.abs(previous - this.lateralOffset) > 1.0E-6) {
            this.setChanged();
            if (this.level != null && !this.level.isClientSide) {
                this.sendData();
                this.applyOffsetToConnectedTrack("lateral", this.lateralOffset, false);
            }
        }
        return this.lateralOffset;
    }

    public double adjustLongitudinalOffset(int direction) {
        double previous = this.longitudinalOffset;
        this.longitudinalOffset = Mth.clamp((double)((double)Math.round((this.longitudinalOffset + (double)direction * 0.125) / 0.125) * 0.125), (double)-1.0, (double)1.0);
        if (Math.abs(previous - this.longitudinalOffset) > 1.0E-6) {
            this.setChanged();
            if (this.level != null && !this.level.isClientSide) {
                this.sendData();
                this.applyOffsetToConnectedTrack("longitudinal", this.longitudinalOffset, false);
            }
        }
        return this.longitudinalOffset;
    }

    public double adjustHeightOffset(int direction, boolean sideInteraction) {
        double previous = this.heightOffset;
        this.heightOffset = Mth.clamp((double)((double)Math.round((this.heightOffset + (double)direction * 0.125) / 0.125) * 0.125), (double)-0.75, (double)0.75);
        if (Math.abs(previous - this.heightOffset) > 1.0E-6) {
            this.setChanged();
            if (this.level != null && !this.level.isClientSide) {
                this.sendData();
                this.applyOffsetToConnectedTrack("height", this.heightOffset, sideInteraction);
            }
        }
        return this.heightOffset;
    }

    public double adjustTuning(String key, int direction) {
        if (key.equals("strength")) {
            int current = this.strength == null ? 16 : this.strength.getValue();
            int next = Mth.clamp((int)(current + direction * 5), (int)5, (int)180);
            if (this.strength != null) {
                this.strength.value = next;
            }
            if (this.level != null && !this.level.isClientSide) {
                this.applyStrengthToConnectedTrack(next);
            } else {
                this.onTuningChanged();
            }
            return next;
        }
        double step = key.equals("drive") ? 0.1 : 0.05;
        double previous = this.getTuning(key);
        double next = Mth.clamp((double)((double)Math.round((previous + (double)direction * step) / step) * step), (double)0.1, (double)4.0);
        switch (key) {
            case "spring": {
                this.springMultiplier = next;
                break;
            }
            case "damping": {
                this.dampingMultiplier = next;
                break;
            }
            case "bump_clearance": {
                this.bumpClearanceMultiplier = next;
                break;
            }
            case "bump_force": {
                this.bumpForceMultiplier = next;
                break;
            }
            case "max_impulse": {
                this.maxImpulseMultiplier = next;
                break;
            }
            case "drive": {
                this.driveMultiplier = next;
                break;
            }
            case "grip": {
                this.gripMultiplier = next;
                break;
            }
            default: {
                return previous;
            }
        }
        if (this.level != null && !this.level.isClientSide) {
            this.applyTuningToConnectedTrack(key, next);
        } else {
            this.onTuningChanged();
        }
        return next;
    }

    public double getTuning(String key) {
        return switch (key) {
            case "strength" -> {
                if (this.strength == null) {
                    yield 16.0;
                }
                yield this.strength.getValue();
            }
            case "spring" -> this.springMultiplier;
            case "damping" -> this.dampingMultiplier;
            case "bump_clearance" -> this.bumpClearanceMultiplier;
            case "bump_force" -> this.bumpForceMultiplier;
            case "max_impulse" -> this.maxImpulseMultiplier;
            case "drive" -> this.driveMultiplier;
            case "grip" -> this.gripMultiplier;
            default -> 1.0;
        };
    }

    private void onTuningChanged() {
        this.setChanged();
        if (this.level != null && !this.level.isClientSide) {
            this.sendData();
        }
    }

    public void selectScrollTuningMode(String key) {
        this.scrollTuningKey = key;
        if (this.strength == null) {
            return;
        }
        if ("strength".equals(key)) {
            this.strength.value = this.protectedStrengthValue;
            this.lastPropagatedStrength = this.protectedStrengthValue;
        } else {
            this.protectedStrengthValue = this.lastPropagatedStrength;
            this.strength.value = this.level != null && this.level.isClientSide ? SableTrackBlockEntity.tuningToScroll(this.getTuning(key)) : this.protectedStrengthValue;
        }
    }

    public double getLerpedExtension(float partialTicks) {
        return Mth.lerp((double)partialTicks, (double)this.lastExtension, (double)this.extension);
    }

    public double getLerpedLateralOffset(float partialTicks) {
        return Mth.lerp((double)partialTicks, (double)this.lastLateralOffset, (double)this.lateralOffset);
    }

    public double getLerpedLongitudinalOffset(float partialTicks) {
        return Mth.lerp((double)partialTicks, (double)this.lastLongitudinalOffset, (double)this.longitudinalOffset);
    }

    public double getLerpedHeightOffset(float partialTicks) {
        return Mth.lerp((double)partialTicks, (double)this.lastHeightOffset, (double)this.heightOffset);
    }

    public float getLerpedAngle(float partialTicks) {
        return (float)Mth.lerp((double)partialTicks, (double)this.lastAngle, (double)this.angle);
    }

    public float getSharedTrackAngle(Direction facing, float partialTicks) {
        SableTrackBlockEntity track;
        BlockPos candidate;
        BlockEntity blockEntity;
        BlockPos candidate2;
        if (this.level == null) {
            return this.getLerpedAngle(partialTicks);
        }
        Direction along = facing.getClockWise();
        BlockPos start = this.getBlockPos();
        for (int step = 1; step <= 16 && this.isSameTrackLine(candidate2 = start.relative(along.getOpposite()), facing); ++step) {
            start = candidate2;
        }
        SableTrackBlockEntity fallback = this;
        for (int step = 0; step <= 32 && (blockEntity = this.level.getBlockEntity(candidate = start.relative(along, step))) instanceof SableTrackBlockEntity && (track = (SableTrackBlockEntity)blockEntity).getBlockState().getValue(SableTrackBlock.HORIZONTAL_FACING) == facing; ++step) {
            if (!track.effectivePart().appliesPhysics()) continue;
            if (fallback == this) {
                fallback = track;
            }
            if (!(Math.abs(track.angle - track.lastAngle) > 1.0E-6)) continue;
            return track.getLerpedAngle(partialTicks);
        }
        return fallback.getLerpedAngle(partialTicks);
    }

    public ItemStack getHeldItem() {
        return this.heldItem;
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        if (this.heldItem.isEmpty()) {
            return super.getRequiredItems(state);
        }
        return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, this.heldItem);
    }

    public void clearContent() {
        this.heldItem = ItemStack.EMPTY;
    }

    public void setHeldItem(ItemStack heldItem) {
        this.heldItem = heldItem.copy();
        this.setChanged();
        this.invalidateRenderBoundingBox();
        if (this.level != null && !this.level.isClientSide) {
            this.syncSuspensionModelState();
            this.sendData();
        }
    }

    public float getSharedTrackSpeed(Direction facing) {
        float ownSpeed;
        float f = ownSpeed = facing.getAxis() == Direction.Axis.X ? -this.getSpeed() : this.getSpeed();
        if ((double)Math.abs(ownSpeed) > 1.0E-4 && this.effectiveRole() == SableTrackRole.DRIVE) {
            return ownSpeed;
        }
        if (this.level == null) {
            return ownSpeed;
        }
        Direction along = facing.getClockWise();
        for (int step = 1; step <= 16; ++step) {
            float positive = this.driveSpeedAt(this.getBlockPos().relative(along, step), facing);
            if ((double)Math.abs(positive) > 1.0E-4) {
                return positive;
            }
            float negative = this.driveSpeedAt(this.getBlockPos().relative(along.getOpposite(), step), facing);
            if (!((double)Math.abs(negative) > 1.0E-4)) continue;
            return negative;
        }
        return this.effectiveRole() == SableTrackRole.DRIVE ? ownSpeed : 0.0f;
    }

    public boolean isVisualSuspensionHidden() {
        return this.visualSuspensionHidden;
    }

    public DyeColor getBeltColor() {
        return this.beltColor;
    }

    public boolean applyBeltColorToConnectedTrack(DyeColor color) {
        if (this.level == null || this.level.isClientSide) {
            return false;
        }
        boolean changed = this.setBeltColor(color);
        Direction facing = (Direction)this.getBlockState().getValue(SableTrackBlock.HORIZONTAL_FACING);
        Direction along = facing.getClockWise();
        changed |= this.copyBeltColorAlong(along, facing, color);
        return changed |= this.copyBeltColorAlong(along.getOpposite(), facing, color);
    }

    public void resetTuningToConnectedTrack() {
        if (this.level == null || this.level.isClientSide) {
            return;
        }
        this.resetTuning();
        Direction facing = (Direction)this.getBlockState().getValue(SableTrackBlock.HORIZONTAL_FACING);
        Direction along = facing.getClockWise();
        this.resetTuningAlong(along, facing);
        this.resetTuningAlong(along.getOpposite(), facing);
    }

    public void toggleVisualSuspensionHidden() {
        this.visualSuspensionHidden = !this.visualSuspensionHidden;
        this.setChanged();
        this.invalidateRenderBoundingBox();
        if (this.level != null && !this.level.isClientSide) {
            this.sendData();
        }
    }

    private void syncSuspensionModelState() {
        boolean useSuspensionModel = this.effectiveRole() == SableTrackRole.SUSPENSION;
        BlockState state = this.getBlockState();
        if (state.hasProperty((Property)SableTrackBlock.SUSPENSION_MODEL) && (Boolean)state.getValue((Property)SableTrackBlock.SUSPENSION_MODEL) != useSuspensionModel) {
            this.level.setBlock(this.worldPosition, (BlockState)state.setValue((Property)SableTrackBlock.SUSPENSION_MODEL, (Comparable)Boolean.valueOf(useSuspensionModel)), 2);
        }
    }

    private boolean copyBeltColorAlong(Direction direction, Direction facing, DyeColor color) {
        boolean changed = false;
        for (int step = 1; step <= 16; ++step) {
            SableTrackBlockEntity neighbor;
            BlockPos targetPos = this.getBlockPos().relative(direction, step);
            BlockEntity blockEntity = this.level.getBlockEntity(targetPos);
            if (!(blockEntity instanceof SableTrackBlockEntity) || (neighbor = (SableTrackBlockEntity)blockEntity).getBlockState().getValue(SableTrackBlock.HORIZONTAL_FACING) != facing) {
                return changed;
            }
            changed |= neighbor.setBeltColor(color);
        }
        return changed;
    }

    private void resetTuningAlong(Direction direction, Direction facing) {
        for (int step = 1; step <= 16; ++step) {
            SableTrackBlockEntity neighbor;
            BlockPos targetPos = this.getBlockPos().relative(direction, step);
            BlockEntity blockEntity = this.level.getBlockEntity(targetPos);
            if (!(blockEntity instanceof SableTrackBlockEntity) || (neighbor = (SableTrackBlockEntity)blockEntity).getBlockState().getValue(SableTrackBlock.HORIZONTAL_FACING) != facing) {
                return;
            }
            neighbor.resetTuning();
        }
    }

    private void resetTuning() {
        if (this.strength != null) {
            this.strength.value = 16;
        }
        this.lastPropagatedStrength = 16;
        this.protectedStrengthValue = 16;
        this.springMultiplier = 0.5;
        this.dampingMultiplier = 1.0;
        this.bumpClearanceMultiplier = 1.0;
        this.bumpForceMultiplier = 1.0;
        this.maxImpulseMultiplier = 1.0;
        this.driveMultiplier = 1.0;
        this.gripMultiplier = 1.0;
        this.lateralOffset = 0.0;
        this.longitudinalOffset = 0.0;
        this.heightOffset = 0.0;
        this.setChanged();
        if (this.level != null && !this.level.isClientSide) {
            this.sendData();
        }
    }

    private boolean setBeltColor(DyeColor color) {
        if (this.beltColor == color) {
            return false;
        }
        this.beltColor = color;
        this.setChanged();
        this.invalidateRenderBoundingBox();
        if (this.level != null && !this.level.isClientSide) {
            this.sendData();
        }
        return true;
    }

    public double getNeighborExtensionDelta(Direction facing, float partialTicks) {
        SableTrackBlockEntity neighbor;
        if (this.level == null) {
            return 0.0;
        }
        Direction along = facing.getClockWise();
        BlockEntity blockEntity = this.level.getBlockEntity(this.getBlockPos().relative(along));
        if (blockEntity instanceof SableTrackBlockEntity && (neighbor = (SableTrackBlockEntity)blockEntity).getBlockState().getValue(SableTrackBlock.HORIZONTAL_FACING) == facing) {
            return neighbor.getLerpedExtension(partialTicks) - this.getLerpedExtension(partialTicks);
        }
        return 0.0;
    }

    public boolean hasTrackNeighbor(Direction side) {
        BlockEntity blockEntity;
        if (this.level == null || !((blockEntity = this.level.getBlockEntity(this.getBlockPos().relative(side))) instanceof SableTrackBlockEntity)) {
            return false;
        }
        SableTrackBlockEntity neighbor = (SableTrackBlockEntity)blockEntity;
        return neighbor.getBlockState().getValue(SableTrackBlock.HORIZONTAL_FACING) == this.getBlockState().getValue(SableTrackBlock.HORIZONTAL_FACING) && neighbor.effectivePart().appliesPhysics();
    }

    private boolean isSuspensionActiveForPhysics(SableTrackPart part, Direction facing) {
        if (part.role() != SableTrackRole.SUSPENSION) {
            return true;
        }
        SuspensionSegment segment = this.getSuspensionSegment(facing);
        if (segment.count() <= 3) {
            return true;
        }
        return segment.index() == 0 || segment.index() == segment.count() - 1 || segment.index() % 2 == 0;
    }

    private SuspensionSegment getSuspensionSegment(Direction facing) {
        int after;
        int before;
        Direction along = facing.getClockWise();
        for (before = 0; before < 16 && this.isSuspensionAt(this.getBlockPos().relative(along.getOpposite(), before + 1), facing); ++before) {
        }
        for (after = 0; after < 16 && this.isSuspensionAt(this.getBlockPos().relative(along, after + 1), facing); ++after) {
        }
        return new SuspensionSegment(before, before + after + 1);
    }

    private boolean hasSuspensionNeighbor(Direction facing) {
        if (this.level == null) {
            return false;
        }
        Direction along = facing.getClockWise();
        return this.isSuspensionAt(this.getBlockPos().relative(along), facing) || this.isSuspensionAt(this.getBlockPos().relative(along.getOpposite()), facing);
    }

    private boolean isSuspensionAt(BlockPos pos, Direction facing) {
        SableTrackBlockEntity neighbor;
        BlockEntity blockEntity = this.level.getBlockEntity(pos);
        return blockEntity instanceof SableTrackBlockEntity && (neighbor = (SableTrackBlockEntity)blockEntity).getBlockState().getValue(SableTrackBlock.HORIZONTAL_FACING) == facing && neighbor.effectiveRole() == SableTrackRole.SUSPENSION;
    }

    private boolean isSameTrackLine(BlockPos pos, Direction facing) {
        SableTrackBlockEntity neighbor;
        BlockEntity blockEntity = this.level.getBlockEntity(pos);
        return blockEntity instanceof SableTrackBlockEntity && (neighbor = (SableTrackBlockEntity)blockEntity).getBlockState().getValue(SableTrackBlock.HORIZONTAL_FACING) == facing;
    }

    private float driveSpeedAt(BlockPos pos, Direction facing) {
        SableTrackBlockEntity track;
        BlockEntity blockEntity = this.level.getBlockEntity(pos);
        if (!(blockEntity instanceof SableTrackBlockEntity) || (track = (SableTrackBlockEntity)blockEntity).getBlockState().getValue(SableTrackBlock.HORIZONTAL_FACING) != facing || track.effectiveRole() != SableTrackRole.DRIVE) {
            return 0.0f;
        }
        return facing.getAxis() == Direction.Axis.X ? -track.getSpeed() : track.getSpeed();
    }

    private void onStrengthChanged(int value) {
        if (this.level == null || this.level.isClientSide) {
            return;
        }
        if ("strength".equals(this.scrollTuningKey)) {
            this.protectedStrengthValue = value;
            this.applyStrengthToConnectedTrack(value);
            return;
        }
        if (this.strength != null) {
            this.strength.value = this.protectedStrengthValue;
            this.lastPropagatedStrength = this.protectedStrengthValue;
        }
        double tuningValue = SableTrackBlockEntity.scrollToTuning(value);
        this.applyTuningToConnectedTrack(this.scrollTuningKey, tuningValue);
    }

    private void propagateTrackStrengthIfChanged() {
        if (this.level == null || this.level.isClientSide || this.strength == null || !"strength".equals(this.scrollTuningKey)) {
            return;
        }
        int current = this.strength.getValue();
        if (current == this.lastPropagatedStrength) {
            return;
        }
        this.applyStrengthToConnectedTrack(current);
    }

    private void applyStrengthToConnectedTrack(int value) {
        if (this.level == null || this.level.isClientSide || this.strength == null) {
            return;
        }
        this.lastPropagatedStrength = value;
        this.protectedStrengthValue = value;
        this.setChanged();
        this.sendData();
        Direction facing = (Direction)this.getBlockState().getValue(SableTrackBlock.HORIZONTAL_FACING);
        Direction along = facing.getClockWise();
        this.copyStrengthAlong(along, facing, value);
        this.copyStrengthAlong(along.getOpposite(), facing, value);
    }

    private void applyTuningToConnectedTrack(String key, double value) {
        if (this.level == null || this.level.isClientSide) {
            return;
        }
        this.setTuning(key, value);
        Direction facing = (Direction)this.getBlockState().getValue(SableTrackBlock.HORIZONTAL_FACING);
        Direction along = facing.getClockWise();
        this.copyTuningAlong(along, facing, key, value);
        this.copyTuningAlong(along.getOpposite(), facing, key, value);
    }

    private void copyTuningAlong(Direction direction, Direction facing, String key, double value) {
        for (int step = 1; step <= 16; ++step) {
            SableTrackBlockEntity neighbor;
            BlockPos targetPos = this.getBlockPos().relative(direction, step);
            BlockEntity blockEntity = this.level.getBlockEntity(targetPos);
            if (!(blockEntity instanceof SableTrackBlockEntity) || (neighbor = (SableTrackBlockEntity)blockEntity).getBlockState().getValue(SableTrackBlock.HORIZONTAL_FACING) != facing) {
                return;
            }
            neighbor.setTuning(key, value);
        }
    }

    private void applyOffsetToConnectedTrack(String key, double value, boolean sideInteraction) {
        if (this.level == null || this.level.isClientSide) {
            return;
        }
        Direction facing = (Direction)this.getBlockState().getValue(SableTrackBlock.HORIZONTAL_FACING);
        Direction along = facing.getClockWise();
        this.copyOffsetAlong(along, facing, key, value, sideInteraction);
        this.copyOffsetAlong(along.getOpposite(), facing, key, value, sideInteraction);
    }

    private void copyOffsetAlong(Direction direction, Direction facing, String key, double value, boolean sideInteraction) {
        for (int step = 1; step <= 16; ++step) {
            SableTrackBlockEntity neighbor;
            BlockPos targetPos = this.getBlockPos().relative(direction, step);
            BlockEntity blockEntity = this.level.getBlockEntity(targetPos);
            if (!(blockEntity instanceof SableTrackBlockEntity) || (neighbor = (SableTrackBlockEntity)blockEntity).getBlockState().getValue(SableTrackBlock.HORIZONTAL_FACING) != facing) {
                return;
            }
            SableTrackRole role = neighbor.effectiveRole();
            if (role == SableTrackRole.SUSPENSION || (!sideInteraction && role == SableTrackRole.DRIVE)) {
                neighbor.setOffset(key, value);
            }
        }
    }

    private void setOffset(String key, double value) {
        switch (key) {
            case "lateral": {
                this.lateralOffset = value;
                break;
            }
            case "longitudinal": {
                this.longitudinalOffset = value;
                break;
            }
            case "height": {
                this.heightOffset = value;
                break;
            }
            default: {
                return;
            }
        }
        this.setChanged();
        this.invalidateRenderBoundingBox();
        if (this.level != null && !this.level.isClientSide) {
            this.sendData();
        }
    }

    private void setTuning(String key, double value) {
        switch (key) {
            case "spring": {
                this.springMultiplier = value;
                break;
            }
            case "damping": {
                this.dampingMultiplier = value;
                break;
            }
            case "bump_clearance": {
                this.bumpClearanceMultiplier = value;
                break;
            }
            case "bump_force": {
                this.bumpForceMultiplier = value;
                break;
            }
            case "max_impulse": {
                this.maxImpulseMultiplier = value;
                break;
            }
            case "drive": {
                this.driveMultiplier = value;
                break;
            }
            case "grip": {
                this.gripMultiplier = value;
                break;
            }
            default: {
                return;
            }
        }
        this.onTuningChanged();
    }

    private static int tuningToScroll(double value) {
        return Mth.clamp((int)((int)Math.round(5.0 + (Mth.clamp((double)value, (double)0.1, (double)4.0) - 0.1) / 3.9 * 175.0)), (int)5, (int)180);
    }

    private static double scrollToTuning(int value) {
        return 0.1 + (double)(Mth.clamp((int)value, (int)5, (int)180) - 5) / 175.0 * 3.9;
    }

    private void copyStrengthAlong(Direction direction, Direction facing, int value) {
        for (int step = 1; step <= 16; ++step) {
            SableTrackBlockEntity neighbor;
            block4: {
                block3: {
                    BlockPos targetPos = this.getBlockPos().relative(direction, step);
                    BlockEntity blockEntity = this.level.getBlockEntity(targetPos);
                    if (!(blockEntity instanceof SableTrackBlockEntity)) break block3;
                    neighbor = (SableTrackBlockEntity)blockEntity;
                    if (neighbor.strength != null && neighbor.getBlockState().getValue(SableTrackBlock.HORIZONTAL_FACING) == facing) break block4;
                }
                return;
            }
            neighbor.strength.value = value;
            neighbor.lastPropagatedStrength = value;
            neighbor.protectedStrengthValue = value;
            neighbor.setChanged();
            neighbor.sendData();
        }
    }

    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        tag.putDouble("Extension", this.extension);
        tag.putDouble("LateralOffset", this.lateralOffset);
        tag.putDouble("LongitudinalOffset", this.longitudinalOffset);
        tag.putDouble("HeightOffset", this.heightOffset);
        tag.putDouble("SpringMultiplier", this.springMultiplier);
        tag.putDouble("DampingMultiplier", this.dampingMultiplier);
        tag.putDouble("BumpClearanceMultiplier", this.bumpClearanceMultiplier);
        tag.putDouble("BumpForceMultiplier", this.bumpForceMultiplier);
        tag.putDouble("MaxImpulseMultiplier", this.maxImpulseMultiplier);
        tag.putDouble("DriveMultiplier", this.driveMultiplier);
        tag.putDouble("GripMultiplier", this.gripMultiplier);
        tag.putBoolean("VisualSuspensionHidden", this.visualSuspensionHidden);
        if (this.beltColor != null) {
            tag.putString("BeltColor", this.beltColor.getName());
        }
        tag.put("HeldTrackItem", this.heldItem.saveOptional(registries));
        super.write(tag, registries, clientPacket);
    }

    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        double clientExtension = this.extension;
        double clientLastExtension = this.lastExtension;
        if (!clientPacket && tag.contains("Extension")) {
            this.extension = tag.getDouble("Extension");
        }
        if (tag.contains("LateralOffset")) {
            this.lateralOffset = tag.getDouble("LateralOffset");
        }
        if (tag.contains("LongitudinalOffset")) {
            this.longitudinalOffset = tag.getDouble("LongitudinalOffset");
        }
        if (tag.contains("HeightOffset")) {
            this.heightOffset = tag.getDouble("HeightOffset");
        }
        if (tag.contains("SpringMultiplier")) {
            this.springMultiplier = tag.getDouble("SpringMultiplier");
        }
        if (tag.contains("DampingMultiplier")) {
            this.dampingMultiplier = tag.getDouble("DampingMultiplier");
        }
        if (tag.contains("BumpClearanceMultiplier")) {
            this.bumpClearanceMultiplier = tag.getDouble("BumpClearanceMultiplier");
        }
        if (tag.contains("BumpForceMultiplier")) {
            this.bumpForceMultiplier = tag.getDouble("BumpForceMultiplier");
        }
        if (tag.contains("MaxImpulseMultiplier")) {
            this.maxImpulseMultiplier = tag.getDouble("MaxImpulseMultiplier");
        }
        if (tag.contains("DriveMultiplier")) {
            this.driveMultiplier = tag.getDouble("DriveMultiplier");
        }
        if (tag.contains("GripMultiplier")) {
            this.gripMultiplier = tag.getDouble("GripMultiplier");
        }
        if (tag.contains("VisualSuspensionHidden")) {
            this.visualSuspensionHidden = tag.getBoolean("VisualSuspensionHidden");
        }
        this.beltColor = tag.contains("BeltColor") ? DyeColor.byName((String)tag.getString("BeltColor"), null) : null;
        this.heldItem = ItemStack.parseOptional((HolderLookup.Provider)registries, (CompoundTag)tag.getCompound("HeldTrackItem"));
        if (clientPacket) {
            this.extension = clientExtension;
            this.lastExtension = clientLastExtension;
            this.lastLateralOffset = this.lateralOffset;
            this.lastLongitudinalOffset = this.longitudinalOffset;
            this.lastHeightOffset = this.heightOffset;
        }
        super.read(tag, registries, clientPacket);
        if (this.strength != null) {
            this.lastPropagatedStrength = this.protectedStrengthValue = this.strength.getValue();
        }
    }

    protected AABB createRenderBoundingBox() {
        return new AABB(this.getBlockPos()).inflate(3.0 + Math.abs(this.lateralOffset) + Math.abs(this.longitudinalOffset));
    }

    private static class TrackStrengthValueBehaviour
    extends ScrollValueBehaviour {
        private static final int MAX_STRENGTH = 180;
        private final SableTrackBlockEntity owner;

        public TrackStrengthValueBehaviour(Component label, SableTrackBlockEntity be, ValueBoxTransform slot) {
            super(label, (SmartBlockEntity)be, slot);
            this.owner = be;
            this.between(5, 180);
        }

        public ValueSettingsBoard createBoard(Player player, BlockHitResult hitResult) {
            SuspensionKeyItem.TuningMode mode = player.getMainHandItem().is(TracksItems.SUSPENSION_KEY.asItem()) ? SuspensionKeyItem.getMode(player.getMainHandItem()) : (player.getOffhandItem().is(TracksItems.SUSPENSION_KEY.asItem()) ? SuspensionKeyItem.getMode(player.getOffhandItem()) : SuspensionKeyItem.TuningMode.STRENGTH);
            this.owner.selectScrollTuningMode(mode.key);
            if (this.owner.level != null && ((SableTrackBlockEntity)this.owner).level.isClientSide) {
                PacketDistributor.sendToServer((CustomPacketPayload)new SelectTrackTuningModePayload(this.owner.getBlockPos(), mode.key), (CustomPacketPayload[])new CustomPacketPayload[0]);
            }
            this.value = mode == SuspensionKeyItem.TuningMode.STRENGTH ? this.owner.protectedStrengthValue : SableTrackBlockEntity.tuningToScroll(this.owner.getTuning(mode.key));
            return new ValueSettingsBoard(mode.title(), 180, 20, (List)ImmutableList.of((Object)mode.title()), new ValueSettingsFormatter(ValueSettingsBehaviour.ValueSettings::format));
        }
    }

    private static final class TrackStrengthValueBox
    extends ValueBoxTransform {
        private TrackStrengthValueBox() {
        }

        public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack ms) {
            Direction facing = (Direction)state.getValue(SableTrackBlock.HORIZONTAL_FACING);
            float yRot = AngleHelper.horizontalAngle((Direction)facing) + 180.0f;
            ((PoseTransformStack)TransformStack.of((PoseStack)ms).rotateYDegrees(yRot)).rotateXDegrees(90.0f);
        }

        public boolean testHit(LevelAccessor level, BlockPos pos, BlockState state, Vec3 localHit) {
            if (TracksClient.holdingSuspensionKey && !TracksClient.holdingSuspensionKeyInPositionMode && !TracksClient.holdingSuspensionKeyInAllPositionMode && !TracksClient.holdingSuspensionKeyInResetMode) {
                return true;
            }
            Vec3 offset = this.getLocalOffset(level, pos, state);
            return offset != null && localHit.distanceTo(offset) < (double)(this.scale / 3.0f);
        }

        public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
            Direction facing = (Direction)state.getValue(SableTrackBlock.HORIZONTAL_FACING);
            float stateAngle = AngleHelper.horizontalAngle((Direction)facing) + 180.0f;
            return VecHelper.rotateCentered((Vec3)VecHelper.voxelSpace((double)8.0, (double)15.5, (double)11.0), (double)stateAngle, (Direction.Axis)Direction.Axis.Y);
        }
    }

    private record TerrainCastResult(double maxExtension, @NotNull Direction normal, @Nullable SubLevel subLevel, @Nullable BlockPos minInteractingBlock) {
    }

    private record SuspensionSegment(int index, int count) {
    }
}

