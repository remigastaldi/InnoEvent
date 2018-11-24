/*
 * File Created: Friday, 26th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 24th November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.app;

import com.inno.app.room.ImmutableRoom;
import com.inno.service.Save;

public class InnoSave {
  Save<SaveObject> _save = new Save<>();

  public InnoSave() {
  }

  public void save(SaveObject save) {
    _save.save(save);
  }

  public void saveTo(SaveObject save, String path) {
    _save.saveTo(save, path);
  }

  public SaveObject loadFrom(String path) {
    return _save.loadFrom(path);
  }
}