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


package com.inno.app.undoredo;

import com.inno.app.room.ImmutableSittingSection;
import com.inno.app.room.Room;
import com.inno.app.undoredo.command.CreateSittingSection;
import com.inno.app.undoredo.command.UpdateSectionPositions;
import com.inno.service.undoredo.UndoRedo;
import com.inno.ui.innoengine.InnoEngine;

public class UndoRedoHelper {
  private UndoRedo _undoRedo = new UndoRedo();
  private InnoEngine _engine;
  private Room _room;

  public UndoRedoHelper(InnoEngine engine, Room room) {
    _engine = engine;
    _room = room;
  }

  public void undo(int levels) {
    _undoRedo.undo(1);
  }

  public void redo(int levels) {
    _undoRedo.redo(1);
  }

  public void executeUpdateSectionPositions(String idSection, double[] positions, boolean rectangular) {
    double[] oldPositions = _room.getImmutableSectionById(idSection).getPositions().clone();
    UpdateSectionPositions command = new UpdateSectionPositions(_engine, _room, idSection, positions, rectangular, oldPositions);
    command.updatePositions(positions);

    _undoRedo.insert(command);
  }

  public ImmutableSittingSection createSittingSection(double[] positions, double rotation, boolean isRectangle) {
    CreateSittingSection command = new CreateSittingSection(_engine, _room, positions, rotation, isRectangle);
    ImmutableSittingSection section = command.createSectionInDomain();

    _undoRedo.insert(command);
    return section; 
  }
}