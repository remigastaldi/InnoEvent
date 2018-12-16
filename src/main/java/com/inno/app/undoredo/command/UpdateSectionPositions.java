/*
 * File Created: Saturday, 15th December 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 15th December 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Remi
 * <<licensetext>>
 */


package com.inno.app.undoredo.command;

import com.inno.app.room.Room;
import com.inno.service.Point;
import com.inno.service.Utils;
import com.inno.service.undoredo.Command;
import com.inno.ui.innoengine.InnoEngine;

public class UpdateSectionPositions implements Command {
  private InnoEngine _engine = null;
  private Room _room = null;
  private String _idSection = null;
  private double[] _positions = null;
  private boolean _rectangular = false;

  private double[] _oldPositions = null;

  public UpdateSectionPositions(InnoEngine engine,Room room, String idSection, double[] positions, boolean rectangular) {
    _engine = engine;
    _room = room;
    _idSection = idSection;
    _positions = positions.clone();
    _rectangular = rectangular;
  }

  public UpdateSectionPositions(InnoEngine engine,Room room, String idSection, double[] positions, boolean rectangular, double[] oldPos) {
    _engine = engine;
    _room = room;
    _idSection = idSection;
    _positions = positions.clone();
    _rectangular = rectangular;
    _oldPositions = oldPos.clone();
  }

  @Override
  public void execute() {
    _oldPositions = _room.getImmutableSectionById(_idSection).getPositions().clone();
    updateSectionPositions(_positions);
    _engine.updateSectionFromData(_idSection);
  }

  @Override
  public void unExecute() {
    if (_oldPositions != null) {
      updateSectionPositions(_oldPositions);
      _engine.updateSectionFromData(_idSection);
    }
  }

  public void updateSectionPositions(double pos[]) {
    if (_rectangular) {
      Point pt = new Point(_room.getImmutableScene().getCenter()[0],
          _room.getImmutableScene().getCenter()[1]);
      double rotation = Utils.calculateRectangleRotation(pt, pos);
      if (rotation != rotation)
        rotation = 0.0;
      _room.setSectionRotation(_idSection, rotation);
    }

    this._room.updateSectionPositions(_idSection, pos);
  }
}