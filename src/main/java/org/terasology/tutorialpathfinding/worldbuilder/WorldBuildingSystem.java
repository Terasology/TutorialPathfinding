// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.tutorialpathfinding.worldbuilder;

import org.joml.Vector3i;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.players.event.OnPlayerSpawnedEvent;
import org.terasology.engine.registry.In;
import org.terasology.engine.rendering.assets.texture.Texture;
import org.terasology.engine.rendering.assets.texture.TextureUtil;
import org.terasology.engine.utilities.Assets;
import org.terasology.engine.world.WorldProvider;
import org.terasology.engine.world.block.Block;
import org.terasology.engine.world.block.BlockManager;
import org.terasology.engine.world.block.BlockRegion;
import org.terasology.engine.world.selection.BlockSelectionComponent;
import org.terasology.nui.Color;


@RegisterSystem
public class WorldBuildingSystem extends BaseComponentSystem {

    private static final Logger logger = LoggerFactory.getLogger(WorldBuildingSystem.class);

    private static final int SURFACE_HEIGHT = 9;

    @In
    private BlockManager blockManager;
    @In
    private WorldProvider worldProvider;
    @In
    private EntityManager entityManager;

    private Block ground;

    private int level = 0;

    private EntityRef tempTaskEntity;

    @ReceiveEvent
    public void onPlayerSpawn(OnPlayerSpawnedEvent event, EntityRef player) {
        ground = blockManager.getBlock("CoreAssets:Plank");

        String[] test = new String[10];
        test[0] = "1151151115";
        test[1] = "1151151115";
        test[2] = "1115115115";
        test[3] = "1115115115";
        test[4] = "1111511515";
        test[5] = "1151151115";
        test[6] = "1151151115";
        test[7] = "1151151115";
        test[8] = "1151151115";
        test[9] = "1151151115";

        setLevel(1);
        createFromText(test, new Vector3i(0, SURFACE_HEIGHT, 0));
        setLevel(6);
        createFromText(test, new Vector3i(0, SURFACE_HEIGHT, 0));

        BlockSelectionComponent blockSelectionComponent = new BlockSelectionComponent();
        blockSelectionComponent.shouldRender = true;
        blockSelectionComponent.currentSelection = new BlockRegion(0, 15, 0).setSize(5, 2, 3);
        blockSelectionComponent.texture = Assets.get(TextureUtil.getTextureUriForColor(new Color(Color.blue).setAlpha(100)),
                Texture.class).get();
        tempTaskEntity = entityManager.create(blockSelectionComponent);
        logger.error("TempTask entity is : {}", tempTaskEntity.toFullDescription());
        tempTaskEntity.destroy();


    }

    public void createFromText(String[] test, Vector3i origin) {

        for (int i = 0; i < test.length; i++) {
            String current = test[i];
            for (int j = 0; j < current.length(); j++) {
                char c = current.charAt(j);
                int height = c - '0';

                for (int k = 0; k < height; k++) {
                    worldProvider.setBlock(new Vector3i(i + origin.x(), k + level + origin.y(), j + origin.z()),
                            ground);
                }


            }
        }
    }

    public void setLevel(int level) {
        this.level = level;
    }


}
