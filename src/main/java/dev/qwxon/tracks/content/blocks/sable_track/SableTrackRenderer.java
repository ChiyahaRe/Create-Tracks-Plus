/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  com.simibubi.create.AllPartialModels
 *  com.simibubi.create.content.kinetics.base.KineticBlockEntity
 *  com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer
 *  dev.engine_room.flywheel.lib.model.baked.PartialModel
 *  dev.engine_room.flywheel.lib.transform.PoseTransformStack
 *  dev.engine_room.flywheel.lib.transform.TransformStack
 *  dev.ryanhcode.offroad.index.OffroadPartialModels
 *  net.createmod.catnip.math.AngleHelper
 *  net.createmod.catnip.render.CachedBuffers
 *  net.createmod.catnip.render.SpriteShiftEntry
 *  net.createmod.catnip.render.SuperByteBuffer
 *  net.minecraft.client.renderer.LevelRenderer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider$Context
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.world.level.BlockAndTintGetter
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.Property
 *  org.joml.Vector2d
 */
package dev.qwxon.tracks.content.blocks.sable_track;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.transform.PoseTransformStack;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import dev.qwxon.tracks.TracksClient;
import dev.qwxon.tracks.client.TrackRenderTuning;
import dev.qwxon.tracks.content.blocks.sable_track.SableTrackBlock;
import dev.qwxon.tracks.content.blocks.sable_track.SableTrackBlockEntity;
import dev.qwxon.tracks.content.blocks.sable_track.SableTrackPart;
import dev.qwxon.tracks.content.blocks.sable_track.SableTrackRole;
import dev.qwxon.tracks.index.TracksPartialModels;
import dev.qwxon.tracks.index.TracksSpriteShifts;
import dev.ryanhcode.offroad.index.OffroadPartialModels;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import org.joml.Vector2d;

