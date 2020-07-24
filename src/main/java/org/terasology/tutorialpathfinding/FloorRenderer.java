// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.tutorialpathfinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.entitySystem.entity.EntityBuilder;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.entitySystem.systems.RenderSystem;
import org.terasology.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.logic.location.LocationComponent;
import org.terasology.logic.nameTags.NameTagComponent;
import org.terasology.math.JomlUtil;
import org.terasology.math.geom.Vector3f;
import org.terasology.math.geom.Vector3i;
import org.terasology.minion.move.MinionMoveComponent;
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

import java.util.ArrayList;
import java.util.List;

@RegisterSystem
public class FloorRenderer implements RenderSystem, UpdateSubscriberSystem {

    Logger logger = LoggerFactory.getLogger(FloorRenderer.class);

    private ArrayList<Integer> namedFloors;


    private BlockSelectionRenderer blueRenderer;
    private BlockSelectionRenderer redRenderer;
    private BlockSelectionRenderer greenRenderer;
    private BlockSelectionRenderer entranceRenderer;

    @In
    WorldProvider worldProvider;
    @In
    PathfinderSystem pathfinderSystem;
    @In
    EntityManager entityManager;

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

                if (!namedFloors.contains(currentId)) {
                    namedFloors.add(currentId);
                    NameTagComponent nameTagComponent = new NameTagComponent();
                    nameTagComponent.text = Integer.toString(currentId);
                    nameTagComponent.yOffset = 3;
                    nameTagComponent.scale = 7;

                    EntityBuilder builder = entityManager.newBuilder();


                    builder.addComponent(nameTagComponent);
                    LocationComponent locationComponent = new LocationComponent();
                    Vector3f blockPos = walkableBlock1.getBlockPosition().toVector3f();
                    locationComponent.setWorldPosition(blockPos);
                    builder.addComponent(locationComponent);

                    EntityRef newFloor = builder.build();


                }
                int checkColor = currentId % 3;

                if (currentFloor.isEntrance(walkableBlock1)) {
                    entranceRenderer.beginRenderOverlay();
                    entranceRenderer.renderMark(walkableBlock1.getBlockPosition());
                    entranceRenderer.endRenderOverlay();
                } else {
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
        entranceRenderer =
                new BlockSelectionRenderer(Assets.get(TextureUtil.getTextureUriForColor(Color.BLACK.alterAlpha(150)),
                        Texture.class).get());


        namedFloors = new ArrayList<Integer>();

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
