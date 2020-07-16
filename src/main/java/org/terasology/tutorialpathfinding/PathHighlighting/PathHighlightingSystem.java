// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.tutorialpathfinding.PathHighlighting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.math.JomlUtil;
import org.terasology.math.geom.Vector3i;
import org.terasology.pathfinding.model.Path;
import org.terasology.registry.In;
import org.terasology.tutorialpathfinding.events.HighlightPathEvent;
import org.terasology.tutorialpathfinding.systems.SpawnSystem;
import org.terasology.world.WorldProvider;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;

import java.util.ArrayList;

import static org.joml.Math.abs;

@RegisterSystem
public class PathHighlightingSystem extends BaseComponentSystem {

    Logger logger = LoggerFactory.getLogger(PathHighlightingSystem.class);
    @In
    private BlockManager blockManager;
    @In
    private WorldProvider worldProvider;

    @ReceiveEvent
    public void highlightblock(HighlightPathEvent event, EntityRef entityRef) {
        highlightBlocks(event.getBlockPositions());
    }

    public void highlightBlocks(ArrayList<Vector3i> blocks) {
        Block pathBlock = blockManager.getBlock("coreassets:Plank");

        ArrayList<Vector3i> connectedPath = connectDisconnectedPaths(blocks);

        for (Vector3i blockPos : connectedPath) {
            worldProvider.setBlock(JomlUtil.from(blockPos), pathBlock);
        }

    }

    public ArrayList<Vector3i> connectDisconnectedPaths(ArrayList<Vector3i> blocks) {
        ArrayList<Vector3i> connectedPath = new ArrayList<>();



        logger.error("Connected nodes are ");


        for (int i = 1; i < blocks.size(); i++) {

            Vector3i currentBlock = blocks.get(i);
            Vector3i previousBlock = blocks.get(i - 1);
            logger.error("Connecting {} to {} ", previousBlock.toString(), currentBlock.toString());
            ArrayList<Vector3i> connectedNodes = connectNodes(previousBlock, currentBlock);
            //logger.error(connectedNodes.toString());
            connectedPath.addAll(connectedNodes);

        }


        return connectedPath;
    }

    /**
     * Bresenham's algorithm that returns integer points that lie on the line joining two points
     *
     * @param ptA First point
     * @param ptB Second point
     * @return Arraylist <Vector3i></> of the block positions that lie on the line.
     */

    public ArrayList<Vector3i> connectNodes(Vector3i ptA, Vector3i ptB) {

        int height = ptA.getY();

        ArrayList<Vector3i> connectedNodes = new ArrayList<>();
        int x0 = ptA.getX();
        int y0 = ptA.getZ();
        int x1 = ptB.getX();
        int y1 = ptB.getZ();
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


}
