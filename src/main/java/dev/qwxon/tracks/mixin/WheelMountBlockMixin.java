/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.simibubi.create.content.kinetics.base.HorizontalKineticBlock
 *  dev.ryanhcode.offroad.content.blocks.wheel_mount.WheelMountBlock
 *  dev.ryanhcode.offroad.content.blocks.wheel_mount.WheelMountBlockEntity
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.ItemInteractionResult
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.context.UseOnContext
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.RenderShape
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BooleanProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package dev.qwxon.tracks.mixin;

import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import dev.qwxon.tracks.compat.SableCollisionCompat;
import dev.qwxon.tracks.mixin_interface.WheelMountOffsetAccess;
import dev.ryanhcode.offroad.content.blocks.wheel_mount.WheelMountBlock;
import dev.ryanhcode.offroad.content.blocks.wheel_mount.WheelMountBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={WheelMountBlock.class})
public abstract class WheelMountBlockMixin
extends HorizontalKineticBlock {
    @Unique
    private static final BooleanProperty TRACKS_HIDDEN = BooleanProperty.create((String)"tracks_hidden");
    @Unique
    private static final VoxelShape TRACKS_HIDDEN_SELECTION_SHAPE = Block.box((double)0.0, (double)14.0, (double)0.0, (double)16.0, (double)16.0, (double)16.0);

    protected WheelMountBlockMixin(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Inject(method={"<init>"}, at={@At(value="RETURN")})
    private void tracks$initHiddenProperty(BlockBehaviour.Properties properties, CallbackInfo ci) {
        this.registerDefaultState((BlockState)this.defaultBlockState().setValue((Property)TRACKS_HIDDEN, (Comparable)Boolean.valueOf(false)));
    }

    @Inject(method={"createBlockStateDefinition"}, at={@At(value="TAIL")})
    private void tracks$createHiddenState(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(new Property[]{TRACKS_HIDDEN});
    }

    @Inject(method={"useItemOn"}, at={@At(value="HEAD")}, cancellable=true)
    private void tracks$handleShiftMountToggles(ItemStack heldItem, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<ItemInteractionResult> cir) {
        if (!player.isShiftKeyDown()) {
            return;
        }
        if (hitResult.getDirection() == Direction.UP) {
            if (!level.isClientSide) {
                WheelMountBlockMixin.tracks$toggleHiddenMount(state, level, pos);
            }
            cir.setReturnValue(ItemInteractionResult.CONSUME);
            return;
        }
        if (hitResult.getDirection() == state.getValue(HORIZONTAL_FACING)) {
            BlockEntity blockEntity;
            if (!level.isClientSide && (blockEntity = level.getBlockEntity(pos)) instanceof WheelMountBlockEntity) {
                WheelMountBlockEntity be = (WheelMountBlockEntity)blockEntity;
                ((WheelMountOffsetAccess)be).tracks$toggleVisualSuspensionHidden();
                level.playSound(null, pos, (SoundEvent)SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.PLAYERS, 0.5f, 1.1f);
            }
            cir.setReturnValue(ItemInteractionResult.CONSUME);
        }
    }

    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (context.getClickedFace() == Direction.UP) {
            if (!level.isClientSide) {
                WheelMountBlockMixin.tracks$toggleHiddenMount(state, level, pos);
            }
            return InteractionResult.SUCCESS;
        }
        if (context.getClickedFace() == state.getValue(HORIZONTAL_FACING)) {
            BlockEntity blockEntity;
            if (!level.isClientSide && (blockEntity = level.getBlockEntity(pos)) instanceof WheelMountBlockEntity) {
                WheelMountBlockEntity be = (WheelMountBlockEntity)blockEntity;
                ((WheelMountOffsetAccess)be).tracks$toggleVisualSuspensionHidden();
                level.playSound(null, pos, (SoundEvent)SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.PLAYERS, 0.5f, 1.1f);
            }
            return InteractionResult.SUCCESS;
        }
        return super.onSneakWrenched(state, context);
    }

    protected RenderShape getRenderShape(BlockState state) {
        if (state.hasProperty((Property)TRACKS_HIDDEN) && ((Boolean)state.getValue((Property)TRACKS_HIDDEN)).booleanValue()) {
            return RenderShape.INVISIBLE;
        }
        return super.getRenderShape(state);
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (WheelMountBlockMixin.tracks$isHidden(state)) {
            return TRACKS_HIDDEN_SELECTION_SHAPE;
        }
        return super.getShape(state, level, pos, context);
    }

    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (WheelMountBlockMixin.tracks$isHidden(state) && SableCollisionCompat.isSableColliderBake()) {
            return Shapes.empty();
        }
        if (WheelMountBlockMixin.tracks$isHidden(state) && context != CollisionContext.empty()) {
            return Shapes.empty();
        }
        return super.getCollisionShape(state, level, pos, context);
    }

    protected VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        if (WheelMountBlockMixin.tracks$isHidden(state)) {
            return Shapes.empty();
        }
        return super.getOcclusionShape(state, level, pos);
    }

    @Unique
    private static boolean tracks$isHidden(BlockState state) {
        return state.hasProperty((Property)TRACKS_HIDDEN) && (Boolean)state.getValue((Property)TRACKS_HIDDEN) != false;
    }

    @Unique
    private static void tracks$toggleHiddenMount(BlockState state, Level level, BlockPos pos) {
        level.setBlock(pos, (BlockState)state.cycle((Property)TRACKS_HIDDEN), 2);
        level.playSound(null, pos, (SoundEvent)SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.PLAYERS, 0.5f, (Boolean)state.getValue((Property)TRACKS_HIDDEN) != false ? 0.8f : 1.2f);
    }
}

