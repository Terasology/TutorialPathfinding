// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.tutorialpathfinding;

import org.joml.Vector3f;
import org.joml.Vector3i;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.engine.entitySystem.entity.EntityBuilder;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.entitySystem.systems.RenderSystem;
import org.terasology.engine.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.logic.nameTags.NameTagComponent;
import org.terasology.engine.registry.In;
import org.terasology.engine.rendering.assets.texture.Texture;
import org.terasology.engine.rendering.assets.texture.TextureUtil;
import org.terasology.engine.rendering.world.selection.BlockSelectionRenderer;
import org.terasology.engine.utilities.Assets;
import org.terasology.navgraph.Floor;
import org.terasology.navgraph.NavGraphChunk;
import org.terasology.navgraph.WalkableBlock;
import org.terasology.nui.Color;
import org.terasology.pathfinding.componentSystem.PathfinderSystem;

import java.util.ArrayList;

@RegisterSystem
public class FloorRenderer implements RenderSystem, UpdateSubscriberSystem {

    private static final Logger logger = LoggerFactory.getLogger(FloorRenderer.class);

    @In
    PathfinderSystem pathfinderSystem;
    @In
    EntityManager entityManager;

    private ArrayList<Integer> namedFloors;

    private BlockSelectionRenderer blueRenderer;
    private BlockSelectionRenderer redRenderer;
    private BlockSelectionRenderer greenRenderer;
    private BlockSelectionRenderer entranceRenderer;

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
                    Vector3f blockPos = new Vector3f(walkableBlock1.getBlockPosition());
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
                new BlockSelectionRenderer(Assets.get(TextureUtil.getTextureUriForColor(new Color(Color.blue).setAlpha(35)),
                        Texture.class).get());
        greenRenderer =
                new BlockSelectionRenderer(Assets.get(TextureUtil.getTextureUriForColor(new Color(Color.green).setAlpha(35)),
                        Texture.class).get());
        redRenderer =
                new BlockSelectionRenderer(Assets.get(TextureUtil.getTextureUriForColor(new Color(Color.red).setAlpha(35)),
                        Texture.class).get());
        entranceRenderer =
                new BlockSelectionRenderer(Assets.get(TextureUtil.getTextureUriForColor(new Color(Color.black).setAlpha(150)),
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
