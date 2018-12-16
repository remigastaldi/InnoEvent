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
import com.inno.app.room.ImmutableStandingSection;
import com.inno.app.room.Room;
import com.inno.service.undoredo.Command;
import com.inno.ui.innoengine.InnoEngine;

public class CreateStandingSection implements Command {
  private InnoEngine _engine = null;
  private Room _room = null;
  private ImmutableStandingSection _section = null;
  private int _nbPeople = 0;
  private double[] _positions = null;
  private  double _rotation;

  public CreateStandingSection(InnoEngine engine, Room room, int nbPeople, double[] positions, double rotation) {
    _engine = engine;
    _room = room;
    _nbPeople = nbPeople;
    _positions = positions;
    _rotation = rotation;
  }

  @Override
  public void execute() {
    _section = _room.createStandingSection(_nbPeople, _positions, _rotation);
    Core.get().createPlace(_section.getId(), "#6378bf");
    Core.get().createStandingSection(_nbPeople, _positions, _rotation);
    
    // _engine.crea
  }

  @Override
  public void unExecute() {
    Core.get().deleteSection(_section.getId());
  }
}