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


package com.inno.app.undoredo;

import com.inno.app.room.ImmutableSittingSection;
import com.inno.app.room.Room;
import com.inno.app.undoredo.command.CopySectionFromBuffer;
import com.inno.app.undoredo.command.CreateSittingSection;
import com.inno.app.undoredo.command.DeleteSection;
import com.inno.app.undoredo.command.UpdateSectionPositions;
import com.inno.service.pricing.Pricing;
import com.inno.service.undoredo.UndoRedo;

public class UndoRedoHelper {
  private UndoRedo _undoRedo = new UndoRedo();
  private Room _room = null;
  private Pricing _pricing = null;
  private long _actionsNumber = 0;
  private long _lastChangedCheck = 0;

  public UndoRedoHelper(Room room, Pricing pricing) {
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
    UpdateSectionPositions command = new UpdateSectionPositions(_room, idSection, positions, rectangular, oldPositions);
    command.updateSectionPositions(positions);

    _undoRedo.insert(command);

    _actionsNumber++;
  }

  public ImmutableSittingSection createSittingSection(double[] positions, double rotation, boolean isRectangle) {
    CreateSittingSection command = new CreateSittingSection(_room, _pricing, positions, rotation, isRectangle);
    ImmutableSittingSection section = command.createSectionInDomain();

    _undoRedo.insert(command);
    _actionsNumber++;
    return section;
  }

  public void deleteSection(String idSection) {
    if (_room.getSectionById(idSection) == null)
      return;
    DeleteSection command = new DeleteSection(_room, _pricing, idSection);

    command.deleteSection();
    _undoRedo.insert(command);
    _actionsNumber++;
  }

  public void createSectionFromBuffer() {
    CopySectionFromBuffer command = new CopySectionFromBuffer(_room, _pricing);

    command.execute();
    _undoRedo.insert(command);
    _actionsNumber++;
  }

  public boolean hasChanged() {
    boolean hasChanged = false;

    if (_actionsNumber > _lastChangedCheck) {
      hasChanged = true;
      _lastChangedCheck = _actionsNumber;
    }
    return hasChanged;
  }
}