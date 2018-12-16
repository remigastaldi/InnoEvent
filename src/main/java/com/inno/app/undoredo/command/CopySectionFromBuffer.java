/*
 * File Created: Sunday, 16th December 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Sunday, 16th December 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Remi
 * <<licensetext>>
 */


package com.inno.app.undoredo.command;

import com.inno.app.room.Room;
import com.inno.app.room.Section;
import com.inno.service.pricing.Pricing;
import com.inno.service.undoredo.Command;
import com.inno.ui.innoengine.InnoEngine;

public class CopySectionFromBuffer implements Command {
  private InnoEngine _engine = null;
  private Room _room = null;
  private String _idSection = null;
  private Pricing _pricing = null;
  private Section _buffer = null;

  public CopySectionFromBuffer(InnoEngine engine, Room room, Pricing pricing) {
    _engine = engine;
    _room = room;
    _pricing = pricing;
  }

  @Override
  public void execute() {
    Section section = null;

    if (_buffer != null) {
      _room.setBuffer(_buffer);
    }
    try {
      section = (Section) ((Section) _room.createSectionFromBuffer());
      if (_buffer == null)
        _buffer = (Section) ((Section)section).clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    
    if (section == null)
      return;
      _idSection = section.getId();
      section.setNameSection("S-" + section.getId());
      
    double[] pos = section.getPositions();
    for (int i = 0; i < pos.length; ++i) {
      pos[i] += 1;
    }
    _room.updateSectionPositions(section.getId(), pos);
    if (section.isRectangle()) {
      _engine.createRectangularSection(section.getId()).select();
    } else if (section.isStanding()) {
      _engine.createIrregularSection(section.getId(), true).select();
    } else {
      _engine.createIrregularSection(section.getId(), false).select();
    }
  }

  @Override
  public void unExecute() {
    if (_idSection != null) {
      new DeleteSection(_engine, _room, _pricing, _idSection).execute();
    }
  }
}