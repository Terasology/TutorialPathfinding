// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.tutorialpathfinding.Actions;


import org.terasology.engine.logic.behavior.BehaviorAction;
import org.terasology.engine.logic.behavior.core.Actor;
import org.terasology.engine.logic.behavior.core.BaseAction;
import org.terasology.engine.logic.behavior.core.BehaviorState;

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


