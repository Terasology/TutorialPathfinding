// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.tutorialpathfinding.systems;

import com.google.common.collect.Lists;
import org.joml.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.logic.players.event.OnPlayerSpawnedEvent;
import org.terasology.network.NetworkComponent;
import org.terasology.physics.CollisionGroup;
import org.terasology.physics.StandardCollisionGroup;
import org.terasology.physics.components.RigidBodyComponent;
import org.terasology.physics.components.shapes.BoxShapeComponent;
import org.terasology.registry.In;
import org.terasology.rendering.logic.SkeletalMeshComponent;
import org.terasology.tutorialpathfinding.events.CharacterSpawnEvent;

@RegisterSystem
public class SpawnSystem extends BaseComponentSystem {

    Logger logger = LoggerFactory.getLogger(SpawnSystem.class);

    @In
    private EntityManager entityManager;

    @In
    private LocalPlayer localPlayer;

    Prefab baseGooey;

    @Override
    public void postBegin() {
        super.postBegin();
        baseGooey = entityManager.getPrefabManager().getPrefab("TutorialPathfinding:baseGooey");
        logger.error("\n sdfsdfs sdfsdfsd \n\n dfsdfsdf{}", baseGooey.getName());
    }

    @ReceiveEvent
    public void onPlayerSpawn(OnPlayerSpawnedEvent event, EntityRef player){

        player.send(new CharacterSpawnEvent(
                baseGooey, new Vector3f(0, 12, 10)
        ));
    }

    private void spawnCharacter(Prefab prefab, Vector3f spawnPosition) {

        EntityRef newCharacter = entityManager.create(prefab, spawnPosition);

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
        logger.error("Called event \n\n\n suhas");
        Prefab prefabToSpawn = event.getCharacterPrefab();
        Vector3f spawnPosition = event.getSpawnPosition();
        spawnCharacter(prefabToSpawn, spawnPosition);

    }
}
