package com.inno.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import com.inno.app.room.*;
import com.inno.service.Point;
import com.inno.service.Utils;
import com.inno.service.Triplet;


public class AutomaticPrices {

    public static void setAutomaticPrices(Room room, double minPrice, double maxPrice, double total, Core.AttributionType type)
    {
        Point posScene = new Point(room.getScene().getCenter()[0], room.getScene().getCenter()[1]);
        HashMap<Double, ArrayList<Triplet<String, String, Integer>>> seatsByDistance = new HashMap<>();
        long nbr = 0;
        for ( ImmutableSittingSection section : room.getImmutableSittingSections().values())
        {
            for (ImmutableSittingRow row : section.getImmutableSittingRows())
            {
                for(ImmutableSeat seat : row.getSeats())
                {
                    Point pos = new Point(seat.getPosition()[0], seat.getPosition()[1]);
                    Point sec = new Point (section.getPositions()[0], section.getPositions()[1]);
                    Point posSeat = Utils.rotatePoint(pos, sec, Math.toRadians(section.getRotation()));
                    System.out.println("seatID: "+section.getId()+"-"+row.getIdRow()+"-"+seat.getId());
                    System.out.println("position: "+ posSeat.get_x() +", "+ posSeat.get_y());
                    double distance = Utils.distance(posSeat, posScene);
                    Triplet<String, String, Integer> id = new Triplet<>(section.getId(), row.getIdRow(), seat.getId());
                    if (!seatsByDistance.containsKey(distance))
                    {
                        ArrayList<Triplet<String, String, Integer>> listeId = new ArrayList<>();
                        listeId.add(id);
                        seatsByDistance.put(distance, listeId);
                        nbr++;
                    }
                    else
                    {
                        ArrayList<Triplet<String, String, Integer>> listeid = seatsByDistance.get(distance);
                        listeid.add(id);
                        seatsByDistance.replace(distance, listeid);
                        nbr++;
                    }
                }
            }
        }

        TreeSet<Double> distancesMintoMax = new TreeSet<>(seatsByDistance.keySet());
        System.out.println(nbr+" distances: ");
        int i = 1;
        for(double d : distancesMintoMax)
        {
            System.out.println(i+": "+d);
            System.out.println("seat :"+seatsByDistance.get(d).get(0).getFirst()+ "-"
                    +seatsByDistance.get(d).get(0).getSecond()+"-"+seatsByDistance.get(d).get(0).getThird());
            i++;
        }

        if ((((total/nbr)>=minPrice) && ((total/nbr)<=maxPrice))||(total == 0))
        {
            double sum = 0;
            int m = 0;
            System.out.println("PRINT 1");
            for (double dist : distancesMintoMax)
            {
                ArrayList<Triplet<String, String, Integer>> listid = seatsByDistance.get(dist);
                for (Triplet<String, String, Integer> seatid : listid)
                {
                    double price = maxPrice - (m * ((maxPrice - minPrice) / (nbr - 1)));
                    Core.get().setSeatPrice(seatid.getFirst(), seatid.getSecond(), Integer.toString(seatid.getThird()), price);
                    sum += price;
                    System.out.println("seat "+seatid.getFirst()+"-"+seatid.getSecond()+"-"+Integer.toString(seatid.getThird())+ ": "
                            + Core.get().getSeatPrice(seatid.getFirst(), seatid.getSecond(), Integer.toString(seatid.getThird())).getPrice());
                    m++;
                }
            }

            if (((total/nbr)>=minPrice) && ((total/nbr)<=maxPrice)) {
                if (sum < total) {
                    System.out.println("PRINT 2");
                    int v = 0;
                    double revenu = 0;
                    double p = (2 * ((nbr * maxPrice) - total)) / (nbr * (nbr - 1));
                    for (double dist : distancesMintoMax) {
                        ArrayList<Triplet<String, String, Integer>> listid = seatsByDistance.get(dist);
                        for (Triplet<String, String, Integer> seatid : listid) {
                            System.out.println("seat " + seatid.getFirst() + "-" + seatid.getSecond() + "-" + Integer.toString(seatid.getThird()) + ": "
                                    + Core.get().getSeatPrice(seatid.getFirst(), seatid.getSecond(), Integer.toString(seatid.getThird())).getPrice());
                            Core.get().setSeatPrice(seatid.getFirst(), seatid.getSecond(), Integer.toString(seatid.getThird()), maxPrice - (p * v));
                            System.out.println("new price :" + Core.get().getSeatPrice(seatid.getFirst(), seatid.getSecond(), Integer.toString(seatid.getThird())).getPrice());
                            revenu += maxPrice - (p * v);
                            v++;
                        }
                    }
                    System.out.println(revenu);
                } else if (sum > total) {
                    System.out.println("PRINT 3");
                    int v = 0;
                    double revenu = 0;
                    double p = (2 * (total - (nbr * minPrice))) / (nbr * (nbr - 1));
                    for (double dist : distancesMintoMax.descendingSet()) {
                        ArrayList<Triplet<String, String, Integer>> listid = seatsByDistance.get(dist);
                        for (Triplet<String, String, Integer> seatid : listid) {
                            System.out.println("seat " + seatid.getFirst() + "-" + seatid.getSecond() + "-" + Integer.toString(seatid.getThird())
                                    + ": " + Core.get().getSeatPrice(seatid.getFirst(), seatid.getSecond(), Integer.toString(seatid.getThird())).getPrice());
                            Core.get().setSeatPrice(seatid.getFirst(), seatid.getSecond(), Integer.toString(seatid.getThird()), minPrice + (p * v));
                            System.out.println("new price :" + Core.get().getSeatPrice(seatid.getFirst(), seatid.getSecond(), Integer.toString(seatid.getThird())).getPrice());
                            revenu += minPrice + (p * v);
                            v++;
                        }
                    }
                    System.out.println(revenu);
                }

                for (double dist : seatsByDistance.keySet()) {
                    ArrayList<Triplet<String, String, Integer>> listid = seatsByDistance.get(dist);
                    int totalElements = listid.size();
                    if (totalElements > 1) {
                        double s = 0;
                        for (Triplet<String, String, Integer> seatid : listid) {
                            s += Core.get().getSeatPrice(seatid.getFirst(), seatid.getSecond(), Integer.toString(seatid.getThird())).getPrice();
                        }
                        double price = s / totalElements;
                        for (Triplet<String, String, Integer> seatid : listid) {
                            Core.get().setSeatPrice(seatid.getFirst(), seatid.getSecond(), Integer.toString(seatid.getThird()), price);
                        }
                    }
                }
            }
        }

        if (type == Core.AttributionType.ROW)
        {
            if ((((total/nbr)>=minPrice) && ((total/nbr)<=maxPrice))||(total == 0))
            {
                double revenu = 0;
                for (ImmutableSittingSection section : room.getImmutableSittingSections().values()) {
                    for (ImmutableSittingRow row : section.getImmutableSittingRows()) {
                        double rowPrice = 0;
                        int seatsInRow = 0;
                        for (ImmutableSeat seat : row.getSeats()) {
                            rowPrice += Core.get().getSeatPrice(section.getId(), row.getIdRow(), Integer.toString(seat.getId())).getPrice();
                            seatsInRow++;
                        }
                        Core.get().setRowPrice(section.getId(), row.getIdRow(), rowPrice / seatsInRow);
                        revenu += rowPrice;
                    }
                }
                System.out.println("Revenu total: " + revenu);
            }
        }

        if (type == Core.AttributionType.SECTION)
        {
            if ((((total/nbr)>=minPrice) && ((total/nbr)<=maxPrice))||(total == 0))
            {
                double revenu = 0;
                for (ImmutableSittingSection section : room.getImmutableSittingSections().values()) {
                    double sectionPrice = 0;
                    int seatsInSection = 0;
                    for (ImmutableSittingRow row : section.getImmutableSittingRows()) {

                        for (ImmutableSeat seat : row.getSeats()) {
                            sectionPrice += Core.get().getSeatPrice(section.getId(), row.getIdRow(), Integer.toString(seat.getId())).getPrice();
                            seatsInSection++;
                        }
                    }
                    Core.get().setSectionPrice(section.getId(), sectionPrice / seatsInSection);
                    revenu += sectionPrice;
                }
                System.out.println("Revenu total: " + revenu);
            }
        }
    }
}
