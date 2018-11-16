/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Thursday, 15th November 2018
 * Modified By: MAREL Maud
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.app.room;

//import java.util.HashMap;

public class Room implements ImmutableRoom {
    
    private String _name; //Name of the Room -> add to VP   
    private double _height;
    private double _width;
    private Scene _scene;
    //private HashMap<Integer, SittingSection> _sittingSections = new HashMap();
    //private HashMap<Integer, StandingSection> _standingSections = new HashMap();
    
    public Room(String name, double height, double width) {
        this._name = name;
        this._height = height;
        this._width = width;
    }

    // Room Methods
    public void setName(String name) {
        this._name = name;
    }

    public String getName() {
        return this._name;
    }

    public void setHeight(double height) {
        this._height = height;
    }

    public double getHeight() {
        return this._height;
    }

    public void setWidth(double width) {
        this._width = width;
    }

    public double getWidth() {
        return this._width;
    }

    // Scene Methods
    public void createScene(double width, double height, double[] positions) {
        this._scene = new Scene(width, height, positions);
    }

    public Scene getScene() {
        return this._scene;
    }

    public void deleteScene() {
        this._scene = null;
    }

    public void setSceneWidth(double width) {
        this._scene.setWidth(width);
    }

    public void setSceneHeight(double height) {
        this._scene.setHeight(height);
    }

    public void setScenePositions(double[] positions) {
        this._scene.setPositions(positions);
    }

    public ImmutableScene getImmutableScene() {
        return this._scene;
    }
}