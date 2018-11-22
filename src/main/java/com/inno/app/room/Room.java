/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Thursday, 22nd November 2018
 * Modified By: MAREL Maud
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.app.room;

import java.util.HashMap;
import java.util.ArrayList;

public class Room implements ImmutableRoom {
    
    private String _name; //Name of the Room -> add to VP   
    private double _height;
    private double _width;
    private Scene _scene;
    private VitalSpace _vitalSpace;
    private HashMap<String, SittingSection> _sittingSections = new HashMap<String, SittingSection>();
    private HashMap<String, StandingSection> _standingSections = new HashMap<String, StandingSection>();
    private ArrayList<Integer> _idSection = new ArrayList<Integer>();
    private Integer _idSectionMax;
    
    public Room(String name, double height, double width, double heightVitalSpace, double widthVitalSpace) {
        this._name = name;
        this._height = height;
        this._width = width;
        this._idSectionMax = 0;
        this._vitalSpace = new VitalSpace(heightVitalSpace, widthVitalSpace);
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

    public void setHeightVitalSpace(double height) {
        this._vitalSpace.setHeight(height);
    }

    public void setWidthVitalSpace(double width) {
        this._vitalSpace.setWidth(width);
    }

    public ImmutableVitalSpace getImmutableVitalSpace() {
        return this._vitalSpace;
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

    public void setSceneRotation(double rotation) {
        this._scene.setRotation(rotation);
    }

    public ImmutableScene getImmutableScene() {
        return this._scene;
    }

    //Section Methods
    public ImmutableSection getImmutableSectionById(String idSection) {
        ImmutableSection section = null;
        if ((section = this._sittingSections.get(idSection)) != null) {
            return section;
        }
        else if ((section = this._standingSections.get(idSection)) != null) {
            return section;
        }
        return section;
    }

    public void setSectionId(String oldIdSection, String newIdSection) {
        SittingSection sittingSection = null;
        StandingSection standingSection = null;

        if ((sittingSection = this._sittingSections.remove(oldIdSection)) != null) {
            this._sittingSections.put(newIdSection, sittingSection);
            sittingSection.setIdSection(newIdSection);
        }
        else {
            standingSection = this._standingSections.remove(oldIdSection);
            this._standingSections.put(newIdSection, standingSection);
            standingSection.setIdSection(newIdSection);
        }
        try {
            Integer.parseInt(oldIdSection);
            this._idSection.add(Integer.parseInt(oldIdSection));
        }
        catch(NumberFormatException e) {
            System.out.println("OldSectionId not an integer, don't need to add in arraylist");
        }
    }

    public Section getSectionById(String idSection) {
        Section section = null;
        if ((section = this._sittingSections.get(idSection)) != null) {
            return section;
        }
        else if ((section = this._standingSections.get(idSection)) != null) {
            return section;
        }
        return section;
    }

    public void setSectionElevation(String idSection, double elevation) {
        Section section = getSectionById(idSection);
        section.setElevation(elevation);
    }

    public void updateSectionPositions(String idSection, double[] positions) {
        Section section = getSectionById(idSection);
        section.updatePosition(positions);
    }

    public void setSectionRotation(String idSection, double rotation) {
        Section section = getSectionById(idSection);
        section.setRotation(rotation);
    }

    public void deleteSection(String idSection) {
        this._sittingSections.remove(idSection);
        this._standingSections.remove(idSection);
        this._idSection.add(Integer.parseInt(idSection));
    }

    public String findFreeId() {
        int idSection = 0;

        if (this._idSection.size() != 0) {
            idSection = this._idSection.remove(0);
        }
        else {
            this._idSectionMax += 1;
            idSection = this._idSectionMax;
        }
        return Integer.toString(idSection);
    }

        //standingSection Methods
    public ImmutableStandingSection createStandingSection(double elevation, int nbPeople, double[] positions, double rotation) {
        String id = findFreeId();
        StandingSection standingSection = new StandingSection(id, elevation, positions, nbPeople, rotation);
        this._standingSections.put(id, standingSection);
        return standingSection;
    }

    /*public ImmutableStandingSection getImmutableStandingSection(int idSection) {

    }*/

    public void setStandingNbPeople(String idSection, int nbPeople) {
        StandingSection standingSection = this._standingSections.get(idSection);
        standingSection.setNbPeople(nbPeople);
    }

        //sittingSection Methods
    public ImmutableSittingSection createSittingSection(double elevation, double[] positions, double rotation) {
        String id = findFreeId();
        double vitalSpaceHeight = this.getImmutableVitalSpace().getHeight();
        double vitalSpaceWidth = this.getImmutableVitalSpace().getWidth();
        SittingSection sittingSection = new SittingSection(id, elevation, positions, rotation, vitalSpaceHeight, vitalSpaceWidth);
        this._sittingSections.put(id, sittingSection);
        return sittingSection;
    }

    public void setSittingSectionVitalSpace(String sectionId, double width, double height) {
        SittingSection sittingSection = this._sittingSections.get(sectionId);
        sittingSection.setVitalSpace(width, height);
    }
}