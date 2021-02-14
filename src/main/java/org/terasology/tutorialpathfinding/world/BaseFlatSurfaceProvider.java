// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.tutorialpathfinding.world;

import org.joml.Vector2ic;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Produces;
import org.terasology.world.generation.facets.ElevationFacet;

/**
 * Provides a flat SurfaceHeightFacet set at SURFACE_HEIGHT
 */
@Produces(ElevationFacet.class)
public class BaseFlatSurfaceProvider implements FacetProvider {
    // Surface height of the flat surface
    static final int SURFACE_HEIGHT = 10;

    /**
     * @param seed the seed value (typically used for random number generators)
     */
    @Override
    public void setSeed(long seed) {

    }

    /**
     * This is always called after {@link #setSeed(long)}.
     */
    @Override
    public void initialize() {

    }

    @Override
    public void process(GeneratingRegion region) {

        Border3D border3D = region.getBorderForFacet(ElevationFacet.class);
        ElevationFacet elevationFacet = new ElevationFacet(region.getRegion(), border3D);

        // Iterating through the positions in the region to be processed and setting the value to SURFACE_HEIGHT at
        // each point
        for (Vector2ic pos : elevationFacet.getRelativeArea()) {
            elevationFacet.setWorld(pos, SURFACE_HEIGHT);
        }

        // Pass our newly created and populated facet to the region
        region.setRegionFacet(ElevationFacet.class, elevationFacet);

    }
}
