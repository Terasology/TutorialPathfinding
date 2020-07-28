// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.tutorialpathfinding.PathHighlighting;

import org.terasology.entitySystem.event.Event;
import org.terasology.math.geom.Vector3i;

import java.util.ArrayList;

public class HighlightBlockEvent implements Event {

    private Vector3i blockPos;

    public Vector3i getBlockPosition() {
        return blockPos;
    }
    public HighlightBlockEvent(Vector3i blockPosition){
        setBlockPositions(blockPosition);
    }

    public void setBlockPositions(Vector3i blockPosition) {
        this.blockPos = blockPosition;
    }

}
