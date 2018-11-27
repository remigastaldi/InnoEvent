/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Tuesday, 27th November 2018
 * Modified By: HUBERT Léo

 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.app.room;

import com.inno.service.Point;

import java.util.HashMap;
import java.io.Serializable;
import java.util.ArrayList;

public class Room implements ImmutableRoom, Serializable {

    private static final long serialVersionUID = 1L;
    private String _name; // Name of the Room -> add to VP
    private double _height;
    private double _width;
    private Scene _scene;
    private VitalSpace _vitalSpace;
    private HashMap<String, SittingSection> _sittingSections = new HashMap<String, SittingSection>();
    private HashMap<String, StandingSection> _standingSections = new HashMap<String, StandingSection>();

    public Room(String name, double height, double width, double heightVitalSpace, double widthVitalSpace) {
        this._name = name;
        this._height = height;
        this._width = width;
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
    public ImmutableScene createScene(double width, double height, double[] positions) {
        this._scene = new Scene(width, height, positions);
        return _scene;
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

    // Section Methods
    public ImmutableSection getImmutableSectionById(String idSection) {
        ImmutableSection section = null;

        if ((section = this._sittingSections.get(idSection)) != null) {
            return section;
        } else if ((section = this._standingSections.get(idSection)) != null) {
            return section;
        }
        return section;
    }

    public void setSectionName(String idSection, String name) {
        SittingSection sittingSection = null;
        StandingSection standingSection = null;

        if ((sittingSection = this._sittingSections.get(idSection)) != null) {
            sittingSection.setNameSection(name);
        } else if ((standingSection = this._standingSections.get(idSection)) != null) {
            standingSection.setNameSection(name);
        }
    }

    public Section getSectionById(String idSection) {
        Section section = null;
        if ((section = this._sittingSections.get(idSection)) != null) {
            return section;
        } else if ((section = this._standingSections.get(idSection)) != null) {
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
    }

    // standingSection Methods
    public ImmutableStandingSection createStandingSection(int nbPeople, double[] positions, double rotation) {
        String id = Integer.toString(this._sittingSections.size() + this._standingSections.size() + 1);
        StandingSection standingSection = new StandingSection("Untitled" + id, id, positions, nbPeople, rotation);
        this._standingSections.put(id, standingSection);
        return standingSection;
    }

    public void setStandingNbPeople(String idSection, int nbPeople) {
        StandingSection standingSection = this._standingSections.get(idSection);
        standingSection.setNbPeople(nbPeople);
    }

        //sittingSection Methods
    public ImmutableSittingSection createSittingSection(double[] positions, double rotation, boolean isRectangle) {
        String id = Integer.toString(this._sittingSections.size() + this._standingSections.size() + 1);
        double vitalSpaceHeight = this.getImmutableVitalSpace().getHeight();
        double vitalSpaceWidth = this.getImmutableVitalSpace().getWidth();
        SittingSection sittingSection = new SittingSection("Untitled" + id, id, positions, rotation, vitalSpaceHeight, vitalSpaceWidth, isRectangle);
        this._sittingSections.put(id, sittingSection);
        return sittingSection;
    }

    public void setSittingSectionVitalSpace(String sectionId, double width, double height) {
        SittingSection sittingSection = this._sittingSections.get(sectionId);
        sittingSection.setVitalSpace(width, height);
    }

    public void setSittingSectionAutoDistribution(String sectionId, boolean autoDistrib) {
        SittingSection sittingSection = this._sittingSections.get(sectionId);
        sittingSection.setAutoDistribution(autoDistrib);
    }

    public ImmutableSittingRow createSittingRow(String sectionId, double[] posStart, double[] posEnd) {
        SittingSection sittingSection = this._sittingSections.get(sectionId);
        ImmutableSittingRow sittingSectionRow = sittingSection.createRow(posStart, posEnd);
        return sittingSectionRow;
    }

    public void deleteSittingRow(String sectionId, String idRow) {
        SittingSection sittingSection = this._sittingSections.get(sectionId);
        sittingSection.deleteRow(idRow);
    }

    public void clearAllSittingRows(String sectionId) {
        SittingSection sittingSection = this._sittingSections.get(sectionId);
        sittingSection.clearAllRows();
    }

    public ImmutableSeat createSeat(String sectionId, String idRow, double[] pos) {
        SittingSection sittingSection = this._sittingSections.get(sectionId);
        ImmutableSeat sittingSectionSeat = sittingSection.createSeat(idRow, pos);
        return sittingSectionSeat;
    }

    @Override
    public HashMap<String, ? extends ImmutableSittingSection> getImmutableSittingSections() {
        return _sittingSections;
    }

    @Override
    public HashMap<String, ? extends ImmutableStandingSection> getImmutableStandingSections() {
        return _standingSections;
    }
}