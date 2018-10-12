/*
 * File Created: Thursday, 27th September 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Friday, 12th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno;

import com.inno.view.startuppopup.main.StartupPopupMainView;

public class Main {
  public static void main(String[] args) {
    // System.out.println(new App().getGreeting());
    //MainView mainview = new MainView();
    StartupPopupMainView startUp = new StartupPopupMainView();

    startUp.run(args);
  }
}

