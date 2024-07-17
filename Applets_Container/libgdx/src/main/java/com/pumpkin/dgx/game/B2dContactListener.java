package com.pumpkin.dgx.game;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * Created by julienvillegas on 07/12/2017.
 */

public class B2dContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        String classA = contact.getFixtureA().getBody().getUserData().getClass().getName();
        String classB = contact.getFixtureB().getBody().getUserData().getClass().getName();

        //Gdx.app.debug("begin Contact","between: "+classA+" and "+ classB);
        if(classA.equalsIgnoreCase(WindowsFrame.class.getName()) && classB.equalsIgnoreCase(Ball.class.getName())){
            Ball a = (Ball)(contact.getFixtureB().getBody().getUserData());
            a.eliminate();
            MyGdxGame.ballNbr--;

        }
        else if(classB.equalsIgnoreCase(WindowsFrame.class.getName()) && classA.equalsIgnoreCase(Ball.class.getName())){
            Ball a = (Ball)(contact.getFixtureA().getBody().getUserData());
            a.eliminate();
            MyGdxGame.ballNbr--;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
