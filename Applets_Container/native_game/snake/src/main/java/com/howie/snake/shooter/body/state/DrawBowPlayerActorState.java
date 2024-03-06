package com.howie.snake.shooter.body.state;

public abstract class DrawBowPlayerActorState extends PlayerActorState {

//    public void act(PlayerActor parentActor) {
//        final AbstractInputDevice input = parentActor.engine.controllingInputDevice;
//        aim(parentActor, input);
//
//        parentActor.addVelocity(0.25 * input.horizontalMoveButton, 0.25 * input.verticalMoveButton);
//
//        if (triggerPulled(parentActor)) fire(parentActor);
//
//        if (buttonPressed(input) == false) {
//            parentActor.state = moveState.entryState(parentActor);
//        }
//    }
//
//    public abstract void aim(PlayerActor parentActor, AbstractInputDevice input);
//
//    public abstract void fire(PlayerActor parentActor);
//
//    public abstract boolean buttonPressed(AbstractInputDevice input);
//
//    public abstract boolean triggerPulled(PlayerActor parentActor);
}
