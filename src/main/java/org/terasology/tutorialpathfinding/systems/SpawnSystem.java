// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.tutorialpathfinding.systems;

import com.google.common.collect.Lists;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.terasology.math.JomlUtil;
import org.terasology.minion.move.MinionMoveComponent;
import org.terasology.minion.move.MinionMoveSystem;
import org.terasology.network.NetworkComponent;
import org.terasology.physics.CollisionGroup;
import org.terasology.physics.StandardCollisionGroup;
import org.terasology.physics.components.RigidBodyComponent;
import org.terasology.physics.components.shapes.BoxShapeComponent;
import org.terasology.registry.In;
import org.terasology.tutorialpathfinding.components.SpawnEntityComponent;
import org.terasology.tutorialpathfinding.components.PathfindingSpawnerComponent;
import org.terasology.tutorialpathfinding.events.CharacterSpawnEvent;
import org.terasology.world.OnChangedBlock;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.block.items.BlockItemFactory;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void postBegin() {
        super.postBegin();
        baseGooey = entityManager.getPrefabManager().getPrefab("TutorialPathfinding:baseGooey");
        logger.error("\n sdfsdfs sdfsdfsd \n\n dfsdfsdf{}", baseGooey.getName());
    }

    @ReceiveEvent
    public void onPlayerSpawn(OnPlayerSpawnedEvent event, EntityRef player) {




        BlockItemFactory blockFactory = new BlockItemFactory(entityManager);
        EntityRef planks = blockFactory.newInstance(blockManager.getBlockFamily("coreassets:Plank"), 99);
        inventoryManager.giveItem(player, player, planks);
        inventoryManager.giveItem(player, player, entityManager.create("TutorialPathfinding:spawnEntitiesItem"));

        inventoryManager.giveItem(player, player, blockFactory.newInstance(blockManager.getBlockFamily(
                "TutorialPathfinding:spawner"), 2));

        inventoryManager.giveItem(player, player, blockFactory.newInstance(blockManager.getBlockFamily(
                "TutorialPathfinding:target"), 2));

    }

    @ReceiveEvent
    public void blockchanged(OnChangedBlock event, EntityRef entityRef) {
        Block newBlock = event.getNewType();
        Block spawner = blockManager.getBlock("TutorialPathfinding:spawner");
        Block target = blockManager.getBlock("TutorialPathfinding:target");
        logger.error((spawner.toString()));

        if (spawner.equals(newBlock)) {
            logger.error("spawner placed");
            spawnerPositions.add(JomlUtil.from(event.getBlockPosition()));
        }
        if(target.equals(newBlock)){
            targetPostion = new Vector3f(JomlUtil.from(event.getBlockPosition()));
        }



    }

    @ReceiveEvent(components = {SpawnEntityComponent.class})
    public void setTarget(ActivateEvent event, EntityRef entityRef) {

        logger.error("item activated ");
        for (Vector3i spawnerPos : spawnerPositions) {

            spawnerPos.add(0, 2, 0);

            Vector3f floatSpawnerPos = new Vector3f(spawnerPos);

            entityRef.send(new CharacterSpawnEvent(baseGooey, floatSpawnerPos));


//            LocationComponent locationComponent = spawner.getComponent(LocationComponent.class);
//            Vector3f spawnerPos = JomlUtil.from(locationComponent.getWorldPosition()) ;
//
//            PathfindingSpawnerComponent spawnerComponent = spawner.getComponent(PathfindingSpawnerComponent.class);
//
//            logger.error("found at \n {} x  {} y  {}  z\n ", spawnerPos.x , spawnerPos.y , spawnerPos.z);
//
//

        }




    }

    private void spawnCharacter(Prefab prefab, Vector3f spawnPosition) {

        EntityRef newCharacter = entityManager.create(prefab, spawnPosition);

        MinionMoveComponent minionMoveComponent = new MinionMoveComponent();
        minionMoveComponent.target = JomlUtil.from(targetPostion);

        newCharacter.addOrSaveComponent(minionMoveComponent);




        NetworkComponent networkComponent = new NetworkComponent();
        networkComponent.replicateMode = NetworkComponent.ReplicateMode.ALWAYS;
        newCharacter.addComponent(networkComponent);
        BoxShapeComponent boxShape = new BoxShapeComponent();
        boxShape.extents = new org.terasology.math.geom.Vector3f(1, 1, 1);

        RigidBodyComponent rigidBody = newCharacter.getComponent(RigidBodyComponent.class);
        rigidBody.collidesWith = Lists.<CollisionGroup>newArrayList(StandardCollisionGroup.DEFAULT,
                StandardCollisionGroup.CHARACTER, StandardCollisionGroup.WORLD);

        newCharacter.addOrSaveComponent(boxShape);
        newCharacter.saveComponent(rigidBody);


    }


    @ReceiveEvent
    public void characterSpawn(CharacterSpawnEvent event, EntityRef player) {

        Prefab prefabToSpawn = event.getCharacterPrefab();
        Vector3f spawnPosition = event.getSpawnPosition();
        spawnCharacter(prefabToSpawn, spawnPosition);

    }
}
