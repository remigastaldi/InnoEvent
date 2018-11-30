/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Thursday, 29th November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.service;

import java.util.Vector;
import javafx.geometry.Point2D;

public class Utils {
    public static boolean insidePolygon(Point polygon[],Point p)
    {
        int n = polygon.length;
        int counter = 0;
        double xinters;

        Point p1 = polygon[0];
        for (int i=1;i<=n;i++) {
            Point p2 = polygon[i % n];
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
        double x = center.get_x() - distance(center, P);
        double y = center.get_y();
//        double Cos = ((xBis-a/X)+(Y/Math.pow(X,2))*(yBis-b))/(1-((Math.pow(Y,2))/Math.pow(X,2)));
//        double Sin = ((a-xBis)/Y+(X/Math.pow(Y,2))*(b-yBis))/(1-((Math.pow(X,2))/Math.pow(Y,2)));
//        double angleRadian = Math.atan2(Cos, Sin);

        double result = Math.atan2(y-center.get_y(), x-center.get_x())
                - Math.atan2(P.get_y()-center.get_y(),P.get_x()-center.get_x());

        return (result);
    }

    public static double calculateRotationAngle(Point center, Point oldP, Point newP)
    {
        return(Math.atan2(newP.get_y()-center.get_y(), newP.get_x()-center.get_x())
                - Math.atan2(oldP.get_y()-center.get_y(),oldP.get_x()-center.get_x()));
    }

    //Anti clockwise rotation of single Point 'p' around center Point 'center' with angle 'angle'
    public static Point rotatePoint(Point p, Point center, double angle) {

        if((p.get_x()==center.get_x())&&(p.get_y()==center.get_y()))
        {
            return p;
        }

        double xrot = Math.cos(angle) * (p.get_x() - center.get_x()) - Math.sin(angle) * (p.get_y() - center.get_y()) + center.get_x();
        double yrot = Math.sin(angle) * (p.get_x() - center.get_x()) + Math.cos(angle) * (p.get_y() - center.get_y()) + center.get_y();
        return new Point(xrot, yrot);
    }

    //Anti clockwise rotation of polygon around center Point 'center' with angle 'angle'
    public static Point[] rotatePolygon(Point polygon[], Point center, double angle)
    {
        int i =0;
        Point[] polygonRotated = new Point[polygon.length];
        for (Point point:polygon)
        {
            polygonRotated[i] = rotatePoint(point, center, angle);
            i++;
        }
        return polygonRotated;
    }

    //conversion from double[] to Point[]
    public static Point[] dArray_To_pArray(double[] d_Array)
    {
        Point[] p_Array = new Point[d_Array.length/2];
        for(int i =0; i<d_Array.length; i+=2 )
        {
            p_Array[i/2] = new Point(d_Array[i], d_Array[i+1]);
        }
        return p_Array;
    }

    //conversion from Point[] to double[]
    public static double[] pArray_To_dArray(Point[] p_Array)
    {
        double[] d_Array = new double[p_Array.length*2];
        for(int i=0; i<p_Array.length; i++)
        {
            d_Array[2*i] = p_Array[i].get_x();
            d_Array[(2*i)+1] = p_Array[i].get_y();
        }
        return d_Array;
    }

    public static double[] rotateRectangle(Point center, double[] rectangle)
    {
        System.out.println("PRINT_1");
        for(int i=0; i<rectangle.length; i+=2)
        {
            System.out.println("x: "+rectangle[i]+" y: "+rectangle[i+1]);
        }

        Point pRectangle[] = dArray_To_pArray(rectangle);

        System.out.println("PRINT_2");
        for(Point point:pRectangle)
        {
            System.out.println("x: "+point.get_x()+" y: "+point.get_y());
        }

        Point click = pRectangle[2];
        System.out.println("PRINT_3");
        System.out.println("x: "+click.get_x()+" y: "+click.get_y());

        double anglerot = calculateRectangleRotation(center, rectangle);
        Point pt[] = rotatePolygon((dArray_To_pArray(rectangle)), click, anglerot);
//        System.out.println("rotation: "+anglerot);
//        Point pt2[] = {pt[2], pt[3], pt[1], pt[0]};
//        return pArray_To_dArray(pt2);
        return pArray_To_dArray(pt);
    }

    public static Point findLeftmostPoint(Point[] points)
    {
        Point min = points[0];
        for(Point point: points)
        {
            if(point.get_x()<min.get_x())
            {
                min = point;
            }
        }
        return min;
    }

    public static Point findRightmostPoint(Point[] points)
    {
        Point max = points[0];
        for(Point point: points)
        {
            if(point.get_x()>max.get_x())
            {
                max = point;
            }
        }
        return max;
    }

    public static Point findLowestPoint(Point[] points)
    {
        Point min = points[0];
        for(Point point: points)
        {
            if(point.get_y()>min.get_y())
            {
                min = point;
            }
        }
        return min;
    }

    public static Point findHighestPoint(Point[] points)
    {
        Point max = points[0];
        for(Point point: points)
        {
            if(point.get_y()<max.get_y())
            {
                max = point;
            }
        }
        return max;
    }

    public static Point[] mirrorRectangle(Point[] rectangle)
    {
        System.out.println("PRINT_4");
        for(Point point:rectangle)
        {
            System.out.println("x: "+point.get_x()+" y: "+point.get_y());
        }

        Point mirroredRect[] = {new Point(rectangle[1].get_x(), 2*rectangle[2].get_y()-rectangle[1].get_y()),
                new Point(rectangle[0].get_x(), 2*rectangle[2].get_y()-rectangle[1].get_y()),rectangle[3],rectangle[2]};
//        return rectangle;

        System.out.println("PRINT_5");
        for(Point point:mirroredRect)
        {
            System.out.println("x: "+point.get_x()+" y: "+point.get_y());
        }

        return mirroredRect;
    }

    //calculate the angle needed to perform rotation of rectanglular section initiated at Point 'click' with width 'width'
    // to perpendicularly face scene center 'center'
    public static double calculateRectangleRotation(Point center, double[] rectangle)
    {
//        Point[] pRectangle = mirrorRectangle(dArray_To_pArray(rectangle));
        Point[] pRectangle = dArray_To_pArray(rectangle);

        Point click = pRectangle[0];
        double a = distance(pRectangle[0], pRectangle[1]);//width
        double b = distance(click, center);
        double cosA = (2*(b*b)-(a*a))/(2*b*b);

        Point p = rotatePoint(click, center, -Math.acos(cosA));
        Point q = new Point(click.get_x()+a, click.get_y());

        double rotation = Math.toDegrees(calculateRotationAngle(click, q, p));
        System.out.println("angle: "+rotation);
        return rotation;
    }
}