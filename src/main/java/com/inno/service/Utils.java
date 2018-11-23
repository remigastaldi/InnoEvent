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
    public static boolean InsidePolygon(Point polygon[],int N,Point p)
    {
        int counter = 0;
        int i;
        double xinters;
        Point p1,p2;

        p1 = polygon[0];
        for (i=1;i<=N;i++) {
            p2 = polygon[i % N];
            if (p.get_y() > Math.min(p1.get_y(),p2.get_y())) {
                if (p.get_y() <= Math.max(p1.get_y(), p2.get_y())) {
                    if (p.get_x() <= Math.max(p1.get_x(),p2.get_x())) {
                        if (p1.get_y() != p2.get_y()) {
                            xinters = (p.get_y()-p1.get_y())*(p2.get_x()-p1.get_x())/(p2.get_y()-p1.get_y())+p1.get_x();
                            if (p1.get_x() == p2.get_x() || p.get_x() <= xinters)
                                counter++;
                        }
                    }
                }
            }
            p1 = p2;
        }

        return (counter % 2 != 0);
    }

    public static Point lineLineIntersection(Point A, Point B, Point C, Point D) {
        // Line AB
        double a1 = B.get_y() - A.get_y();
        double b1 = A.get_x() - B.get_x();
        double c1 = a1 * (A.get_x()) + b1 * (A.get_y());

        // Line CD
        double a2 = D.get_y() - C.get_y();
        double b2 = C.get_x() - D.get_x();
        double c2 = a2 * (C.get_x()) + b2 * (C.get_y());

        double determinant = a1 * b2 - a2 * b1;

        if (determinant == 0) {
            // The lines are parallel
            return new Point(Double.MAX_VALUE, Double.MAX_VALUE);
        } else {
            double x = (b2 * c1 - b1 * c2) / determinant;
            double y = (a1 * c2 - a2 * c1) / determinant;
            return new Point(x, y);
        }
    }

    public static Point centroid(Point polygon[])  {
        double centroidX = 0, centroidY = 0;

        for(Point point : polygon) {
            centroidX += point.get_x();
            centroidY += point.get_y();
        }
        return new Point(centroidX / polygon.length, centroidY / polygon.length);
    }

    public static double distance(Point A, Point B)
    {
        return(Math.sqrt(Math.pow(A.get_x()-B.get_x(),2) + Math.pow(A.get_y()-B.get_y(),2)));
    }

    public static double calculateLeftSideRotationAngle(Point center, Point P)
    {
//        double a = center.get_x();
//        double b = center.get_y();
//        double x = P.get_x();
//        double y = P.get_y();
//        double X = x-a;
//        double Y = y-b;
        double xBis = center.get_x() - distance(center, P);
        double yBis = center.get_y();
//        double Cos = ((xBis-a/X)+(Y/Math.pow(X,2))*(yBis-b))/(1-((Math.pow(Y,2))/Math.pow(X,2)));
//        double Sin = ((a-xBis)/Y+(X/Math.pow(Y,2))*(b-yBis))/(1-((Math.pow(X,2))/Math.pow(Y,2)));
//        double angleRadian = Math.atan2(Cos, Sin);

        double result = Math.atan2(yBis-center.get_y(), xBis-center.get_x())
                - Math.atan2(P.get_y()-center.get_y(),P.get_x()-center.get_x());

        return (result);
    }

    public static double calculateRotationAngle(Point center, Point oldP, Point newP)
    {
        return(Math.atan2(newP.get_y()-center.get_y(), newP.get_x()-center.get_x())
                - Math.atan2(oldP.get_y()-center.get_y(),oldP.get_x()-center.get_x()));
    }

    public static Point rotatePoint(Point p, Point o, double angle) {
        double xrot = Math.cos(angle) * (p.get_x() - o.get_x()) - Math.sin(angle) * (p.get_y() - o.get_y()) + o.get_x();
        double yrot = Math.sin(angle) * (p.get_x() - o.get_x()) + Math.cos(angle) * (p.get_y() - o.get_y()) + o.get_y();
        return new Point(xrot, yrot);
    }

    public static Point[] rotatePolygon(Point polygon[], Point centre, double angle)
    {
        int i =0;
        Point[] polygonRotated = new Point[polygon.length];
        for (Point point:polygon)
        {
            polygonRotated[i] = rotatePoint(point, centre, angle);
            i++;
        }
        return polygonRotated;
    }

    public static Point[] dArray_To_pArray(double[] d_Array)
    {
        Point[] p_Array = new Point[d_Array.length/2];
        for(int i =0; i<d_Array.length; i+=2 )
        {
            p_Array[i/2] = new Point(d_Array[i], d_Array[i+1]);
        }
        return p_Array;
    }

//    public static Point[] rotateRectanglePerpendicularly(Point polygon[], Point centre, double angle)
//    {
//
//        for(Point point:polygon)
//        {
//
//        }
//        double
//    }

}