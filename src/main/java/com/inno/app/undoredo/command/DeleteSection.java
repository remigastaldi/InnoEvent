/*
 * File Created: Saturday, 15th December 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Tuesday, 18th December 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Remi
 * <<licensetext>>
 */


package com.inno.app.undoredo.command;

import com.inno.app.Core;
import com.inno.app.room.ImmutableSittingSection;
import com.inno.app.room.Room;
import com.inno.app.room.Section;
import com.inno.service.pricing.Pricing;
import com.inno.service.undoredo.Command;

public class DeleteSection implements Command {
  private Room _room = null;
  private String _idSection = null;
  private Pricing _pricing = null;

  private ImmutableSittingSection _section = null;

  public DeleteSection(Room room, Pricing pricing, String idSection) {
    _room = room;
    _pricing = pricing;
    _idSection = idSection;
  }

  @Override
  public void execute() {
    deleteSection();
    Core.get().deleteUiSection(_idSection);
  }

  @Override
  public void unExecute() {
    if (_section != null) {
      // TODO; Standing section
      // new CreateSittingSection(_engine, _room, _pricing, _section.getPositions(), _section.getRotation(), _section.isRectangle()).execute();
      CreateSittingSection command =  new CreateSittingSection(_room, _pricing,
        _section.getPositions(), _section.getRotation(), _section.isRectangle());
      _section = command.createSectionInDomain();

      _idSection = _section.getId();
      command.createSectionInEngine(_section);
    }
  }

  public void deleteSection() {
    try {
      Section section = _room.getSectionById(_idSection);
      if (section == null)
        return;

      _section =  (ImmutableSittingSection) section.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }

    Core.get().deleteSectionPrices(_idSection);
    _room.deleteSection(_idSection);
  }
}