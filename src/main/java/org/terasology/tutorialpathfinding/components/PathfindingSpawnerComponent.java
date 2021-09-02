// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.tutorialpathfinding.components;

import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.gestalt.entitysystem.component.Component;

public class PathfindingSpawnerComponent implements Component<PathfindingSpawnerComponent> {

    public Prefab prefabToSpawn;

    @Override
    public void copyFrom(PathfindingSpawnerComponent other) {
        this.prefabToSpawn = other.prefabToSpawn;
    }
}
