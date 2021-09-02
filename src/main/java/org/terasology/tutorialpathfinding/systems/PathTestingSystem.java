// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.tutorialpathfinding.systems;


import org.joml.Vector3f;
import org.joml.Vector3i;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.common.ActivateEvent;
import org.terasology.engine.logic.players.LocalPlayer;
import org.terasology.engine.logic.players.event.OnPlayerSpawnedEvent;
import org.terasology.engine.registry.CoreRegistry;
import org.terasology.engine.registry.In;
import org.terasology.engine.world.block.BlockManager;
import org.terasology.engine.world.block.items.BlockItemFactory;
import org.terasology.module.inventory.systems.InventoryManager;
import org.terasology.navgraph.Floor;
import org.terasology.navgraph.NavGraphSystem;
import org.terasology.navgraph.WalkableBlock;
import org.terasology.pathfinding.componentSystem.PathfinderSystem;
import org.terasology.pathfinding.model.LineOfSight2d;
import org.terasology.pathfinding.model.Path;
import org.terasology.pathfinding.model.Pathfinder;
import org.terasology.tutorialpathfinding.PathHighlighting.HighlightBlockEvent;
import org.terasology.tutorialpathfinding.PathHighlighting.HighlightPathEvent;
import org.terasology.tutorialpathfinding.components.FindBlockTesterComponent;
import org.terasology.tutorialpathfinding.components.PathEndComponent;
import org.terasology.tutorialpathfinding.components.PathStartComponent;

import java.util.ArrayList;

@RegisterSystem
public class PathTestingSystem extends BaseComponentSystem {

    Logger logger = LoggerFactory.getLogger(PathTestingSystem.class);

    @In
    private EntityManager entityManager;

    @In
    private LocalPlayer localPlayer;

    @In
    private BlockManager blockManager;

    @In
    private InventoryManager inventoryManager;

    @In
    private transient NavGraphSystem navGraphSystem;

    @In
    private transient PathfinderSystem pathfinderSystem;

    private Vector3f startPathPosition = new Vector3f();


    @Override
    public void postBegin() {
        super.postBegin();
        setup();

    }

    public void setup() {
        navGraphSystem = CoreRegistry.get(NavGraphSystem.class);
        pathfinderSystem = CoreRegistry.get(PathfinderSystem.class);
    }

    @ReceiveEvent
    public void onPlayerSpawn(OnPlayerSpawnedEvent event, EntityRef player) {

        inventoryManager.giveItem(player, player, entityManager.create("TutorialPathfinding:startPath"));
        inventoryManager.giveItem(player, player, entityManager.create("TutorialPathfinding:endPath"));
        inventoryManager.giveItem(player, player, entityManager.create("TutorialPathfinding:findBlockTester"));
        BlockItemFactory blockFactory = new BlockItemFactory(entityManager);
        EntityRef planks = blockFactory.newInstance(blockManager.getBlockFamily("coreassets:Plank"), 99);
        inventoryManager.giveItem(player, player, planks);
        inventoryManager.giveItem(player, player, entityManager.create("TutorialPathfinding:spawnEntitiesItem"));

        inventoryManager.giveItem(player, player, blockFactory.newInstance(blockManager.getBlockFamily(
                "TutorialPathfinding:spawner"), 2));

        inventoryManager.giveItem(player, player, blockFactory.newInstance(blockManager.getBlockFamily(
                "TutorialPathfinding:target"), 2));


    }

    @ReceiveEvent(components = PathStartComponent.class)
    public void startPath(ActivateEvent event, EntityRef entityRef) {
        //logger.error("path start at {}",event.getHitPosition().toString());
        logger.error("path start at {}", event.getInstigatorLocation().toString());
        startPathPosition = (event.getInstigatorLocation());

        Floor currentFloor = pathfinderSystem.getBlock(event.getInstigatorLocation()).floor;
        logger.error(currentFloor.getMap().toString());


    }

    @ReceiveEvent(components = FindBlockTesterComponent.class)
    public void findBlockUnder(ActivateEvent event, EntityRef entityRef) {
        //logger.error("path start at {}",event.getHitPosition().toString());

        Vector3f playerPos = (event.getInstigatorLocation());

        WalkableBlock result = pathfinderSystem.getBlock(event.getInstigator());
        if (result == null) {
            logger.error("result is null");
        } else {
            logger.error(result.toString());
        }

        Vector3i blockToBeHighlighted = result.getBlockPosition();

        HighlightBlockEvent highlightBlockEvent = new HighlightBlockEvent(blockToBeHighlighted);

        event.getInstigator().send(highlightBlockEvent);


    }

    @ReceiveEvent(components = PathEndComponent.class)
    public void endPath(ActivateEvent event, EntityRef entityRef) {
        // logger.error(entityRef.toFullDescription());
        logger.error("path end at {}", event.getInstigatorLocation().toString());
        logger.error("path start at {}", (startPathPosition).toString());

        if (pathfinderSystem == null) {
            setup();
            logger.error("Pathfinder system is null here");
        }

        if (pathfinderSystem == null) {
            logger.error("pathfinder is null again");
        }

        WalkableBlock startwalkableBlock = pathfinderSystem.getBlock(startPathPosition);

        logger.error("Start block {}", startwalkableBlock.toString());
        WalkableBlock endWalkableBlock = pathfinderSystem.getBlock(event.getInstigatorLocation());
        logger.error("End block {}", endWalkableBlock.toString());

        Pathfinder pathfinder = new Pathfinder(navGraphSystem, new LineOfSight2d());

        Path path = pathfinder.findPath(endWalkableBlock, startwalkableBlock);

        ArrayList<WalkableBlock> nodes = path.getNodes();
        ArrayList<Vector3i> pathBlockPositions = new ArrayList<>();


        for (WalkableBlock pathBlock : nodes) {
            Vector3i pathBlockPos = pathBlock.getBlockPosition();
            pathBlockPositions.add(pathBlockPos);


        }
        //pathBlockPositions.add(endWalkableBlock.getBlockPosition());
        pathBlockPositions.add(0, endWalkableBlock.getBlockPosition());

        for (Vector3i pos : pathBlockPositions) {
            logger.error("Block at {} ", pos.toString());
        }
        event.getInstigator().send(new HighlightPathEvent(pathBlockPositions));

        //logger.error(path.toString());


    }


}
