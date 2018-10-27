/*
 * File Created: Saturday, 27th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 27th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui;

import  com.inno.app.Core;

public abstract class ViewController {
  public Core InnoCore() {
    return Core.get();
  }
};
