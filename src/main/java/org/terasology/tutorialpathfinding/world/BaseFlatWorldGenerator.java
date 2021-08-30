// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.tutorialpathfinding.world;

import org.terasology.engine.core.SimpleUri;
import org.terasology.engine.registry.In;
import org.terasology.engine.world.generation.BaseFacetedWorldGenerator;
import org.terasology.engine.world.generation.WorldBuilder;
import org.terasology.engine.world.generator.RegisterWorldGenerator;
import org.terasology.engine.world.generator.plugin.WorldGeneratorPluginLibrary;
import org.terasology.core.world.generator.facetProviders.SeaLevelProvider;

@RegisterWorldGenerator(id = "FlatWorld", displayName = "Flat World for Pathfinding")
public class BaseFlatWorldGenerator extends BaseFacetedWorldGenerator {
    @In
    private WorldGeneratorPluginLibrary worldGeneratorPluginLibrary;

    public BaseFlatWorldGenerator(SimpleUri uri) {
        super(uri);
    }

    @Override
    protected WorldBuilder createWorld() {
        return new WorldBuilder(worldGeneratorPluginLibrary)
                .addProvider(new BaseFlatSurfaceProvider())
                .addProvider(new SeaLevelProvider(0))
                .addRasterizer(new BaseFlatWorldRasterizer());
    }
}
