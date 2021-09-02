// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.tutorialpathfinding.PathHighlighting;

import org.joml.Vector3i;
import org.terasology.engine.entitySystem.event.Event;

import java.util.ArrayList;

public class HighlightPathEvent implements Event {
    private ArrayList<Vector3i> blockPositions;

    public HighlightPathEvent(ArrayList<Vector3i> blockPositions) {
        setBlockPositions(blockPositions);
    }

    public ArrayList<Vector3i> getBlockPositions() {
        return blockPositions;
    }


    public void setBlockPositions(ArrayList<Vector3i> blockPositions) {
        this.blockPositions = blockPositions;
    }
}
