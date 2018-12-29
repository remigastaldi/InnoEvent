/*
 * File Created: Saturday, 15th December 2018
 * Author: GASTALDI R??mi
 * -----
 * Last Modified: Saturday, 15th December 2018
 * Modified By: GASTALDI R??mi
 * -----
 * Copyright - 2018 GASTALDI Remi
 * <<licensetext>>
 */

package com.inno.service.undoredo;

public interface Command {
  void execute();

  void unExecute();
}