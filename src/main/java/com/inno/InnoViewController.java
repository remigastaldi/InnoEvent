/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Friday, 12th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno;

import  com.inno.Core;

public abstract class InnoViewController {
  public Core InnoCore() {
    return Core.get();
  }
};