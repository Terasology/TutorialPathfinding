// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.tutorialpathfinding.PathHighlighting;

import org.joml.Vector3i;
import org.terasology.engine.entitySystem.event.Event;

public class HighlightBlockEvent implements Event {

    private Vector3i blockPos;

    public HighlightBlockEvent(Vector3i blockPosition) {
        setBlockPositions(blockPosition);
    }

    public Vector3i getBlockPosition() {
        return blockPos;
    }

    public void setBlockPositions(Vector3i blockPosition) {
        this.blockPos = blockPosition;
    }

}
