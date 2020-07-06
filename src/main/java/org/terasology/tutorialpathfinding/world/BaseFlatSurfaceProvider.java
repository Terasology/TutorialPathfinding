// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.tutorialpathfinding.world;

import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Rect2i;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Produces;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

@Produces(SurfaceHeightFacet.class)
public class BaseFlatSurfaceProvider implements FacetProvider {
    static final int SURFACE_HEIGHT = 10;


    @Override
    public void setSeed(long seed) {

    }

    @Override
    public void initialize() {

    }

    @Override
    public void process(GeneratingRegion region) {

        Border3D border3D = region.getBorderForFacet(SurfaceHeightFacet.class);
        SurfaceHeightFacet surfaceHeightFacet = new SurfaceHeightFacet(region.getRegion(), border3D);

        Rect2i processRegion = surfaceHeightFacet.getWorldRegion();

        for (BaseVector2i pos : processRegion.contents()) {
            surfaceHeightFacet.setWorld(pos,SURFACE_HEIGHT);
        }

        region.setRegionFacet(SurfaceHeightFacet.class,surfaceHeightFacet);

    }
}
