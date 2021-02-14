// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.tutorialpathfinding.systems;


import org.joml.Vector3f;
import org.joml.Vector3i;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.entitySystem.entity.EntityBuilder;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.common.ActivateEvent;
import org.terasology.logic.inventory.InventoryManager;
import org.terasology.logic.location.LocationComponent;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.logic.players.event.OnPlayerSpawnedEvent;
import org.terasology.minion.move.MinionMoveComponent;
import org.terasology.registry.In;
import org.terasology.tutorialpathfinding.components.SpawnEntityComponent;
import org.terasology.tutorialpathfinding.events.CharacterSpawnEvent;
import org.terasology.world.OnChangedBlock;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;

import java.util.ArrayList;

@RegisterSystem
public class SpawnSystem extends BaseComponentSystem {

    Logger logger = LoggerFactory.getLogger(SpawnSystem.class);

    @In
    private EntityManager entityManager;

    @In
    private LocalPlayer localPlayer;

    @In
    private BlockManager blockManager;

    @In
    private InventoryManager inventoryManager;

    private ArrayList<Vector3i> spawnerPositions = new ArrayList<Vector3i>();

    private Vector3f targetPostion;

    Prefab baseGooey;

    private EntityRef targetEntity;

    @Override
    public void postBegin() {
        super.postBegin();
        baseGooey = entityManager.getPrefabManager().getPrefab("TutorialPathfinding:baseGooey");
        logger.error("\n sdfsdfs sdfsdfsd \n\n dfsdfsdf{}", baseGooey.getName());
    }

    @ReceiveEvent
    public void onPlayerSpawn(OnPlayerSpawnedEvent event, EntityRef player) {






    }

    @ReceiveEvent
    public void blockchanged(OnChangedBlock event, EntityRef entityRef) {
        Block newBlock = event.getNewType();
        Block spawner = blockManager.getBlock("TutorialPathfinding:spawner");
        Block target = blockManager.getBlock("TutorialPathfinding:target");


        if (spawner.equals(newBlock)) {
            logger.error("spawner placed");
            spawnerPositions.add(event.getBlockPosition());
        }
        if(target.equals(newBlock)){
            targetPostion = new Vector3f(event.getBlockPosition());

        }



    }

    @ReceiveEvent(components = {SpawnEntityComponent.class})
    public void setTarget(ActivateEvent event, EntityRef entityRef) {
        for (Vector3i spawnerPos : spawnerPositions) {
            Vector3f floatSpawnerPos = new Vector3f(spawnerPos);
            floatSpawnerPos.add(0, 2, 0);

            entityRef.send(new CharacterSpawnEvent(baseGooey, floatSpawnerPos));
        }
    }

    private void spawnCharacter(Prefab prefab, Vector3f spawnPosition) {

        EntityBuilder builder = entityManager.newBuilder(prefab);

        MinionMoveComponent minionMoveComponent = builder.getComponent(MinionMoveComponent.class);
        Vector3f tempVector = new Vector3f();
        minionMoveComponent.target = targetPostion;
        builder.saveComponent(minionMoveComponent);


        builder.saveComponent(minionMoveComponent);
        LocationComponent locationComponent = builder.getComponent(LocationComponent.class);
        locationComponent.setWorldPosition(spawnPosition);
        builder.saveComponent(locationComponent);

        EntityRef newChar = builder.build();


    }


    @ReceiveEvent
    public void characterSpawn(CharacterSpawnEvent event, EntityRef player) {

        Prefab prefabToSpawn = event.getCharacterPrefab();
        Vector3f spawnPosition = event.getSpawnPosition();
        spawnCharacter(prefabToSpawn, spawnPosition);

    }
}
