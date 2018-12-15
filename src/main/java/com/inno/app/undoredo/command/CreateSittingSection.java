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

import com.inno.app.Core;
import com.inno.app.room.ImmutableSittingSection;
import com.inno.app.room.Room;
import com.inno.service.Point;
import com.inno.service.Utils;
import com.inno.service.undoredo.Command;
import com.inno.ui.innoengine.InnoEngine;

public class CreateSittingSection implements Command {
  private InnoEngine _engine = null;
  private Room _room = null;
  private double[] _positions = null;
  private double _rotation = 0d;
  private boolean _isRectangle = false;
  private String _id = null;

  public CreateSittingSection(InnoEngine engine, Room room, double[] positions, double rotation, boolean isRectangle) {
    _engine = engine;
    _room = room;
    _positions = positions.clone();
    _rotation = rotation;
    _isRectangle = isRectangle;
  }

  public CreateSittingSection(InnoEngine engine, Room room, double[] positions, double rotation, boolean isRectangle, String id) {
    _engine = engine;
    _room = room;
    _positions = positions.clone();
    _rotation = rotation;
    _isRectangle = isRectangle;
    _id = id;
  }
  
  @Override
  public void execute() {
    double newRotation = 0d;
    if (_isRectangle) {
      Point pt = new Point(Core.get().getImmutableRoom().getImmutableScene().getCenter()[0],
        Core.get().getImmutableRoom().getImmutableScene().getCenter()[1]);
      newRotation = Utils.calculateRectangleRotation(pt, _positions);
    }

    ImmutableSittingSection section = _room.createSittingSection(_positions, newRotation, _isRectangle);
    
    _id = section.getId();
    if (section.isRectangle())
      _engine.createRectangularSection(_id);
    else
      _engine.createIrregularSection(_id, false);
  }

  @Override
  public void unExecute() {
    if (_id != null) {
      _engine.deleteSittingSection(_id);
      Core.get().deleteSection(_id);
    }
  }
}