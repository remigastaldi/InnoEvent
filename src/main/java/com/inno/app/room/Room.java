/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Wednesday, 12th December 2018
 * Modified By: MAREL Maud

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

    public Room(String name, double width, double height, double widthVitalSpace, double heightVitalSpace)  {
        this._name = name;
        this._width = width;
        this._height = height;
        this._vitalSpace = new VitalSpace(widthVitalSpace, heightVitalSpace);
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

    public void setVitalSpace(double width, double height) {
        for (SittingSection section : _sittingSections.values()) {
            if (section.getImmutableVitalSpace().getWidth() == _vitalSpace.getWidth()
                && section.getImmutableVitalSpace().getHeight() == _vitalSpace.getHeight()) {
                    section.setVitalSpace(width, height);
                    Core.get().updateSectionPositions(section.getIdSection(), section.getPositions(), section.isRectangle()); // Just to recalculate seats positions
            }
        }
        _vitalSpace.setWidth(width);
        _vitalSpace.setHeight(height);
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

    public void setSceneElevation(double elevation) {
        this._scene.setElevation(elevation);
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

    public ImmutableStandingSection sittingToStandingSection(String idSection) {
        ImmutableSittingSection oldSection = null;
        ImmutableStandingSection newSection = null;

        oldSection = this._sittingSections.get(idSection);
        newSection = this.createStandingSection(0, oldSection.getPositions(), oldSection.getRotation());
        this.getSectionById(newSection.getIdSection()).setNameSection(oldSection.getNameSection());
        this.getSectionById(newSection.getIdSection()).setElevation(oldSection.getElevation());
        deleteSection(idSection);
        return newSection;
    }

    public ImmutableSittingSection standingToSittingSection(String idSection) {
        ImmutableStandingSection oldSection = null;
        ImmutableSittingSection newSection = null;

        oldSection = this._standingSections.get(idSection);
        newSection = this.createSittingSection(oldSection.getPositions(), oldSection.getRotation(), false);
        this.getSectionById(newSection.getIdSection()).setNameSection(oldSection.getNameSection());
        this.getSectionById(newSection.getIdSection()).setElevation(oldSection.getElevation());
        deleteSection(idSection);
        return newSection;
    }

    public ImmutableSection duplicateSection(String idSection) {
        ImmutableSection oldSection = getImmutableSectionById(idSection);
        ImmutableSection newSection = null;
        String id = Integer.toString(this._sittingSections.size() + this._standingSections.size() + 1);

        try {
            newSection = (ImmutableSection) oldSection.clone();
            if (this._sittingSections.get(idSection) != null) {
                this.getSectionById(idSection).setIdSection(id);
                this._sittingSections.put(id, (SittingSection) newSection);
                this.getSectionById(id).setNameSection("Untitled" + id);
            } else if (this._standingSections.get(idSection) != null) {
                this.getSectionById(idSection).setIdSection(id);
                this._standingSections.put(id, (StandingSection) newSection);
                this.getSectionById(id).setNameSection("Untitled" + id);
            }
        } catch (CloneNotSupportedException e) {
            System.err.println(e);
        }
        return newSection;
    }

    public void setIdSection(String idSection, String newId) {
        SittingSection sittingSection = null;
        StandingSection standingSection = null;

        if ((sittingSection = this._sittingSections.get(idSection)) != null) {
            sittingSection.setIdSection(newId);
        } else if ((standingSection = this._standingSections.get(idSection)) != null) {
            standingSection.setIdSection(newId);
        }   
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
            clearAllSittingRows(section.getIdSection());
            if (((ImmutableSittingSection) section).isRectangle()) {
                updateRectangleRows(positions, section);
            } else {
                updatePolygonRows(positions, (SittingSection) section);
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

        rotation += section.getUserRotation();
        section.setRotation(rotation);
    }

    public void setSectionUserRotation(String idSection, double rotation) {
        Section section = getSectionById(idSection);

        if (section == null) {
            System.out.println("Bad section ID");
            return;
        }

        System.out.println("+++++++>> " + (section.getRotation() - section.getUserRotation()) + rotation);
        section.setRotation((section.getRotation() - section.getUserRotation()) + rotation);
        section.setUserRotation(rotation);
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
        double vitalSpaceWidth = this.getImmutableVitalSpace().getWidth();
        double vitalSpaceHeight = this.getImmutableVitalSpace().getHeight();
        SittingSection sittingSection = new SittingSection("Untitled" + id, id, positions, rotation, vitalSpaceWidth,
                vitalSpaceHeight, isRectangle);
        this._sittingSections.put(id, sittingSection);

        if (isRectangle) {
            updateRectangleRows(positions, sittingSection);
        } else {
            updatePolygonRows(positions, sittingSection);
        }

        return sittingSection;
    }

	private void updateRectangleRows(double[] positions, Section section) {
		double xRow = positions[0];
		double yRow = positions[1];
		double xSeat = positions[0];
		double ySeat = positions[1];
		double vitalSpaceHeight = ((ImmutableSittingSection) section).getImmutableVitalSpace().getHeight();
		double vitalSpaceWidth = ((ImmutableSittingSection) section).getImmutableVitalSpace().getWidth();

		while (yRow < positions[7] - vitalSpaceHeight * 0.99) {
            double[] posStart = { xRow + (vitalSpaceWidth / 2), yRow + (vitalSpaceHeight / 2) };
            double[] posEnd = { positions[0] + (int)((positions[2] - positions[0]) / vitalSpaceWidth) * vitalSpaceWidth - vitalSpaceWidth / 2, yRow + (vitalSpaceHeight / 2) };
            ImmutableSittingRow row = createSittingRow(section.getIdSection(), posStart, posEnd);
		    Core.get().createPlace(section.getIdSection() + "|" + row.getIdRow(), "#7289DA");
            
		    while (xSeat < positions[2] - vitalSpaceWidth * 0.99) {
                double[] seatPos = { xSeat + (vitalSpaceWidth / 2), ySeat + (vitalSpaceHeight / 2) };
		        ImmutableSeat seat = createSeat(section.getIdSection(), row.getIdRow(), seatPos);
		        Core.get().createPlace(section.getIdSection() + "|" + row.getIdRow() + "|" + seat.getId(), "#FFA500");
		        xSeat += vitalSpaceWidth;
		    }

		    yRow += vitalSpaceHeight;
            xSeat = positions[0];
            ySeat += vitalSpaceHeight;
        }
    }

	private void updatePolygonRows(double[] positions, SittingSection sittingSection) {
        // TODO: Why width and height are inverted
        double vitalSpaceWidth = sittingSection.getImmutableVitalSpace().getHeight();
        double vitalSpaceHeight = sittingSection.getImmutableVitalSpace().getWidth();
		boolean firstSeat = false;
		Point sceneCenter = new Point(_scene.getCenter()[0], _scene.getCenter()[1]);
		Point[] p_Polygon = Utils.dArray_To_pArray(positions);
		Point centroid = Utils.centroid(p_Polygon);
		double angle = Utils.calculateLeftSideRotationAngle(sceneCenter, centroid);
		Point[] polyTemp = Utils.rotatePolygon(p_Polygon, sceneCenter, angle);

		double leftMostX = Utils.findLeftmostPoint(polyTemp).get_x();
		double rightMostX = Utils.findRightmostPoint(polyTemp).get_x();
		double highestY = Utils.findHighestPoint(polyTemp).get_y();
		double lowestY = Utils.findLowestPoint(polyTemp).get_y();

		ArrayList<Point> coord = new ArrayList<>();

		double posx = rightMostX;
		boolean rowCreated = false;
		do {
		    if (!firstSeat) {
		        posx -= 0.1;
		    }
		    if (firstSeat) {
		        posx -= vitalSpaceWidth;
		    }

		    double posy = lowestY + vitalSpaceHeight / 2;

		    do {
		        if (!rowCreated) {
		            posy -= 0.1;
		        }
		        if (rowCreated) {
		            posy -= vitalSpaceHeight;
		        }

		        Point pt = new Point(posx, posy);
		        Point pt1 = new Point(posx - vitalSpaceWidth / 2, posy);
		        Point pt2 = new Point(posx, posy - vitalSpaceHeight);
		        Point pt3 = new Point(posx + vitalSpaceWidth / 2, posy);
		        Point pt4 = new Point(posx, posy + vitalSpaceHeight);

		        if (Utils.insidePolygon(polyTemp, pt1) && Utils.insidePolygon(polyTemp, pt2)
		                && Utils.insidePolygon(polyTemp, pt3) && Utils.insidePolygon(polyTemp, pt4)) {
		            rowCreated = true;
		            firstSeat = true;
		            coord.add(pt);
		        }

		        if (rowCreated && !(Utils.insidePolygon(polyTemp, pt1) && Utils.insidePolygon(polyTemp, pt2)
		                && Utils.insidePolygon(polyTemp, pt3) && Utils.insidePolygon(polyTemp, pt4))) {
		            Point Start = Utils.rotatePoint(coord.get(0), sceneCenter, -angle);
		            Point End = Utils.rotatePoint(coord.get(coord.size() - 1), sceneCenter, -angle);
		            double[] posStart = { Start.get_x(), Start.get_y() };
		            double[] posEnd = { End.get_x(), End.get_y() };
		            ImmutableSittingRow row = createSittingRow(sittingSection.getIdSection(), posStart, posEnd);
		            Core.get().createPlace(sittingSection.getIdSection() + "|" + row.getIdRow(), "#7289DA");

		            for (Point point : coord) {
		                Point rPoint = Utils.rotatePoint(point, sceneCenter, -angle);
		                double[] seatPos = { rPoint.get_x(), rPoint.get_y() };
		                ImmutableSeat seat = createSeat(sittingSection.getIdSection(), row.getIdRow(), seatPos);
		                Core.get().createPlace(
		                        sittingSection.getIdSection() + "|" + row.getIdRow() + "|" + seat.getId(),
		                        "#FFA500");
		            }
		            rowCreated = false;
                    coord.clear();
                }
            } while (posy > highestY);
        } while (posx > leftMostX);
    }

    public void setSittingSectionVitalSpace(String sectionId, double width, double height) {
        SittingSection sittingSection = this._sittingSections.get(sectionId);
        sittingSection.setVitalSpace(width, height);
        updateSectionPositions(sittingSection.getIdSection(), sittingSection.getPositions());
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