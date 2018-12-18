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
import com.inno.service.Point;
import com.inno.service.Utils;
import com.inno.service.pricing.Pricing;
import com.inno.service.undoredo.Command;

public class CreateSittingSection implements Command {
  private Room _room = null;
  private double[] _positions = null;
  private double _rotation = 0d;
  private boolean _isRectangle = false;
  private String _id = null;
  private Pricing _pricing = null;

  public CreateSittingSection(Room room, Pricing pricing, double[] positions, double rotation, boolean isRectangle) {
    _room = room;
    _pricing = pricing;
    _positions = positions.clone();
    _rotation = rotation;
    _isRectangle = isRectangle;
  }

  // public CreateSittingSection(InnoEngine engine, Room room, double[] positions, double rotation, boolean isRectangle, String id) {
  //   _engine = engine;
  //   _room = room;
  //   _positions = positions.clone();
  //   _rotation = rotation;
  //   _isRectangle = isRectangle;
  //   _id = id;
  // }
  
  @Override
  public void execute() {
    ImmutableSittingSection section = createSectionInDomain();
    createSectionInEngine(section);
  }

  @Override 
  public void unExecute() {
    if (_id != null) {
      new DeleteSection(_room, _pricing, _id).execute();
    }
  }

  public ImmutableSittingSection createSectionInDomain() {
    double newRotation = 0d;
    if (_isRectangle) {
      Point pt = new Point(Core.get().getImmutableRoom().getImmutableScene().getCenter()[0],
        Core.get().getImmutableRoom().getImmutableScene().getCenter()[1]);
      newRotation = Utils.calculateRectangleRotation(pt, _positions);
    }

    ImmutableSittingSection section = _room.createSittingSection(_positions, newRotation, _isRectangle);
    
    _id = section.getId();

    return section;
  }

  public void createSectionInEngine(ImmutableSittingSection section) {
    if (section.isRectangle())
      Core.get().createRectangularSection(_id);
    else
      Core.get().createIrregularSection(_id, false);
  }
}