/*
 * File Created: Saturday, 15th December 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Sunday, 16th December 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Remi
 * <<licensetext>>
 */


package com.inno.app.undoredo;

import com.inno.app.room.ImmutableSittingSection;
import com.inno.app.room.Room;
import com.inno.app.undoredo.command.CopySectionFromBuffer;
import com.inno.app.undoredo.command.CreateSittingSection;
import com.inno.app.undoredo.command.DeleteSection;
import com.inno.app.undoredo.command.UpdateSectionPositions;
import com.inno.service.pricing.Pricing;
import com.inno.service.undoredo.UndoRedo;
import com.inno.ui.innoengine.InnoEngine;

public class UndoRedoHelper {
  private UndoRedo _undoRedo = new UndoRedo();
  private InnoEngine _engine;
  private Room _room;
  private Pricing _pricing;

  public UndoRedoHelper(InnoEngine engine, Room room, Pricing pricing) {
    _engine = engine;
    _room = room;
    _pricing = pricing;
  }

  public void undo(int levels) {
    _undoRedo.undo(1);
  }

  public void redo(int levels) {
    _undoRedo.redo(1);
  }

  public void updateSectionPositions(String idSection, double[] positions, boolean rectangular) {
    double[] oldPositions = _room.getImmutableSectionById(idSection).getPositions().clone();
    UpdateSectionPositions command = new UpdateSectionPositions(_engine, _room, idSection, positions, rectangular, oldPositions);
    command.updateSectionPositions(positions);

    _undoRedo.insert(command);
  }

  public ImmutableSittingSection createSittingSection(double[] positions, double rotation, boolean isRectangle) {
    CreateSittingSection command = new CreateSittingSection(_engine, _room, _pricing, positions, rotation, isRectangle);
    ImmutableSittingSection section = command.createSectionInDomain();

    _undoRedo.insert(command);
    return section;
  }

  public void deleteSection(String idSection) {
    if (_room.getSectionById(idSection) == null)
      return;
    DeleteSection command = new DeleteSection(_engine, _room, _pricing, idSection);

    command.deleteSection();
    _undoRedo.insert(command);
  }

  public void createSectionFromBuffer() {
    CopySectionFromBuffer command = new CopySectionFromBuffer(_engine, _room, _pricing);

    command.execute();
    _undoRedo.insert(command);
  }
}