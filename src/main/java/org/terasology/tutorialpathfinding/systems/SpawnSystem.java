// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.tutorialpathfinding.systems;

import com.google.common.collect.Lists;
import org.joml.Vector3f;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.network.NetworkComponent;
import org.terasology.physics.CollisionGroup;
import org.terasology.physics.StandardCollisionGroup;
import org.terasology.physics.components.RigidBodyComponent;
import org.terasology.physics.components.shapes.BoxShapeComponent;
import org.terasology.registry.In;
import org.terasology.rendering.logic.SkeletalMeshComponent;
import org.terasology.tutorialpathfinding.events.CharacterSpawnEvent;

@RegisterSystem(RegisterMode.AUTHORITY)
public class SpawnSystem extends BaseComponentSystem {

    @In
    private EntityManager entityManager;

    @In
    private LocalPlayer localPlayer;

    @Override
    public void postBegin() {
        Prefab baseGooey = entityManager.getPrefabManager().getPrefab("TutorialPathfinding:baseGooey");
        localPlayer.getCharacterEntity().send(new CharacterSpawnEvent(baseGooey, new Vector3f(0, 12, 2)));


    }


    @ReceiveEvent
    public void characterSpawn(CharacterSpawnEvent event, EntityRef character) {
        Prefab prefabToSpawn = event.getCharacterPrefab();
        Vector3f spawnPosition = event.getSpawnPosition();

        EntityRef newCharacter = entityManager.create(prefabToSpawn, spawnPosition);
        NetworkComponent networkComponent = new NetworkComponent();
        networkComponent.replicateMode = NetworkComponent.ReplicateMode.ALWAYS;
        newCharacter.addComponent(networkComponent);
        BoxShapeComponent boxShape = new BoxShapeComponent();
        SkeletalMeshComponent skeletalMesh = newCharacter.getComponent(SkeletalMeshComponent.class);
        boxShape.extents = skeletalMesh.mesh.getStaticAabb().getExtents().scale(2.0f);

        RigidBodyComponent rigidBody = newCharacter.getComponent(RigidBodyComponent.class);
        rigidBody.collidesWith = Lists.<CollisionGroup>newArrayList(StandardCollisionGroup.DEFAULT, StandardCollisionGroup.CHARACTER, StandardCollisionGroup.WORLD);

        newCharacter.addOrSaveComponent(boxShape);
        newCharacter.saveComponent(rigidBody);

    }
}
