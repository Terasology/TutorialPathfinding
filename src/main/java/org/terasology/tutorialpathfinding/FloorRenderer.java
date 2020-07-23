// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.tutorialpathfinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.entitySystem.systems.RenderSystem;
import org.terasology.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.math.geom.Vector3i;
import org.terasology.navgraph.Floor;
import org.terasology.navgraph.NavGraphChunk;
import org.terasology.navgraph.WalkableBlock;
import org.terasology.pathfinding.componentSystem.PathfinderSystem;
import org.terasology.registry.In;
import org.terasology.rendering.assets.texture.Texture;
import org.terasology.rendering.assets.texture.TextureUtil;
import org.terasology.rendering.nui.Color;
import org.terasology.rendering.world.selection.BlockSelectionRenderer;
import org.terasology.utilities.Assets;
import org.terasology.world.WorldProvider;

@RegisterSystem
public class FloorRenderer implements RenderSystem, UpdateSubscriberSystem {

    Logger logger = LoggerFactory.getLogger(FloorRenderer.class);
    boolean[][][] counted = new boolean[16][32][16];

    private BlockSelectionRenderer blueRenderer;
    private BlockSelectionRenderer redRenderer;
    private BlockSelectionRenderer greenRenderer;

    @In
    WorldProvider worldProvider;
    @In
    PathfinderSystem pathfinderSystem;

    @Override
    public void renderOpaque() {

    }

    @Override
    public void renderAlphaBlend() {

        WalkableBlock walkableBlock = pathfinderSystem.getBlock(new Vector3i(0, 10, 0));
        if (walkableBlock != null) {
            Floor floor = walkableBlock.floor;
            NavGraphChunk
                    navGraphChunk = floor.navGraphChunk;
            for (WalkableBlock walkableBlock1 : navGraphChunk.walkableBlocks) {
                Floor currentFloor = walkableBlock1.floor;
                int currentId = currentFloor.id;
                int checkColor = currentId % 3;

                if (checkColor == 0) {
                    redRenderer.beginRenderOverlay();
                    redRenderer.renderMark(walkableBlock1.getBlockPosition());
                    redRenderer.endRenderOverlay();

                } else if (checkColor == 1) {
                    blueRenderer.beginRenderOverlay();
                    blueRenderer.renderMark(walkableBlock1.getBlockPosition());
                    blueRenderer.endRenderOverlay();
                } else {
                    greenRenderer.beginRenderOverlay();
                    greenRenderer.renderMark(walkableBlock1.getBlockPosition());
                    greenRenderer.endRenderOverlay();
                }
            }
        }


    }

    @Override
    public void renderOverlay() {

    }

    @Override
    public void renderShadows() {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void initialise() {

    }

    @Override
    public void preBegin() {

    }

    @Override
    public void postBegin() {

        blueRenderer =
                new BlockSelectionRenderer(Assets.get(TextureUtil.getTextureUriForColor(Color.BLUE.alterAlpha(35)),
                        Texture.class).get());
        greenRenderer =
                new BlockSelectionRenderer(Assets.get(TextureUtil.getTextureUriForColor(Color.GREEN.alterAlpha(35)),
                        Texture.class).get());
        redRenderer =
                new BlockSelectionRenderer(Assets.get(TextureUtil.getTextureUriForColor(Color.RED.alterAlpha(35)),
                        Texture.class).get());

    }

    @Override
    public void preSave() {

    }

    @Override
    public void postSave() {

    }

    @Override
    public void shutdown() {

    }
}
