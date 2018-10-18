/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 13th October 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.service;

import java.util.Vector;
import javafx.geometry.Point2D;

public class Utils {
    public Point2D getCenterOfPoints(Vector<Point2D> points) {
        double sum1 = 0;
        double sum2 = 0;
        double sum3 = 0;

        for (int i = 0; i < points.size(); i++) {
            Point2D point1 = points.get(i);
            Point2D point2;
            if (i + 1 == points.size()) {
                point2 = points.get(0);
            } else {
                point2 = points.get(i + 1);
            }
            double val1 = ((point1.getX() * point2.getY()) - (point2.getX() * point1.getY()));
            double val2 = (val1 * (point1.getX() + point2.getX()));
            double val3 = (val1 * (point1.getY() + point2.getY()));
            sum1 += val1;
            sum2 += val2;
            sum3 += val3;
        }

        double air = (sum1 / 2);
        return new Point2D((sum2 / (6 * air)), (sum3 / (6 * air)));
    }

}