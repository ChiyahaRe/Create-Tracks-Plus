/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.tterrag.registrate.util.entry.BlockEntityEntry
 *  com.tterrag.registrate.util.nullness.NonNullSupplier
 *  dev.simulated_team.simulated.registrate.SimulatedRegistrate
 */
package dev.qwxon.tracks.index;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.qwxon.tracks.Tracks;
import dev.qwxon.tracks.content.blocks.sable_track.SableTrackBlockEntity;
import dev.qwxon.tracks.content.blocks.sable_track.SableTrackRenderer;
import dev.qwxon.tracks.index.TracksBlocks;
import dev.simulated_team.simulated.registrate.SimulatedRegistrate;

public class TracksBlockEntityTypes {
    private static final SimulatedRegistrate REGISTRATE = Tracks.getRegistrate();
    public static final BlockEntityEntry<SableTrackBlockEntity> SABLE_TRACK = REGISTRATE.blockEntity("sable_track", SableTrackBlockEntity::new)
            .validBlocks(new NonNullSupplier[]{TracksBlocks.TRACK_MOUNT})
            // Registrate's renderer(NonNullSupplier<...>) expects the supplied value itself to be a
            // NonNullFunction<Context, BlockEntityRenderer<? super T>>, not a BlockEntityRendererProvider.
            // They're structurally identical (both single-arg functions), but a lambda/method reference is
            // bound to whichever functional interface it's cast to, so casting to the wrong one here compiles
            // fine (thanks to generics erasure) yet always fails with a ClassCastException at registration time.
            .renderer(() -> (NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<? super SableTrackBlockEntity>>) SableTrackRenderer::new)
            .register();

    public static void init() {
    }
}

