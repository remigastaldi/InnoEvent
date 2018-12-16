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


package com.inno.service;

import java.util.Stack;

public class IdHandler {
  private Stack<String> _freeId = new Stack<>();
  private Integer _currentMax = 0;

  public IdHandler() {
  }

  public void releaseId(String id) {
    _freeId.push(id);
  }

  public String getUniqueId() {
    if (_freeId.size() > 0)
      return _freeId.pop();
    else {
      _currentMax++;
      return _currentMax.toString();
    }
  }
}