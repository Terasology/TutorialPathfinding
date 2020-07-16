// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.tutorialpathfinding.events;

import org.terasology.entitySystem.event.Event;
import org.terasology.math.geom.Vector3i;

import java.util.ArrayList;

public class HighlightPathEvent implements Event {
    private ArrayList<Vector3i> blockPositions;

    public ArrayList<Vector3i> getBlockPositions() {
        return blockPositions;
    }
    public HighlightPathEvent(ArrayList<Vector3i> blockPositions){
        setBlockPositions(blockPositions);
    }

    public void setBlockPositions(ArrayList<Vector3i> blockPositions) {
        this.blockPositions = blockPositions;
    }
}
