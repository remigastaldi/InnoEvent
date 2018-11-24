/*
 * File Created: Wednesday, 10th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 24th November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;;

public class Save<T> {
  private String _lastPath = null;

  public Save() {
  }

  public boolean save(T object) {
    if (_lastPath != null) {
      saveTo(object, _lastPath);
      return true;
    }
    else {
      System.out.println("Save path is not set");
      return false;
    }
  }

  public void saveTo(T object, String path) {
    if (_lastPath != path)
      _lastPath = path;

    try {
      System.out.println(path);
      FileOutputStream fos = new FileOutputStream(path);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(object);
      oos.close();      
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  @SuppressWarnings("unchecked")
  public T loadFrom(String path) {
    T object = null;
    try {
      FileInputStream fin = new FileInputStream(path);
      ObjectInputStream ois = new ObjectInputStream(fin);
      Object obj = ois.readObject();
      object = (T) obj;
      ois.close();
    } catch (Exception e) {
      System.out.println(e);
    }
    return object;
  }
};