// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.tutorialpathfinding.systems;


import org.joml.Vector3f;
import org.joml.Vector3i;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.engine.entitySystem.entity.EntityBuilder;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.common.ActivateEvent;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.logic.players.LocalPlayer;
import org.terasology.engine.logic.players.event.OnPlayerSpawnedEvent;
import org.terasology.minion.move.MinionMoveComponent;
import org.terasology.engine.registry.In;
import org.terasology.module.inventory.systems.InventoryManager;
import org.terasology.tutorialpathfinding.components.SpawnEntityComponent;
import org.terasology.tutorialpathfinding.events.CharacterSpawnEvent;
import org.terasology.engine.world.OnChangedBlock;
import org.terasology.engine.world.block.Block;
import org.terasology.engine.world.block.BlockManager;

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

    private Vector3f targetPosition = new Vector3f();

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
            spawnerPositions.add(new Vector3i(event.getBlockPosition()));
        }
        if (target.equals(newBlock)) {
            targetPosition.set(event.getBlockPosition());

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
        minionMoveComponent.target = targetPosition;
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