public class SableTrackRenderer
extends KineticBlockEntityRenderer<SableTrackBlockEntity> {
    public SableTrackRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    protected void renderSafe(SableTrackBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        PartialModel wheel;
        BlockState blockState = be.getBlockState();
        SableTrackPart part = be.effectivePart();
        SableTrackRole role = part.role();
        BlockState shaftState = this.getRenderedBlockState(be);
        RenderType type = this.getRenderType(be, shaftState);
        boolean hiddenMount = (Boolean)blockState.getValue((Property)SableTrackBlock.HIDDEN);
        if (!hiddenMount && role != SableTrackRole.SUSPENSION) {
            SableTrackRenderer.renderRotatingBuffer((KineticBlockEntity)be, (SuperByteBuffer)this.getRotatedModel(be, shaftState), (PoseStack)ms, (VertexConsumer)buffer.getBuffer(type), (int)light);
            SableTrackRenderer.renderRotatingBuffer((KineticBlockEntity)be, (SuperByteBuffer)this.getOppositeRotatedModel(be, shaftState), (PoseStack)ms, (VertexConsumer)buffer.getBuffer(type), (int)light);
        }
        Direction direction = ((Direction)be.getBlockState().getValue((Property)BlockStateProperties.HORIZONTAL_FACING)).getOpposite();
        double lateralOffset = be.getLerpedLateralOffset(partialTicks);
        double longitudinalOffset = be.getLerpedLongitudinalOffset(partialTicks);
        double heightOffset = be.getLerpedHeightOffset(partialTicks);
        double renderExtension = (part.appliesPhysics() && role != SableTrackRole.DRIVE) ? Math.min(be.getLerpedExtension(partialTicks), part.suspensionTravel()) : 0.0;
        double verticalOffset = role == SableTrackRole.SUSPENSION ? -renderExtension : 0;
        Direction facing = (Direction)blockState.getValue((Property)BlockStateProperties.HORIZONTAL_FACING);
        Direction trackSide = facing.getClockWise();
        boolean frontEnd = role == SableTrackRole.DRIVE && !be.hasTrackNeighbor(trackSide) && be.hasTrackNeighbor(trackSide.getOpposite());
        Direction neighborDirection = facing;
        double neighborHeightOffset = 0;
        if (role == SableTrackRole.DRIVE && frontEnd) {
            neighborDirection = facing.getOpposite();
        }
        Direction alongNeighbor = neighborDirection.getClockWise();
        net.minecraft.world.level.block.entity.BlockEntity neighborBEForOffset = be.getLevel() != null ? be.getLevel().getBlockEntity(be.getBlockPos().relative(alongNeighbor)) : null;
        if (neighborBEForOffset instanceof SableTrackBlockEntity neighborTrack) {
            neighborHeightOffset = neighborTrack.getLerpedHeightOffset(partialTicks);
        }

        int partLight = be.getLevel() == null ? light : LevelRenderer.getLightColor((BlockAndTintGetter)be.getLevel(), (BlockPos)be.getBlockPos().relative(facing));
        double slopeDelta = role.appliesPhysics() ? be.getNeighborExtensionDelta(neighborDirection, partialTicks) : 0.0;
        if (role == SableTrackRole.DRIVE) {
            slopeDelta = be.getNeighborExtensionDelta(neighborDirection, partialTicks);
            if (frontEnd) {
                slopeDelta = -slopeDelta + 0.45 - (neighborHeightOffset - heightOffset)*0.9;
            } else {
                slopeDelta = slopeDelta  - (neighborHeightOffset - heightOffset)*0.75;
            }
        } else if (role == SableTrackRole.SUSPENSION) {
            Direction along = neighborDirection.getClockWise();
            net.minecraft.world.level.block.entity.BlockEntity neighborBE = be.getLevel() != null ? be.getLevel().getBlockEntity(be.getBlockPos().relative(along)) : null;
            if (neighborBE instanceof SableTrackBlockEntity neighborTrack) {
                double neighborHeightOffsetSus = neighborTrack.getLerpedHeightOffset(partialTicks);
                if (neighborTrack.effectiveRole() == SableTrackRole.DRIVE) {
                    Direction neighborFacing = neighborTrack.getBlockState().getValue(SableTrackBlock.HORIZONTAL_FACING);
                    Direction neighborTrackSide = neighborFacing.getClockWise();
                    boolean neighborFrontEnd = !neighborTrack.hasTrackNeighbor(neighborTrackSide) && neighborTrack.hasTrackNeighbor(neighborTrackSide.getOpposite());
                    if (neighborFrontEnd) {
                        slopeDelta += 0.5;
                    } else {
                        slopeDelta -= 0.5;
                    }
                } else {
                    slopeDelta += (neighborHeightOffsetSus - heightOffset);
                }
            }
        }
        float neighborSlopeAngle = TrackRenderTuning.BASE_SLOPE_DEGREES + (float)Math.toDegrees(Math.atan2(slopeDelta, 1.0));
        float beltStretch = (float)Math.min(1.65, Math.sqrt(1.0 + slopeDelta * slopeDelta));
        float visualSlopeDelta = (float)slopeDelta;
        if (role == SableTrackRole.DRIVE) {
            visualSlopeDelta = (float)(slopeDelta + (frontEnd ? -0.5 : 0.5));
            beltStretch = (float)Math.min(1.65, Math.sqrt(1.0 + visualSlopeDelta * visualSlopeDelta));
        }
        float angle = (float)Math.toDegrees(be.getLerpedAngle(partialTicks) * SableTrackRenderer.wheelVisualSign(facing));
        float beltScroll = SableTrackRenderer.getBeltScroll(be, facing, role, partialTicks);
        float endMirror = frontEnd ? -1.0f : 1.0f;
        ms.pushPose();
        ((PoseTransformStack)((PoseTransformStack)TransformStack.of((PoseStack)ms).center()).rotateYDegrees(AngleHelper.horizontalAngle((Direction)direction))).uncenter();
        TrackRenderTuning.Profile tuning = TrackRenderTuning.profileFor(part);
        float beltMirror = role == SableTrackRole.DRIVE ? endMirror : 1.0f;
        float driveStraightScroll = role == SableTrackRole.DRIVE && frontEnd ? -beltScroll : beltScroll;
        float driveWrapScroll = role == SableTrackRole.DRIVE && frontEnd ? beltScroll : -beltScroll;
        SpriteShiftEntry beltSprite = TracksSpriteShifts.belt(be.getBeltColor());
        if (role == SableTrackRole.MOUNT) {
            if (!be.isVisualSuspensionHidden()) {
                double wheelMountHorizontalWheelPosition = 1.375;
                SableTrackRenderer.renderOffroadSuspension(blockState, ms, buffer, partLight, 1.375 + (double)tuning.suspensionMount.x, tuning.suspensionMount.y, tuning.suspensionMount.z, -0.5, 1.0f, tuning.suspensionMount);
            }
            ms.popPose();
            return;
        }
        PartialModel partialModel = wheel = role == SableTrackRole.DRIVE ? TracksPartialModels.TRACKWORK_COGS : TracksPartialModels.TRACKWORK_WHEELS;
        double currentHeightOffset = heightOffset;
        double wheelVisualYOffset = role == SableTrackRole.DRIVE ? -0.8 : -1.0;
        if (role == SableTrackRole.SUSPENSION && !be.isVisualSuspensionHidden()) {
            double wheelVisualY = verticalOffset + part.radius() + wheelVisualYOffset + (double)tuning.wheel.y + currentHeightOffset;
            double suspensionBaseY = tuning.suspensionMount.y;
            double suspensionZ = tuning.wheel.z + tuning.suspensionMount.z;
            SableTrackRenderer.renderOffroadSuspension(blockState, ms, buffer, partLight, tuning.wheel.x + tuning.suspensionMount.x, suspensionBaseY, suspensionZ, wheelVisualY - suspensionBaseY, part.visualScale(), tuning.suspensionMount);
        }
        SableTrackRenderer.renderWheelPartial(wheel, blockState, ms, buffer, partLight, longitudinalOffset + (double)tuning.wheel.x, verticalOffset + part.radius() + wheelVisualYOffset + (double)tuning.wheel.y + currentHeightOffset, lateralOffset + (double)(tuning.wheel.z * beltMirror), angle, part.visualScale(), tuning.wheel);
        if (role == SableTrackRole.DRIVE) {
            SableTrackRenderer.renderBeltPartial(TracksPartialModels.TRACKWORK_WRAPPED_LINK, blockState, ms, buffer, partLight, longitudinalOffset + (double)tuning.wrapBelt.x, part.radius() + (double)tuning.wrapBelt.y - 0.8, lateralOffset + (double)(tuning.wrapBelt.z * endMirror), tuning.wrapBelt.slopeDegrees, part.visualScale(), tuning.wrapBelt, 1.0f, frontEnd, driveWrapScroll, beltSprite);
        }
        double topBeltYOffset = role == SableTrackRole.SUSPENSION ? -0.75 : -0.8;
        double topBeltHeightOffset = 0;
        SableTrackRenderer.renderBeltPartial(TracksPartialModels.TRACKWORK_TRACK_LINK, blockState, ms, buffer, partLight, longitudinalOffset + (double)tuning.topBelt.x, part.radius() + (double)tuning.topBelt.y + topBeltYOffset + topBeltHeightOffset, lateralOffset + (double)(tuning.topBelt.z * beltMirror), TrackRenderTuning.BASE_SLOPE_DEGREES + tuning.topBelt.slopeDegrees, part.visualScale(), tuning.topBelt, 1.0f, frontEnd, driveStraightScroll, beltSprite);
        double bottomBeltVisualY = verticalOffset + part.radius() + (double)tuning.bottomBelt.y + wheelVisualYOffset + 0.6 + currentHeightOffset;
        double bottomBeltVisualX = longitudinalOffset + (double)tuning.bottomBelt.x;
        double bottomBeltVisualZ = lateralOffset + (double)(tuning.bottomBelt.z * beltMirror);
        if (role == SableTrackRole.DRIVE && frontEnd) {
            double neighborExtension = be.getNeighborExtensionDelta(neighborDirection, partialTicks);
            bottomBeltVisualY = verticalOffset - neighborExtension + part.radius() + (double)tuning.bottomBelt.y-0.25 + currentHeightOffset;
            bottomBeltVisualZ -= 0.05; // Bottom Z axis Manual Offset
            slopeDelta += 0.1f; // Y axis Manual Offset
            visualSlopeDelta += 0.8f; // X axis Manual Offset
        }
        neighborSlopeAngle = TrackRenderTuning.BASE_SLOPE_DEGREES + (float)Math.toDegrees(Math.atan2(slopeDelta, 1.0));
        beltStretch = (float)Math.min(1.65, Math.sqrt(1.0 + visualSlopeDelta * visualSlopeDelta));
        SableTrackRenderer.renderBeltPartial(TracksPartialModels.TRACKWORK_TRACK_LINK_DOWN, blockState, ms, buffer, partLight, bottomBeltVisualX, bottomBeltVisualY, bottomBeltVisualZ, neighborSlopeAngle + tuning.bottomBelt.slopeDegrees, part.visualScale(), tuning.bottomBelt, beltStretch, frontEnd, driveStraightScroll, beltSprite);
        ms.popPose();
    }

    private static void renderPartial(PartialModel model, BlockState state, PoseStack ms, MultiBufferSource buffer, int light, double x, double y, double z, float wheelAngle, float widthScale, float lengthScale) {
        SuperByteBuffer partial = CachedBuffers.partial((PartialModel)model, (BlockState)state);
        ((SuperByteBuffer)((SuperByteBuffer)((SuperByteBuffer)((SuperByteBuffer)((SuperByteBuffer)partial.center()).translate((float)x, (float)y, (float)z)).rotateXDegrees(wheelAngle)).scale(widthScale, 1.0f, lengthScale)).uncenter()).light(light).renderInto(ms, buffer.getBuffer(RenderType.cutoutMipped()));
    }

    private static void renderWheelPartial(PartialModel model, BlockState state, PoseStack ms, MultiBufferSource buffer, int light, double x, double y, double z, float wheelAngle, float baseScale, TrackRenderTuning.Element tuning) {
        SuperByteBuffer partial = CachedBuffers.partial((PartialModel)model, (BlockState)state);
        ((SuperByteBuffer)((SuperByteBuffer)((SuperByteBuffer)((SuperByteBuffer)((SuperByteBuffer)((SuperByteBuffer)partial.center()).rotateYDegrees(90.0f)).translate((float)x, (float)y, (float)z)).rotateXDegrees(wheelAngle)).scale(baseScale * tuning.scaleX, tuning.scaleY, tuning.scaleZ)).uncenter()).light(light).renderInto(ms, buffer.getBuffer(RenderType.cutoutMipped()));
    }

    private static void renderBeltPartial(PartialModel model, BlockState state, PoseStack ms, MultiBufferSource buffer, int light, double x, double y, double z, float slopeAngle, float baseScale, TrackRenderTuning.Element tuning, float stretchScaleX, boolean mirrored, float scroll, SpriteShiftEntry beltSprite) {
        float normalizedScroll = scroll - (float)Math.floor(scroll);
        float scrollFrameHeight = beltSprite.getOriginal().getV1() - beltSprite.getOriginal().getV0();
        float atlasScroll = normalizedScroll * scrollFrameHeight;
        SuperByteBuffer partial = CachedBuffers.partial((PartialModel)model, (BlockState)state);
        ((SuperByteBuffer)((SuperByteBuffer)((SuperByteBuffer)((SuperByteBuffer)((SuperByteBuffer)((SuperByteBuffer)((SuperByteBuffer)partial.center()).rotateYDegrees(90.0f)).translate((float)x, (float)y, (float)z)).rotateYDegrees(mirrored ? 180.0f : 0.0f)).rotateXDegrees(slopeAngle)).scale(baseScale * tuning.scaleX, tuning.scaleY, tuning.scaleZ * stretchScaleX)).shiftUVScrolling(beltSprite, atlasScroll).uncenter()).light(light).renderInto(ms, buffer.getBuffer(RenderType.cutout()));
    }

    private static void renderOffroadSuspension(BlockState state, PoseStack ms, MultiBufferSource buffer, int light, double wheelX, double mountY, double wheelZ, double wheelVerticalPosition, float baseScale, TrackRenderTuning.Element tuning) {
        VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());
        SuperByteBuffer teleOuter = CachedBuffers.partial((PartialModel)OffroadPartialModels.TELE_OUTER, (BlockState)state);
        SuperByteBuffer teleInner = CachedBuffers.partial((PartialModel)OffroadPartialModels.TELE_INNER, (BlockState)state);
        SuperByteBuffer teleMount = CachedBuffers.partial((PartialModel)OffroadPartialModels.TELE_MOUNT, (BlockState)state);
        SuperByteBuffer springTop = CachedBuffers.partial((PartialModel)OffroadPartialModels.SPRING_UPPER, (BlockState)state);
        SuperByteBuffer springBottom = CachedBuffers.partial((PartialModel)OffroadPartialModels.SPRING_LOWER, (BlockState)state);
        SuperByteBuffer springMiddle = CachedBuffers.partial((PartialModel)OffroadPartialModels.SPRING_MIDDLE, (BlockState)state);
        double wheelPivotOffsetHor = 0.625;
        double springWheelPivotOffsetHor = 0.75;
        double springWheelPivotOffsetVer = -0.125;
        double horizontalWheelPosition = 1.375;
        double teleMountHor = 0.0;
        double teleMountVer = -0.375;
        double springMountHor = 0.4375;
        double springMountVer = 0.4375;
        double teleAngle = Math.atan2(wheelVerticalPosition - -0.375, 0.75);
        double teleDistance = new Vector2d(wheelVerticalPosition - -0.375, 0.75).length();
        double springAngle = Math.atan2(wheelVerticalPosition - -0.125 - 0.4375, 0.1875);
        double springDistance = new Vector2d(wheelVerticalPosition - -0.125 - 0.4375, 0.1875).length();
        ms.pushPose();
        ms.translate(wheelX - 1.375, mountY, wheelZ);
        ms.scale(baseScale * tuning.scaleX, tuning.scaleY, tuning.scaleZ);
        ms.translate(0.5, 0.5, 0.5);
        ms.mulPose(Axis.XP.rotationDegrees(tuning.slopeDegrees));
        ms.translate(-0.5, -0.5, -0.5);
        ms.pushPose();
        ms.translate(0.0, -0.375, 0.0);
        ms.translate(0.5, 0.5, 0.5);
        ms.mulPose(Axis.XP.rotation((float)teleAngle));
        ms.translate(-0.5, -0.5, -0.5);
        teleOuter.light(light).renderInto(ms, vb);
        ms.translate(0.0, 0.0, -(teleDistance - 1.0));
        teleInner.light(light).renderInto(ms, vb);
        ms.popPose();
        ms.pushPose();
        ms.translate(0.0, wheelVerticalPosition, 0.25);
        teleMount.light(light).renderInto(ms, vb);
        ms.popPose();
        ms.pushPose();
        ms.translate(0.5, 0.9375, 0.0625);
        ms.mulPose(Axis.XP.rotation((float)springAngle + 1.5707964f));
        ms.translate(-0.5, -0.9375, -0.0625);
        float springExtension = (float)springDistance;
        float springSpan = springExtension - 0.25f;
        springTop.light(light).renderInto(ms, vb);
        ((SuperByteBuffer)((SuperByteBuffer)((SuperByteBuffer)springMiddle.light(light).translate(0.0f, 0.8125f, 0.0f)).scale(1.0f, springSpan / 0.875f, 1.0f)).translateBack(0.0f, 0.8125f, 0.0f)).renderInto(ms, vb);
        ((SuperByteBuffer)springBottom.light(light).translate(0.0, -((double)springSpan + -0.875), 0.0)).renderInto(ms, vb);
        ms.popPose();
        ms.popPose();
    }

    private static float getBeltScroll(SableTrackBlockEntity be, Direction facing, SableTrackRole role, float partialTicks) {
        if (!role.appliesPhysics()) {
            return 0.0f;
        }
        float wheelAngle = be.getSharedTrackAngle(facing, partialTicks) * -SableTrackRenderer.wheelVisualSign(facing);
        if (Math.abs(wheelAngle) < 1.0E-4f) {
            return 0.0f;
        }
        double scroll = (double)wheelAngle / (Math.PI * 2) * 2.0;
        return (float)(scroll - Math.floor(scroll));
    }

    private static float wheelVisualSign(Direction facing) {
        return facing == Direction.WEST || facing == Direction.SOUTH ? -1.0f : 1.0f;
    }

    public int getViewDistance() {
        return 512;
    }

    protected SuperByteBuffer getRotatedModel(SableTrackBlockEntity te, BlockState state) {
        return CachedBuffers.partialFacing((PartialModel)AllPartialModels.SHAFT_HALF, (BlockState)te.getBlockState(), (Direction)((Direction)te.getBlockState().getValue((Property)BlockStateProperties.HORIZONTAL_FACING)).getOpposite());
    }

    protected SuperByteBuffer getOppositeRotatedModel(SableTrackBlockEntity te, BlockState state) {
        return CachedBuffers.partialFacing((PartialModel)AllPartialModels.SHAFT_HALF, (BlockState)te.getBlockState(), (Direction)((Direction)te.getBlockState().getValue((Property)BlockStateProperties.HORIZONTAL_FACING)));
    }
}

