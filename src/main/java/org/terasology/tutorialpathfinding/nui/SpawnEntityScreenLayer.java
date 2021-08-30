// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.tutorialpathfinding.nui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.engine.entitySystem.prefab.PrefabManager;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.logic.players.LocalPlayer;
import org.terasology.nui.widgets.UIButton;
import org.terasology.nui.widgets.UILabel;
import org.terasology.engine.registry.In;
import org.terasology.engine.rendering.nui.CoreScreenLayer;
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

        setPrefab(baseGoeey);


        spawnCritter1.subscribe(button -> {
            if(spawnerEntity.exists()==false){
                logger.error("inside subscribe doesnt exist");
            }
            setPrefab(baseGoeey);
        });

    }

    private void setPrefab(Prefab prefabToSpawn) {

        logger.error(localPlayer.toString());
        // spawnerComponent.prefabToSpawn = prefabToSpawn;
        logger.error("set prefab again ");
    }

    //
    private void setSpawnerEntity() {
        logger.error("entered setSpawnerEntity asdlfk");
        for (EntityRef spawner : entityManager.getEntitiesWith(PathfindingSpawnerComponent.class,
                LocationComponent.class)) {
            spawnerEntity = spawner;

            if(spawnerEntity.exists()==false){
                logger.error("doesnt exist in setspawnerentity ");
            }
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
