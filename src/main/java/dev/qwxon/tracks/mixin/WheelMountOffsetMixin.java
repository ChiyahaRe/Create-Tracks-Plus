/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  com.simibubi.create.content.kinetics.base.KineticBlockEntity
 *  dev.ryanhcode.offroad.content.blocks.wheel_mount.WheelMountBlock
 *  dev.ryanhcode.offroad.content.blocks.wheel_mount.WheelMountBlockEntity
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.core.Vec3i
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.util.Mth
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Constant
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyConstant
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package dev.qwxon.tracks.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import dev.qwxon.tracks.mixin_interface.WheelMountOffsetAccess;
import dev.ryanhcode.offroad.content.blocks.wheel_mount.WheelMountBlock;
import dev.ryanhcode.offroad.content.blocks.wheel_mount.WheelMountBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={WheelMountBlockEntity.class})
public abstract class WheelMountOffsetMixin
extends KineticBlockEntity
implements WheelMountOffsetAccess {
    @Unique
    private static final double Tracks_MAX_LATERAL_OFFSET = 1.5;
    @Unique
    private static final double Tracks_MIN_LATERAL_OFFSET = -1.0;
    @Unique
    private static final double Tracks_LATERAL_OFFSET_STEP = 0.125;
    @Unique
    private static final double Tracks_MAX_LONGITUDINAL_OFFSET = 1.5;
    @Unique
    private static final double Tracks_MIN_LONGITUDINAL_OFFSET = -1.0;
    @Unique
    private static final double Tracks_LONGITUDINAL_OFFSET_STEP = 0.125;
    @Unique
    private static final double Tracks_MAX_HEIGHT_OFFSET = 0.75;
    @Unique
    private static final double Tracks_HEIGHT_OFFSET_STEP = 0.125;
    @Shadow
    protected int clientSteeringSignalLeft;
    @Shadow
    protected int clientSteeringSignalRight;
    @Unique
    private double tracks$lateralOffset = 0.0;
    @Unique
    private double tracks$lastLateralOffset = 0.0;
    @Unique
    private double tracks$longitudinalOffset = 0.0;
    @Unique
    private double tracks$lastLongitudinalOffset = 0.0;
    @Unique
    private double tracks$heightOffset = 0.0;
    @Unique
    private double tracks$lastHeightOffset = 0.0;
    @Unique
    private double tracks$wheelSpringMultiplier = 1.0;
    @Unique
    private double tracks$wheelDampingMultiplier = 1.0;
    @Unique
    private double tracks$wheelDriveMultiplier = 1.0;
    @Unique
    private double tracks$wheelGripMultiplier = 1.0;
    @Unique
    private boolean tracks$visualSuspensionHidden = false;

    @Shadow
    protected abstract double getLerpedYaw(double var1);

    public WheelMountOffsetMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public double tracks$adjustLateralOffset(int direction) {
        double previous = this.tracks$lateralOffset;
        this.tracks$lateralOffset = Mth.clamp((double)((double)Math.round((this.tracks$lateralOffset + (double)direction * 0.125) / 0.125) * 0.125), (double)-1.0, (double)1.5);
        if (Math.abs(previous - this.tracks$lateralOffset) > 1.0E-6) {
            this.setChanged();
            if (this.level != null && !this.level.isClientSide) {
                this.sendData();
            }
        }
        return this.tracks$lateralOffset;
    }

    @Override
    public double tracks$adjustLongitudinalOffset(int direction) {
        double previous = this.tracks$longitudinalOffset;
        this.tracks$longitudinalOffset = Mth.clamp((double)((double)Math.round((this.tracks$longitudinalOffset + (double)direction * 0.125) / 0.125) * 0.125), (double)-1.0, (double)1.5);
        if (Math.abs(previous - this.tracks$longitudinalOffset) > 1.0E-6) {
            this.setChanged();
            if (this.level != null && !this.level.isClientSide) {
                this.sendData();
            }
        }
        return this.tracks$longitudinalOffset;
    }

    @Override
    public double tracks$adjustHeightOffset(int direction, boolean sideInteraction) {
        double previous = this.tracks$heightOffset;
        this.tracks$heightOffset = Mth.clamp((double)((double)Math.round((this.tracks$heightOffset + (double)direction * 0.125) / 0.125) * 0.125), (double)-0.75, (double)0.75);
        if (Math.abs(previous - this.tracks$heightOffset) > 1.0E-6) {
            this.setChanged();
            if (this.level != null && !this.level.isClientSide) {
                this.sendData();
            }
        }
        return this.tracks$heightOffset;
    }

    @Override
    public double tracks$adjustTuning(String key, int direction) {
        double step = key.equals("drive") ? 0.1 : 0.05;
        double previous = this.tracks$getTuning(key);
        double next = Mth.clamp((double)((double)Math.round((previous + (double)direction * step) / step) * step), (double)0.1, (double)4.0);
        switch (key) {
            case "spring": {
                this.tracks$wheelSpringMultiplier = next;
                break;
            }
            case "damping": {
                this.tracks$wheelDampingMultiplier = next;
                break;
            }
            case "drive": {
                this.tracks$wheelDriveMultiplier = next;
                break;
            }
            case "grip": {
                this.tracks$wheelGripMultiplier = next;
                break;
            }
            default: {
                return previous;
            }
        }
        this.setChanged();
        if (this.level != null && !this.level.isClientSide) {
            this.sendData();
        }
        return next;
    }

    @Override
    public double tracks$getTuning(String key) {
        return switch (key) {
            case "spring" -> this.tracks$wheelSpringMultiplier;
            case "damping" -> this.tracks$wheelDampingMultiplier;
            case "drive" -> this.tracks$wheelDriveMultiplier;
            case "grip" -> this.tracks$wheelGripMultiplier;
            default -> 1.0;
        };
    }

    @Override
    public void tracks$resetTuning() {
        this.tracks$wheelSpringMultiplier = 1.0;
        this.tracks$wheelDampingMultiplier = 1.0;
        this.tracks$wheelDriveMultiplier = 1.0;
        this.tracks$wheelGripMultiplier = 1.0;
        this.setChanged();
        if (this.level != null && !this.level.isClientSide) {
            this.sendData();
        }
    }

    @Override
    public double tracks$getLerpedLateralOffset(float partialTicks) {
        return Mth.lerp((double)partialTicks, (double)this.tracks$lastLateralOffset, (double)this.tracks$lateralOffset);
    }

    @Override
    public double tracks$getLerpedLongitudinalOffset(float partialTicks) {
        return Mth.lerp((double)partialTicks, (double)this.tracks$lastLongitudinalOffset, (double)this.tracks$longitudinalOffset);
    }

    @Override
    public double tracks$getLerpedHeightOffset(float partialTicks) {
        return Mth.lerp((double)partialTicks, (double)this.tracks$lastHeightOffset, (double)this.tracks$heightOffset);
    }

    @Override
    public double tracks$getLerpedYaw(float partialTicks) {
        return this.getLerpedYaw(partialTicks);
    }

    @Override
    public int tracks$getClientSteeringSignalLeft() {
        return this.clientSteeringSignalLeft;
    }

    @Override
    public boolean tracks$isVisualSuspensionHidden() {
        return this.tracks$visualSuspensionHidden;
    }

    @Override
    public void tracks$toggleVisualSuspensionHidden() {
        this.tracks$visualSuspensionHidden = !this.tracks$visualSuspensionHidden;
        this.setChanged();
        if (this.level != null && !this.level.isClientSide) {
            this.sendData();
        }
    }

    @Override
    public int tracks$getClientSteeringSignalRight() {
        return this.clientSteeringSignalRight;
    }

    @Inject(method={"tick"}, at={@At(value="HEAD")})
    private void tracks$tickOffset(CallbackInfo ci) {
        this.tracks$lastLateralOffset = this.tracks$lateralOffset;
        this.tracks$lastLongitudinalOffset = this.tracks$longitudinalOffset;
        this.tracks$lastHeightOffset = this.tracks$heightOffset;
    }

    @Inject(method={"write"}, at={@At(value="TAIL")})
    private void tracks$writeOffset(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket, CallbackInfo ci) {
        tag.putDouble("TracksLateralOffset", this.tracks$lateralOffset);
        tag.putDouble("TracksLongitudinalOffset", this.tracks$longitudinalOffset);
        tag.putDouble("TracksHeightOffset", this.tracks$heightOffset);
        tag.putDouble("TracksWheelSpringMultiplier", this.tracks$wheelSpringMultiplier);
        tag.putDouble("TracksWheelDampingMultiplier", this.tracks$wheelDampingMultiplier);
        tag.putDouble("TracksWheelDriveMultiplier", this.tracks$wheelDriveMultiplier);
        tag.putDouble("TracksWheelGripMultiplier", this.tracks$wheelGripMultiplier);
        tag.putBoolean("TracksVisualSuspensionHidden", this.tracks$visualSuspensionHidden);
    }

    @Inject(method={"read"}, at={@At(value="TAIL")})
    private void tracks$readOffset(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket, CallbackInfo ci) {
        if (tag.contains("TracksLateralOffset")) {
            this.tracks$lateralOffset = tag.getDouble("TracksLateralOffset");
        }
        if (tag.contains("TracksLongitudinalOffset")) {
            this.tracks$longitudinalOffset = tag.getDouble("TracksLongitudinalOffset");
        }
        if (tag.contains("TracksHeightOffset")) {
            this.tracks$heightOffset = tag.getDouble("TracksHeightOffset");
        }
        if (tag.contains("TracksWheelSpringMultiplier")) {
            this.tracks$wheelSpringMultiplier = tag.getDouble("TracksWheelSpringMultiplier");
        }
        if (tag.contains("TracksWheelDampingMultiplier")) {
            this.tracks$wheelDampingMultiplier = tag.getDouble("TracksWheelDampingMultiplier");
        }
        if (tag.contains("TracksWheelDriveMultiplier")) {
            this.tracks$wheelDriveMultiplier = tag.getDouble("TracksWheelDriveMultiplier");
        }
        if (tag.contains("TracksWheelGripMultiplier")) {
            this.tracks$wheelGripMultiplier = tag.getDouble("TracksWheelGripMultiplier");
        }
        if (tag.contains("TracksVisualSuspensionHidden")) {
            this.tracks$visualSuspensionHidden = tag.getBoolean("TracksVisualSuspensionHidden");
        }
        if (clientPacket) {
            this.tracks$lastLateralOffset = this.tracks$lateralOffset;
            this.tracks$lastLongitudinalOffset = this.tracks$longitudinalOffset;
            this.tracks$lastHeightOffset = this.tracks$heightOffset;
        }
    }

    @ModifyConstant(method={"sable$physicsTick"}, constant={@Constant(doubleValue=40.0)})
    private double tracks$tuneWheelSpring(double original) {
        return original * this.tracks$wheelSpringMultiplier;
    }

    @ModifyConstant(method={"sable$physicsTick"}, constant={@Constant(doubleValue=10.0)})
    private double tracks$tuneWheelDamping(double original) {
        return original * this.tracks$wheelDampingMultiplier;
    }

    @ModifyConstant(method={"sable$physicsTick"}, constant={@Constant(doubleValue=1.75)})
    private double tracks$tuneWheelDrive(double original) {
        return original * this.tracks$wheelDriveMultiplier;
    }

    @ModifyConstant(method={"sable$physicsTick"}, constant={@Constant(doubleValue=-0.6)})
    private double tracks$tuneWheelGrip(double original) {
        return original * this.tracks$wheelGripMultiplier;
    }

    @ModifyExpressionValue(method={"sable$physicsTick", "computeMaxExtensionToTerrain"}, at={@At(value="INVOKE", target="Lnet/minecraft/core/BlockPos;getCenter()Lnet/minecraft/world/phys/Vec3;")})
    private Vec3 tracks$offsetWheelCenter(Vec3 original) {
        return this.tracks$applyWheelOffset(original);
    }

    @Inject(method={"createRenderBoundingBox"}, at={@At(value="RETURN")}, cancellable=true)
    private void tracks$inflateRenderBounds(CallbackInfoReturnable<AABB> cir) {
        cir.setReturnValue(((AABB)cir.getReturnValue()).inflate(Math.abs(this.tracks$lateralOffset) + Math.abs(this.tracks$longitudinalOffset)));
    }

    @Unique
    private Vec3 tracks$applyWheelOffset(Vec3 original) {
        Direction facing = (Direction)this.getBlockState().getValue(WheelMountBlock.HORIZONTAL_FACING);
        return original.add(Vec3.atLowerCornerOf((Vec3i)facing.getClockWise().getNormal()).scale(this.tracks$lateralOffset)).add(Vec3.atLowerCornerOf((Vec3i)facing.getNormal()).scale(this.tracks$longitudinalOffset)).add(0.0, this.tracks$heightOffset, 0.0);
    }
}

