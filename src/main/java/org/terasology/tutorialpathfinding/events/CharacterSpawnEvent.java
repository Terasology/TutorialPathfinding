// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.tutorialpathfinding.events;

import org.joml.Vector3f;
import org.terasology.engine.entitySystem.event.Event;
import org.terasology.engine.entitySystem.prefab.Prefab;


public class CharacterSpawnEvent implements Event {
    private Prefab prefab;
    private Vector3f location;

    public CharacterSpawnEvent() {
    }

    public CharacterSpawnEvent(Prefab prefab, Vector3f location) {
        this.prefab = prefab;
        this.location = location;
    }

    public Prefab getCharacterPrefab() {
        return this.prefab;
    }

    public Vector3f getSpawnPosition() {
        return this.location;
    }
}
