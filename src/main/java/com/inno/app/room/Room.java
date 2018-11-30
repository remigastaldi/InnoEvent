/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Thursday, 29th November 2018
 * Modified By: GASTALDI Rémi

 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.app.room;

import com.inno.app.Core;
import com.inno.service.Point;
import com.inno.service.Utils;

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
        Section section = null;
        if ((section = this._sittingSections.get(idSection)) != null) {
            if (((ImmutableSittingSection)section).isRectangle()) {
                clearAllSittingRows(section.getIdSection());
                double xRow = positions[0];
                double yRow = positions[1];
                double xSeat = positions[0];
                double ySeat = positions[1];
                double vitalSpaceHeight = ((ImmutableSittingSection)section).getImmutableVitalSpace().getHeight();
                double vitalSpaceWidth = ((ImmutableSittingSection)section).getImmutableVitalSpace().getWidth();
                
                // System.out.println("....................... " + yRow + " : " + positions[7]);
                while (yRow < positions[7]) {
                    double[] posStart = { xRow + (vitalSpaceWidth / 2), yRow + (vitalSpaceHeight / 2) };
                    double[] posEnd = { positions[2] - (vitalSpaceWidth / 2), yRow + (vitalSpaceHeight / 2) };
                    ImmutableSittingRow row = Core.get().createSittingRow(section.getIdSection(), posStart, posEnd);
    
                    while (xSeat < positions[2]) {
                        double[] seatPos = { xSeat + (vitalSpaceWidth / 2), ySeat + (vitalSpaceHeight / 2) };
                        Core.get().createSeat(section.getIdSection(), row.getIdRow(), seatPos);
                        xSeat += vitalSpaceWidth;
                    }
                    yRow += vitalSpaceHeight;
                    xSeat = positions[0];
                    ySeat += vitalSpaceHeight;
                }
            }
            section.updatePosition(positions);
        } else if ((section = this._standingSections.get(idSection)) != null) {
            section.updatePosition(positions);
        }
    }

    public void setSectionRotation(String idSection, double rotation) {
        Section section = getSectionById(idSection);

        if (section == null) {
            System.out.println("Bad section ID");
            return;
        }

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

    // sittingSection Methods
    public ImmutableSittingSection createSittingSection(double[] positions, double rotation, boolean isRectangle) {
        String id = Integer.toString(this._sittingSections.size() + this._standingSections.size() + 1);
        double vitalSpaceHeight = this.getImmutableVitalSpace().getHeight();
        double vitalSpaceWidth = this.getImmutableVitalSpace().getWidth();
        SittingSection sittingSection = new SittingSection("Untitled" + id, id, positions, rotation, vitalSpaceHeight,
                vitalSpaceWidth, isRectangle);
        this._sittingSections.put(id, sittingSection);

        if (isRectangle) {
            double xRow = positions[0];
            double yRow = positions[1];
            double xSeat = positions[0];
            double ySeat = positions[1];

            while (yRow < positions[7]) {
                double[] posStart = { xRow + (vitalSpaceWidth / 2), yRow + (vitalSpaceHeight / 2) };
                double[] posEnd = { positions[2] - (vitalSpaceWidth / 2), yRow + (vitalSpaceHeight / 2) };
                ImmutableSittingRow row = Core.get().createSittingRow(sittingSection.getIdSection(), posStart, posEnd);

                while (xSeat < positions[2]) {
                    double[] seatPos = { xSeat + (vitalSpaceWidth / 2), ySeat + (vitalSpaceHeight / 2) };
                    Core.get().createSeat(sittingSection.getIdSection(), row.getIdRow(), seatPos);
                    xSeat += vitalSpaceWidth;
                }
                yRow += vitalSpaceHeight;
                xSeat = positions[0];
                ySeat += vitalSpaceHeight;
            }
        }
        else
        {
            boolean firstSeat = false;
            Point sceneCenter = new Point(_scene.getCenter()[0],_scene.getCenter()[1]);
            Point[] p_Polygon = Utils.dArray_To_pArray(positions);
            Point centroid = Utils.centroid(p_Polygon);
            double angle = Utils.calculateLeftSideRotationAngle(sceneCenter, centroid);
            Point[] polyTemp = Utils.rotatePolygon(p_Polygon,sceneCenter,angle);

            Point lowerLeft = new Point(Utils.findLeftmostPoint(polyTemp).get_x(), Utils.findLowestPoint(polyTemp).get_y());
            Point lowerRight = new Point(Utils.findRightmostPoint(polyTemp).get_x(), Utils.findLowestPoint(polyTemp).get_y());
            Point higherLeft = new Point(Utils.findLeftmostPoint(polyTemp).get_x(), Utils.findHighestPoint(polyTemp).get_y());
            Point higherRight = new Point(Utils.findRightmostPoint(polyTemp).get_x(), Utils.findHighestPoint(polyTemp).get_y());
            ArrayList<Point> coord = new ArrayList<>();

            double posx = lowerRight.get_x()+vitalSpaceWidth/2;
            boolean rowCreated = false;
            do
            {
                if(!firstSeat)
                {
                    posx -= 0.1;
                }
                if(firstSeat)
                {
                    posx -= vitalSpaceWidth;
                }


                double posy = lowerLeft.get_y()+vitalSpaceHeight/2;

                do
                {
//                    posy-=vitalSpaceHeight;
                    if(rowCreated)
                    {
                        posy-=vitalSpaceHeight;
                    }
                    if(!rowCreated)
                    {
                        posy -=0.1;
                    }

                    Point pt = new Point(posx, posy);
                    Point pt1 = new Point(posx-vitalSpaceWidth/2, posy);
                    Point pt2 = new Point(posx, posy-vitalSpaceHeight);
                    Point pt3 = new Point(posx+vitalSpaceWidth/2, posy);
                    Point pt4 = new Point(posx, posy+vitalSpaceHeight);

//                    if(!rowCreated)
//                    {
//                        while((!(Utils.insidePolygon(polyTemp, pt1)&&Utils.insidePolygon(polyTemp, pt2)&&
//                                Utils.insidePolygon(polyTemp, pt3)&&Utils.insidePolygon(polyTemp, pt4)))||pt.get_y()>higherLeft.get_y())
//                        {
//                            double y = pt.get_y()-0.1;
//                            pt.set_y(y);
//                            pt1.set_y(y);
//                            pt2.set_y(y-vitalSpaceHeight);
//                            pt3.set_y(y);
//                            pt4.set_y(y-vitalSpaceHeight);
//                        }
//                    }


                    if(Utils.insidePolygon(polyTemp, pt1)&&Utils.insidePolygon(polyTemp, pt2)&&
                            Utils.insidePolygon(polyTemp, pt3)&&Utils.insidePolygon(polyTemp, pt4))
                    {
                        rowCreated = true;
                        firstSeat = true;
                        coord.add(pt);
                    }

                    if(rowCreated && !(Utils.insidePolygon(polyTemp, pt1)&&Utils.insidePolygon(polyTemp, pt2)&&
                            Utils.insidePolygon(polyTemp, pt3)&&Utils.insidePolygon(polyTemp, pt4)))
                    {
                        Point Start = Utils.rotatePoint(coord.get(0), sceneCenter, -angle);
                        Point End = Utils.rotatePoint(coord.get(coord.size()-1), sceneCenter, -angle);
                        double[] posStart = {Start.get_x(), Start.get_y()};
                        double[] posEnd = {End.get_x(),End.get_y()};
                        ImmutableSittingRow row = Core.get().createSittingRow(sittingSection.getIdSection(), posStart, posEnd);
                        for(Point point: coord)
                        {
                            Point rPoint = Utils.rotatePoint(point,sceneCenter, -angle);
                            double[] seatPos = {rPoint.get_x(), rPoint.get_y()};
                            Core.get().createSeat(sittingSection.getIdSection(), row.getIdRow(), seatPos);
                        }
                        rowCreated = false;
                        coord.clear();
                    }
                }while(posy>higherLeft.get_y());
            }while(posx>lowerLeft.get_x());
        }

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