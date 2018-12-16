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

package com.inno.service.undoredo;

import java.util.Stack;


public class UndoRedo {
  static final int FIXED_SIZE = 5000;
  private Stack<Command> _undoCommands = new Stack<Command>(){
    private static final long serialVersionUID = 1L;
    public Command push(Command item) {
        if (this.size() == FIXED_SIZE) {
            this.removeElementAt(0);
        }
        return super.push(item);
    }
  };
  private Stack<Command> _redoCommands = new Stack<Command>(){
    private static final long serialVersionUID = 1L;
    public Command push(Command item) {
        if (this.size() == FIXED_SIZE) {
            this.removeElementAt(0);
        }
        return super.push(item);
    }
  };

  public void redo(int levels) {
    for (int i = 1; i <= levels; i++) {
      if (_redoCommands.size() != 0) {
        Command command = _redoCommands.pop();
        command.execute();
        _undoCommands.push(command);
      }
    }
  }

  public void undo(int levels) {
    for (int i = 1; i <= levels; i++) {
      if (_undoCommands.size() != 0) {
        Command command = _undoCommands.pop();
        command.unExecute();
        _redoCommands.push(command);
      }
    }
  }

  public void insert(Command command) {
    _undoCommands.push(command);
    _redoCommands.clear();
  }
}