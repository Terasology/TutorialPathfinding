// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.tutorialpathfinding.nui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.entitySystem.prefab.PrefabManager;
import org.terasology.logic.location.LocationComponent;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.registry.In;
import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.rendering.nui.widgets.UIButton;
import org.terasology.rendering.nui.widgets.UILabel;
import org.terasology.tutorialpathfinding.components.PathfindingSpawnerComponent;

public class SpawnEntityScreenLayer extends CoreScreenLayer {
    private static final Logger logger = LoggerFactory.getLogger(SpawnEntityScreenLayer.class);

    @In
    private EntityManager entityManager;

    @In
    private LocalPlayer localPlayer;

    @In
    private PrefabManager prefabManager;

    private UIButton spawnEntity;

    Prefab baseGoeey;

    private EntityRef spawnerEntity;

    @Override
    public void initialise() {
        logger.error("Screen spawned ");
        setSpawnerEntity();
        UIButton spawnCritter1 = find("spawnCritter1", UIButton.class);
        UIButton spawnCritter2 = find("spawnCritter2", UIButton.class);
        UIButton spawnCritter3 = find("spawnCritter3", UIButton.class);

        UILabel spawnCritter1label = find("labelCritter1", UILabel.class);
        UILabel spawnCritter2label = find("labelCritter2", UILabel.class);
        UILabel spawnCritter3label = find("labelCritter3", UILabel.class);

        populateUiLabels(spawnCritter1label);
        populateUiLabels(spawnCritter2label);
        populateUiLabels(spawnCritter3label);

        baseGoeey = entityManager.getPrefabManager().getPrefab("TutorialPathfinding:baseGooey");


        spawnCritter1.subscribe(button -> {
            setPrefab(baseGoeey);
        });

    }

    private void setPrefab(Prefab prefabToSpawn) {
        LocationComponent spawnerComponent = spawnerEntity.getComponent(LocationComponent.class);
        if (spawnerEntity == null) {
            logger.error("Spawner entity is null for some reason");
        } else {
            logger.error(spawnerEntity.toFullDescription());

        }

        // spawnerComponent.prefabToSpawn = prefabToSpawn;
        logger.error("set prefab again ");
    }

    //
    private void setSpawnerEntity() {
        logger.error("entered setSpawnerEntity asdlfk");
        for (EntityRef spawner : entityManager.getEntitiesWith(PathfindingSpawnerComponent.class,
                LocationComponent.class)) {
            spawnerEntity = spawner;
            logger.error(spawnerEntity.toFullDescription());
            break;
        }
    }


    private void populateUiLabels(UILabel label) {

        StringBuilder text = new StringBuilder("Spawns entity");

        //TODO make labels more descriptive


        label.setText(text.toString());
    }

}
