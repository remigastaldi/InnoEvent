package com.inno.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import com.inno.app.room.*;
import com.inno.service.Point;
import com.inno.service.Utils;
import com.inno.service.Triplet;
import javafx.util.Pair;

public class AutomaticPrices {

    public static void setAutomaticPrices(Room room, double minPrice, double maxPrice, double total, Core.AttributionType type) {
        Point posScene = new Point(room.getScene().getCenter()[0], room.getScene().getCenter()[1]);
        HashMap<Double, ArrayList<Triplet<String, String, Integer>>> seatsByDistance = new HashMap<>();
        long nbr = 0;
        for (ImmutableSittingSection section : room.getImmutableSittingSections().values()) {
            for (ImmutableSittingRow row : section.getImmutableSittingRows()) {
                for (ImmutableSeat seat : row.getSeats()) {
                    Point pos = new Point(seat.getPosition()[0], seat.getPosition()[1]);
                    Point sec = new Point(section.getPositions()[0], section.getPositions()[1]);
                    Point posSeat = Utils.rotatePoint(pos, sec, Math.toRadians(section.getRotation()));
                    double distance = Utils.distance(posSeat, posScene);
                    Triplet<String, String, Integer> id = new Triplet<>(section.getId(), row.getIdRow(), seat.getId());
                    if (!seatsByDistance.containsKey(distance)) {
                        ArrayList<Triplet<String, String, Integer>> listeId = new ArrayList<>();
                        listeId.add(id);
                        seatsByDistance.put(distance, listeId);
                        nbr++;
                    } else {
                        ArrayList<Triplet<String, String, Integer>> listeid = seatsByDistance.get(distance);
                        listeid.add(id);
                        seatsByDistance.replace(distance, listeid);
                        nbr++;
                    }
                }
            }
        }

        TreeSet<Double> distancesMintoMax = new TreeSet<>(seatsByDistance.keySet());

        if ((((total / nbr) >= minPrice) && ((total / nbr) <= maxPrice)) || (total == 0)) {
            double R = 255;
            double G = 0;
            double B = 0;
            double i = (double) 512 / nbr;
            double sum = 0;
            int m = 0;
            for (double dist : distancesMintoMax) {
                ArrayList<Triplet<String, String, Integer>> listid = seatsByDistance.get(dist);
                for (Triplet<String, String, Integer> seatid : listid) {
                    String hex = String.format("#%02x%02x%02x", (int) R, (int) G, (int) B);
                    double price = maxPrice - (m * ((maxPrice - minPrice) / (nbr - 1)));
                    Core.get().setSeatPrice(seatid.getFirst(), seatid.getSecond(), Integer.toString(seatid.getThird()), price, hex);
                    sum += price;

                    if (R == 255) {
                        G += i;
                        if (G > 255) {
                            G = 255;
                        }
                    }
                    if (G == 255) {
                        R -= i;
                        if (R < 0) {
                            R = 0;
                        }
                    }
                    m++;
                }
            }

            if (((total / nbr) >= minPrice) && ((total / nbr) <= maxPrice)) {
                if (sum < total) {
                    double r = 255;
                    double g = 0;
                    double b = 0;
                    int v = 0;
                    double p = (2 * ((nbr * maxPrice) - total)) / (nbr * (nbr - 1));
                    for (double dist : distancesMintoMax) {
                        ArrayList<Triplet<String, String, Integer>> listid = seatsByDistance.get(dist);
                        for (Triplet<String, String, Integer> seatid : listid) {
                            String hex = String.format("#%02x%02x%02x", (int) r, (int) g, (int) b);
                            Core.get().setSeatPrice(seatid.getFirst(), seatid.getSecond(), Integer.toString(seatid.getThird()), maxPrice - (p * v), hex);
                            if (r == 255) {
                                g += i;
                                if (g > 255) {
                                    g = 255;
                                }
                            }
                            if (g == 255) {
                                r -= i;
                                if (r < 0) {
                                    r = 0;
                                }
                            }
                            v++;
                        }
                    }

                } else if (sum > total) {
                    double r = 0;
                    double g = 255;
                    double b = 0;
                    int v = 0;
                    double p = (2 * (total - (nbr * minPrice))) / (nbr * (nbr - 1));
                    for (double dist : distancesMintoMax.descendingSet()) {
                        ArrayList<Triplet<String, String, Integer>> listid = seatsByDistance.get(dist);
                        for (Triplet<String, String, Integer> seatid : listid) {
                            String hex = String.format("#%02x%02x%02x", (int) r, (int) g, (int) b);
                            Core.get().setSeatPrice(seatid.getFirst(), seatid.getSecond(), Integer.toString(seatid.getThird()), minPrice + (p * v), hex);
                            if (g == 255) {
                                r += i;
                                if (r > 255) {
                                    r = 255;
                                }
                            }
                            if (r == 255) {
                                g -= i;
                                if (g < 0) {
                                    g = 0;
                                }
                            }
                            v++;
                        }
                    }
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

        if (type == Core.AttributionType.ROW) {
            if ((((total / nbr) >= minPrice) && ((total / nbr) <= maxPrice)) || (total == 0)) {
                int nbrRows = 0;
                HashMap<Double, ArrayList<Pair<String, String>>> rowsByDistance = new HashMap<>();
                for (ImmutableSittingSection section : room.getImmutableSittingSections().values()) {
                    for (ImmutableSittingRow row : section.getImmutableSittingRows()) {
                        Point sec = new Point(section.getPositions()[0], section.getPositions()[1]);
                        Point start = new Point(row.getPosStartRow()[0], row.getPosStartRow()[1]);
                        start = Utils.rotatePoint(start, sec, Math.toRadians(section.getRotation()));
                        Point end = new Point(row.getPosEndRow()[0], row.getPosEndRow()[1]);
                        end = Utils.rotatePoint(end, sec, Math.toRadians(section.getRotation()));
                        Point mid = Utils.midPoint(start, end);
                        double distance = Utils.distance(mid, posScene);
                        Pair<String, String> id = new Pair<>(section.getId(), row.getIdRow());
                        if (!rowsByDistance.containsKey(distance)) {
                            ArrayList<Pair<String, String>> listeId = new ArrayList<>();
                            listeId.add(id);
                            rowsByDistance.put(distance, listeId);
                        } else {
                            ArrayList<Pair<String, String>> listeid = rowsByDistance.get(distance);
                            listeid.add(id);
                            rowsByDistance.replace(distance, listeid);
                        }
                        nbrRows++;
                    }
                }

                TreeSet<Double> rowDistancesMintoMax = new TreeSet<>(rowsByDistance.keySet());

                double r = 255;
                double g = 0;
                double b = 0;
                double i = (double) 512 / nbrRows;
                for (double dist : rowDistancesMintoMax) {
                    ArrayList<Pair<String, String>> listid = rowsByDistance.get(dist);
                    for (Pair<String, String> rowid : listid) {
                        double rowPrice = 0;
                        int seatsInRow = 0;
                        for (ImmutableSittingRow row : room.getImmutableSittingSections().get(rowid.getKey()).getImmutableSittingRows()) {
                            if (row.getIdRow().equals(rowid.getValue())) {
                                for (ImmutableSeat seat : row.getSeats()) {
                                    rowPrice += Core.get().getSeatPrice(rowid.getKey(), row.getIdRow(), Integer.toString(seat.getId())).getPrice();
                                    seatsInRow++;
                                }
                            }
                        }
                        String hex = String.format("#%02x%02x%02x", (int) r, (int) g, (int) b);
                        Core.get().setRowPrice(rowid.getKey(), rowid.getValue(), rowPrice / seatsInRow, hex);
                        if (r == 255) {
                            g += i;
                            if (g > 255) {
                                g = 255;
                            }
                        }
                        if (g == 255) {
                            r -= i;
                            if (r < 0) {
                                r = 0;
                            }
                        }
                    }
                }
            }
        }

        if (type == Core.AttributionType.SECTION) {
            if ((((total / nbr) >= minPrice) && ((total / nbr) <= maxPrice)) || (total == 0)) {
                int nbrSections = 0;
                HashMap<Double, ArrayList<String>> sectionsByDistance = new HashMap<>();
                for (ImmutableSittingSection section : room.getImmutableSittingSections().values()) {
                    Point sec = new Point(section.getPositions()[0], section.getPositions()[1]);
                    Point[] sectionPos = Utils.dArray_To_pArray(section.getPositions());
                    Point centroid = Utils.centroid(sectionPos);
                    centroid = Utils.rotatePoint(centroid, sec, Math.toRadians(section.getRotation()));
                    double distance = Utils.distance(centroid, posScene);
                    if (!sectionsByDistance.containsKey(distance)) {
                        ArrayList<String> listeId = new ArrayList<>();
                        listeId.add(section.getId());
                        sectionsByDistance.put(distance, listeId);
                    } else {
                        ArrayList<String> listeid = sectionsByDistance.get(distance);
                        listeid.add(section.getId());
                        sectionsByDistance.replace(distance, listeid);
                    }
                    nbrSections++;
                }

                TreeSet<Double> sectionsDistancesMintoMax = new TreeSet<>(sectionsByDistance.keySet());

                double r = 255;
                double g = 0;
                double b = 0;
                double i = (double) 512 / nbrSections;
                for (double dist : sectionsDistancesMintoMax) {
                    ArrayList<String> listid = sectionsByDistance.get(dist);
                    for (String sectionid : listid) {
                        double sectionPrice = 0;
                        int seatsInSection = 0;
                        for (ImmutableSittingRow row : room.getImmutableSittingSections().get(sectionid).getImmutableSittingRows()) {
                            for (ImmutableSeat seat : row.getSeats()) {
                                sectionPrice += Core.get().getSeatPrice(sectionid, row.getIdRow(), Integer.toString(seat.getId())).getPrice();
                                seatsInSection++;
                            }
                        }
                        String hex = String.format("#%02x%02x%02x", (int) r, (int) g, (int) b);
                        Core.get().setSectionPrice(sectionid, sectionPrice / seatsInSection, hex);
                        if (r == 255) {
                            g += i;
                            if (g > 255) {
                                g = 255;
                            }
                        }
                        if (g == 255) {
                            r -= i;
                            if (r < 0) {
                                r = 0;
                            }
                        }
                    }
                }
            }
        }
    }
}
