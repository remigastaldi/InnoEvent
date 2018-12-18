/*
 * File Created: Saturday, 15th December 2018
 * Author: GASTALDI R??mi
 * -----
 * Last Modified: Sunday, 16th December 2018
 * Modified By: GASTALDI R??mi
 * -----
 * Copyright - 2018 GASTALDI Remi
 * <<licensetext>>
 */


package com.inno.service;

import java.io.Serializable;
import java.util.Stack;

public class IdHandler implements Serializable {
  private static final long serialVersionUID = 1L;
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