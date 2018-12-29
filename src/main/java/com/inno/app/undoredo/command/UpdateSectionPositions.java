/*
 * File Created: Saturday, 15th December 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Tuesday, 18th December 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Remi
 * <<licensetext>>
 */


package com.inno.app.undoredo.command;

import com.inno.app.Core;
import com.inno.app.room.ImmutableSittingSection;
import com.inno.app.room.Room;
import com.inno.app.room.Section;
import com.inno.app.room.SittingSection;
import com.inno.service.Point;
import com.inno.service.Utils;
import com.inno.service.undoredo.Command;

public class UpdateSectionPositions implements Command {
  private Room _room = null;
  private String _idSection = null;
  private double[] _positions = null;
  private boolean _rectangular = false;
  private boolean _autoDistrib = true;

  private double[] _oldPositions = null;

  public UpdateSectionPositions(Room room, String idSection, double[] positions, boolean rectangular) {
    _room = room;
    _idSection = idSection;
    _positions = positions.clone();
    _rectangular = rectangular;
  }

  public UpdateSectionPositions(Room room, String idSection, double[] positions, boolean rectangular, double[] oldPos) {
    _room = room;
    _idSection = idSection;
    _positions = positions.clone();
    _rectangular = rectangular;
    _oldPositions = oldPos.clone();
  }

  @Override
  public void execute() {
    Section section = (Section) _room.getSectionById(_idSection);
    _oldPositions = section.getPositions().clone();
    if (!section.isStanding())
      _autoDistrib = ((SittingSection)section).getAutoDistribution();
    updateSectionPositions(_positions);
    Core.get().updateSectionFromData(_idSection);
  }

  @Override
  public void unExecute() {
    Section section = (Section) _room.getSectionById(_idSection);
    if (_oldPositions != null) {
      if (!section.isStanding())
        ((SittingSection)section).setAutoDistribution(_autoDistrib);
      updateSectionPositions(_oldPositions);
      Core.get().updateSectionFromData(_idSection);
    }
  }

  public void updateSectionPositions(double pos[]) {
    ImmutableSittingSection section;
    if (_rectangular) {
      Point pt = new Point(_room.getImmutableScene().getCenter()[0],
          _room.getImmutableScene().getCenter()[1]);
      if (((section = _room.getImmutableSittingSections().get(_idSection)) == null) || ((ImmutableSittingSection) section).getAutoDistribution()) {
        double rotation = Utils.calculateRectangleRotation(pt, pos);
        if (rotation != rotation)
          rotation = 0.0;
        _room.setSectionRotation(_idSection, rotation);
      }
    } 

    this._room.updateSectionPositions(_idSection, pos);
  }
}