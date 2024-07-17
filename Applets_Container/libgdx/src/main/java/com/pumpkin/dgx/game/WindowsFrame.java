package com.pumpkin.dgx.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by julienvillegas on 07/12/2017.
 */

public class WindowsFrame {

    private Body body;
    private World world;

    public WindowsFrame(World aWorld, float width, float heigth) {


        world = aWorld;
        BodyDef bd = new BodyDef();
        bd.position.set(-20,-5);
        bd.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bd);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(40, 1);
        body.createFixture(groundBox, 0.0f);
        body.setUserData(this);


    }


}
