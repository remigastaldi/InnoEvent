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
import com.inno.app.room.ImmutableStandingSection;
import com.inno.app.room.Room;
import com.inno.service.undoredo.Command;

public class CreateStandingSection implements Command {
  private Room _room = null;
  private ImmutableStandingSection _section = null;
  private int _nbPeople = 0;
  private double[] _positions = null;
  private  double _rotation;

  public CreateStandingSection(Room room, int nbPeople, double[] positions, double rotation) {
    _room = room;
    _nbPeople = nbPeople;
    _positions = positions;
    _rotation = rotation;
  }

  @Override
  public void execute() {
    _section = _room.createStandingSection(_nbPeople, _positions, _rotation);
    Core.get().createPlace(_section.getId(), "#6378bf");
    // Core.get().createStandingSection(_nbPeople, _positions, _rotation);
  }

  @Override
  public void unExecute() {
    Core.get().deleteSection(_section.getId());
  }
}