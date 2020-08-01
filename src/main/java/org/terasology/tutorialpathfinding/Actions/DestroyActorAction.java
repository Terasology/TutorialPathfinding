// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.tutorialpathfinding.Actions;



import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;
import org.terasology.logic.behavior.BehaviorAction;
import org.terasology.logic.behavior.core.Actor;
import org.terasology.logic.behavior.core.BaseAction;
import org.terasology.logic.behavior.core.BehaviorState;
import org.terasology.math.geom.Vector3f;
import org.terasology.minion.move.MinionMoveComponent;
import org.terasology.navgraph.NavGraphSystem;
import org.terasology.navgraph.WalkableBlock;
import org.terasology.pathfinding.componentSystem.PathfinderSystem;
import org.terasology.pathfinding.model.Path;
import org.terasology.registry.CoreRegistry;
import org.terasology.registry.In;

import java.util.Arrays;
import java.util.List;

/**
 * Requests a path to a target defined using the <b>MinionMoveComponent.target</b>.<br/>
 * <br/>
 * <b>SUCCESS</b> / <b>FAILURE</b>: when paths is found or not found (invalid).<br/>
 * <b>RUNNING</b>: as long as path is searched.<br/>
 * <br/>
 * Auto generated javadoc - modify README.markdown instead!
 */
@BehaviorAction(name = "destroy_entity")
public class DestroyActorAction extends BaseAction {



    @Override
    public void setup() {
    }

    @Override
    public void construct(final Actor actor) {


    }

    @Override
    public BehaviorState modify(Actor actor, BehaviorState result) {
        actor.getEntity().destroy();
        return BehaviorState.SUCCESS;
    }
}


