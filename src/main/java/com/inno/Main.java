/*
 * File Created: Thursday, 27th September 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Thursday, 4th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno;

import com.inno.mainview.MainView;
import com.inno.startuppopup.StartupPopUpView;

public class Main {

  public static void main(String[] args) {
    // System.out.println(new App().getGreeting());
    //MainView mainview = new MainView();
    StartupPopUpView startUp = new StartupPopUpView();

    startUp.run(args);
  }
}

