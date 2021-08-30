// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.tutorialpathfinding.PathHighlighting;

import org.joml.Vector3i;
import org.joml.Vector3ic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.entitySystem.systems.RenderSystem;
import org.terasology.nui.Color;
import org.terasology.engine.registry.In;
import org.terasology.engine.rendering.assets.texture.Texture;
import org.terasology.engine.rendering.assets.texture.TextureUtil;
import org.terasology.engine.rendering.world.selection.BlockSelectionRenderer;
import org.terasology.engine.utilities.Assets;
import org.terasology.engine.world.WorldProvider;
import org.terasology.engine.world.block.Block;
import org.terasology.engine.world.block.BlockManager;

import java.util.ArrayList;

import static org.joml.Math.abs;

@RegisterSystem
public class PathHighlightingSystem extends BaseComponentSystem implements RenderSystem {

    Logger logger = LoggerFactory.getLogger(PathHighlightingSystem.class);

    @In
    private BlockManager blockManager;
    @In
    private WorldProvider worldProvider;

    private boolean pathsChanged;

    BlockSelectionRenderer pathRenderer;

    ArrayList<Vector3i> connectedPath;
    Vector3i blockPos;


    @ReceiveEvent
    public void highlightblocks(HighlightPathEvent event, EntityRef entityRef) {
        highlightBlocks(event.getBlockPositions());
    }
    @ReceiveEvent
    public void highlightblock(HighlightBlockEvent event, EntityRef entityRef) {
        highlightBlock(event.getBlockPosition());
    }

    public void highlightBlocks(ArrayList<Vector3i> blocks) {
        Block pathBlock = blockManager.getBlock("coreassets:Plank");

        connectedPath = connectDisconnectedPaths(blocks);
        pathsChanged = true;

        for (Vector3ic pos : connectedPath) {
            worldProvider.setBlock(pos, pathBlock);
        }

    }

    public void highlightBlock(Vector3i blockPosition){

        this.blockPos = blockPosition;

    }

    public ArrayList<Vector3i> connectDisconnectedPaths(ArrayList<Vector3i> blocks) {
        ArrayList<Vector3i> connectedPath1 = new ArrayList<>();


        logger.error("Connected nodes are ");


        for (int i = 1; i < blocks.size(); i++) {

            Vector3i currentBlock = blocks.get(i);
            Vector3i previousBlock = blocks.get(i - 1);
            logger.error("Connecting {} to {} ", previousBlock.toString(), currentBlock.toString());
            ArrayList<Vector3i> connectedNodes = connectNodes(previousBlock, currentBlock);
            //logger.error(connectedNodes.toString());
            connectedPath1.addAll(connectedNodes);

        }


        return connectedPath1;
    }

    /**
     * Bresenham's algorithm that returns integer points that lie on the line joining two points
     *
     * @param ptA First point
     * @param ptB Second point
     * @return Arraylist <Vector3i></> of the block positions that lie on the line.
     */

    public ArrayList<Vector3i> connectNodes(Vector3i ptA, Vector3i ptB) {

        int height = ptA.y();

        ArrayList<Vector3i> connectedNodes = new ArrayList<>();
        int x0 = ptA.x();
        int y0 = ptA.z();
        int x1 = ptB.x();
        int y1 = ptB.z();
        int dx = abs(x1 - x0);
        int sx = x0 < x1 ? 1 : -1;
        int dy = -abs(y1 - y0);
        int sy = y0 < y1 ? 1 : -1;
        int err = dx + dy;
        while (true) {
            connectedNodes.add(new Vector3i(x0, height, y0));
            if (x0 == x1 && y0 == y1) {
                break;
            }
            int error = 2 * err;
            if (error >= dy) {
                err += dy;
                x0 += sx;
            }
            if (error <= dx) {
                err += dx;
                y0 += sy;
            }


        }

        return connectedNodes;

    }

    @Override
    public void postBegin() {
        super.postBegin();
        pathRenderer =
                new BlockSelectionRenderer(Assets.get(TextureUtil.getTextureUriForColor(new Color(Color.magenta).setAlpha(55)),
                        Texture.class).get());
        pathsChanged = false;

    }

    @Override
    public void renderOpaque() {

    }

    @Override
    public void renderAlphaBlend() {

        pathRenderer.beginRenderOverlay();
        if (pathsChanged) {

            for (Vector3i pos : connectedPath) {
                pathRenderer.renderMark(pos);

            }

        }

        if(blockPos!=null) {
            pathRenderer.renderMark(blockPos);
        }



        pathRenderer.endRenderOverlay();


    }

    @Override
    public void renderOverlay() {

    }

    @Override
    public void renderShadows() {

    }
}
