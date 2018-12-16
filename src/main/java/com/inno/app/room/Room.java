/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Sunday, 16th December 2018
 * Modified By: GASTALDI Rémi

 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.app.room;

import com.inno.app.Core;
import com.inno.service.IdHandler;
import com.inno.service.Point;
import com.inno.service.Utils;
import com.inno.service.pricing.ImmutablePlaceRate;

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
    public enum AttributionType {SEAT, ROW, SECTION};
    private ImmutableSittingSection _bufferedSittingSection = null;
    private ImmutableStandingSection _bufferedStandingSection = null;
    private IdHandler _idHandler = null;
    
    public Room(String name, double width, double height, double widthVitalSpace, double heightVitalSpace) {
        this._name = name;
        this._width = width;
        this._height = height;
        this._vitalSpace = new VitalSpace(widthVitalSpace, heightVitalSpace);
        _idHandler = new IdHandler();
    }

    // Room Methods
    public void setBuffer(Section buffer) {
        if (buffer.isStanding())
            _bufferedStandingSection = (ImmutableStandingSection) buffer;
        else
            _bufferedSittingSection = (ImmutableSittingSection) buffer;
    }

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

    public String getUniqueSectionId() {
        return _idHandler.getUniqueId();
    }

    public void setVitalSpace(double width, double height) {
        for (SittingSection section : _sittingSections.values()) {
            if (section.getImmutableVitalSpace().getWidth() == _vitalSpace.getWidth()
                    && section.getImmutableVitalSpace().getHeight() == _vitalSpace.getHeight()) {
                section.setVitalSpace(width, height);
                Core.get().updateSectionPositions(section.getId(), section.getPositions(),
                        section.isRectangle()); // Just to recalculate seats positions
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
        String id = null;

        oldSection = this._sittingSections.get(idSection);
        newSection = this.createStandingSection(0, oldSection.getPositions(), oldSection.getRotation());
        this.getSectionById(newSection.getId()).setNameSection(oldSection.getNameSection());
        this.getSectionById(newSection.getId()).setElevation(oldSection.getElevation());
        deleteSection(idSection);
        id = newSection.getId();
        // this.getSectionById(newSection.getId()).setIdSection(idSection);
        StandingSection obj = this._standingSections.remove(id);
        this._standingSections.put(newSection.getId(), obj);
        return newSection;
    }

    public ImmutableSittingSection standingToSittingSection(String idSection) {
        ImmutableStandingSection oldSection = null;
        ImmutableSittingSection newSection = null;
        String id = null;

        oldSection = this._standingSections.get(idSection);
        newSection = this.createSittingSection(oldSection.getPositions(), oldSection.getRotation(), false);
        this.getSectionById(newSection.getId()).setNameSection(oldSection.getNameSection());
        this.getSectionById(newSection.getId()).setElevation(oldSection.getElevation());
        deleteSection(idSection);
        id = newSection.getId();
        // this.getSectionById(newSection.getId()).setIdSection(idSection);
        SittingSection obj = this._sittingSections.remove(id);
        this._sittingSections.put(newSection.getId(), obj);
        return newSection;
    }

    public void copySectionToBuffer(String id) {
        try {
            ImmutableSittingSection sittingSection = this._sittingSections.get(id);
            ImmutableStandingSection standingSection =  this._standingSections.get(id);
    
            if (sittingSection != null) {
                _bufferedSittingSection = (ImmutableSittingSection) sittingSection.clone();
            }
            else if (standingSection != null) {
                _bufferedStandingSection = (ImmutableStandingSection) standingSection.clone();
            }
        } catch (CloneNotSupportedException e) {
            System.err.println(e);
        }
    }

    public Section createSectionFromBuffer() {
        Section section = null;
        
        try {
            if (_bufferedSittingSection != null) {
                String id = _idHandler.getUniqueId();
                section = (Section) _bufferedSittingSection.clone();
                section.setId(id);
                section.setNameSection("S-" + id);
                this._sittingSections.put(id, (SittingSection) section);
            } else if (_bufferedStandingSection != null) {
                String id = _idHandler.getUniqueId();
                section = (Section) _bufferedStandingSection.clone();
                section.setId(id);
                section.setNameSection("S-" + id);
                this._standingSections.put(id, (StandingSection) section);
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        if (section != null) {
            Core.get().createPlace(section.getId(), "#6378bf");
        }
        return section;
    }

    public ImmutableSection duplicateSection(String idSection) {
        ImmutableSection oldSection = getImmutableSectionById(idSection);
        ImmutableSection newSection = null;
        String id = _idHandler.getUniqueId();
        
        try {
            newSection = (ImmutableSection) oldSection.clone();
            if (this._sittingSections.get(idSection) != null) {
                this._sittingSections.put(id, (SittingSection) newSection);
                this._sittingSections.get(id).setId(id);
                this._sittingSections.get(id).setNameSection("S-" + id);
            } else if (this._standingSections.get(idSection) != null) {
                this._standingSections.put(id, (StandingSection) newSection);
                this._standingSections.get(id).setId(id);
                this._sittingSections.get(id).setNameSection("S-" + id);
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
            sittingSection.setId(newId);
        } else if ((standingSection = this._standingSections.get(idSection)) != null) {
            standingSection.setId(newId);
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
            clearAllSittingRows(idSection);
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

        section.setRotation((section.getRotation() - section.getUserRotation()) + rotation);
        section.setUserRotation(rotation);
    }

    public void deleteSection(String idSection) {
        this._sittingSections.remove(idSection);
        this._standingSections.remove(idSection);
        _idHandler.releaseId(idSection);
    }

    // standingSection Methods
    public ImmutableStandingSection createStandingSection(int nbPeople, double[] positions, double rotation) {
        String id = _idHandler.getUniqueId();
        StandingSection standingSection = new StandingSection("S-" + id, id, positions, nbPeople, rotation);
        this._standingSections.put(id, standingSection);
        return standingSection;
    }

    public void setStandingNbPeople(String idSection, int nbPeople) {
        StandingSection standingSection = this._standingSections.get(idSection);
        standingSection.setNbPeople(nbPeople);
    }

    // sittingSection Methods
    public ImmutableSittingSection createSittingSection(double[] positions, double rotation, boolean isRectangle) {
        System.out.println("========" + _idHandler);
        // _idHandler = new IdHandler();
        String id = _idHandler.getUniqueId();
        double vitalSpaceWidth = this.getImmutableVitalSpace().getWidth();
        double vitalSpaceHeight = this.getImmutableVitalSpace().getHeight();
        SittingSection sittingSection = new SittingSection("S-" + id, id, positions, rotation, vitalSpaceWidth,
                vitalSpaceHeight, isRectangle);
        this._sittingSections.put(id, sittingSection);

        Core.get().createPlace(sittingSection.getId(), "#6378bf");

        if (isRectangle) {
            updateRectangleRows(positions, sittingSection);
        } else {
            updatePolygonRows(positions, sittingSection);
        }

//        Core.get().setAutomaticPrices(50, 200, 1000, Core.AttributionType.SEAT);
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
            double[] posEnd = { positions[0] + (int) ((positions[2] - positions[0]) / vitalSpaceWidth) * vitalSpaceWidth
                    - vitalSpaceWidth / 2, yRow + (vitalSpaceHeight / 2) };
            ImmutableSittingRow row = createSittingRow(section.getId(), posStart, posEnd);

            ImmutablePlaceRate sectionPlace = Core.get().getSectionPrice(section.getId());
            if (sectionPlace != null && sectionPlace.getPrice() != -1) {
                Core.get().createPlace(section.getId() + "|" + row.getIdRow(), sectionPlace.getColor(),
                        sectionPlace.getPrice());
            } else {
                Core.get().createPlace(section.getId() + "|" + row.getIdRow(), "#7289DA");
            }

            while (xSeat < positions[2] - vitalSpaceWidth * 0.99) {
                double[] seatPos = { xSeat + (vitalSpaceWidth / 2), ySeat + (vitalSpaceHeight / 2) };
                ImmutableSeat seat = createSeat(section.getId(), row.getIdRow(), seatPos);

                ImmutablePlaceRate rowPlace = Core.get().getRowPrice(section.getId(), row.getIdRow());
                if (rowPlace != null && rowPlace.getPrice() != -1) {
                    Core.get().createPlace(section.getId() + "|" + row.getIdRow() + "|" + seat.getId(),
                            rowPlace.getColor(), rowPlace.getPrice());
                } else {
                    Core.get().createPlace(section.getId() + "|" + row.getIdRow() + "|" + seat.getId(),
                            "#FFA500");
                }
                xSeat += vitalSpaceWidth;
            }

            yRow += vitalSpaceHeight;
            xSeat = positions[0];
            ySeat += vitalSpaceHeight;
        }
    }

    private void updatePolygonRows(double[] positions, SittingSection sittingSection) {
        // TODO: Why width and height are inverted
        double vitalSpaceHeight = sittingSection.getImmutableVitalSpace().getHeight();
        double vitalSpaceWidth = sittingSection.getImmutableVitalSpace().getWidth();
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
		        posx -= vitalSpaceHeight/10;
		    }
		    if (firstSeat) {
		        posx -= vitalSpaceHeight;
		    }

            double posy = lowestY + vitalSpaceWidth;

		    do {
		        if (!rowCreated) {
		            posy -= vitalSpaceWidth/10;
		        }
		        if (rowCreated) {
		            posy -= vitalSpaceWidth;
		        }

                Point pt = new Point(posx, posy);
                Point pt1 = new Point(posx - vitalSpaceHeight / 2, posy);
                Point pt2 = new Point(posx, posy - vitalSpaceWidth / 2);
                Point pt3 = new Point(posx + vitalSpaceHeight / 2, posy);
                Point pt4 = new Point(posx, posy + vitalSpaceWidth / 2);
                Point pt5 = new Point(posx - vitalSpaceHeight / 2,posy + vitalSpaceWidth / 2 );
                Point pt6 = new Point(posx + vitalSpaceHeight / 2,posy + vitalSpaceWidth / 2 );
                Point pt7 = new Point(posx - vitalSpaceHeight / 2,posy - vitalSpaceWidth / 2 );
                Point pt8 = new Point(posx + vitalSpaceHeight / 2,posy - vitalSpaceWidth / 2 );

                if (Utils.insidePolygon(polyTemp, pt1) && Utils.insidePolygon(polyTemp, pt2)
                        && Utils.insidePolygon(polyTemp, pt3) && Utils.insidePolygon(polyTemp, pt4)
                        && Utils.insidePolygon(polyTemp, pt5) && Utils.insidePolygon(polyTemp, pt6)
                        && Utils.insidePolygon(polyTemp, pt7) && Utils.insidePolygon(polyTemp, pt8)) {
                    rowCreated = true;
                    firstSeat = true;
                    coord.add(pt);
                }

                if (rowCreated && !(Utils.insidePolygon(polyTemp, pt1) && Utils.insidePolygon(polyTemp, pt2)
                        && Utils.insidePolygon(polyTemp, pt3) && Utils.insidePolygon(polyTemp, pt4)
                        && Utils.insidePolygon(polyTemp, pt5) && Utils.insidePolygon(polyTemp, pt6)
                        && Utils.insidePolygon(polyTemp, pt7) && Utils.insidePolygon(polyTemp, pt8))) {
                    Point Start = Utils.rotatePoint(coord.get(0), sceneCenter, -angle);
                    Point End = Utils.rotatePoint(coord.get(coord.size() - 1), sceneCenter, -angle);
                    double[] posStart = { Start.get_x(), Start.get_y() };
                    double[] posEnd = { End.get_x(), End.get_y() };
                    ImmutableSittingRow row = createSittingRow(sittingSection.getId(), posStart, posEnd);
                    
                    Core.get().createPlace(sittingSection.getId() + "|" + row.getIdRow(), "#7289DA");

                    for (Point point : coord) {
                        Point rPoint = Utils.rotatePoint(point, sceneCenter, -angle);
                        double[] seatPos = { rPoint.get_x(), rPoint.get_y() };
                        ImmutableSeat seat = createSeat(sittingSection.getId(), row.getIdRow(), seatPos);
                        Core.get().createPlace(
                                sittingSection.getId() + "|" + row.getIdRow() + "|" + seat.getId(), "#FFA500");
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
        updateSectionPositions(sittingSection.getId(), sittingSection.getPositions());
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