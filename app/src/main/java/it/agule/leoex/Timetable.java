package it.agule.leoex;

import android.text.format.Time;

/**
 * Actual Timetable data and related types defined here
 */

// direction: from FCO Airport to Rome city and back
enum Direction{ FCOToRome, RomeToFCO;
    public static int COUNT = 2;}

// Train end station in Rome
enum EndStation{
    RomaTer ("Roma Termini"),
    RomaTib ("Roma Tiburtina"),
    RomaOst ("Roma Ostiense"),
    FCOapt  ("✈ Fiumicino");

    private String name;
    EndStation(String name){this.name=name;}
//    public String name(){return name;}
}

enum TrainType{
    Reg ("R", "Regionale"),
    LE  ("LE","Leonardo Express"),
    FA  ("FA","FrecciArgento");

    private String code;
    private String name;
    TrainType(String code, String name){this.code=code;    this.name=name;}
    public String code(){return code;}
//    public String name(){return name;}
}

class TimetableItem{
    private Direction direction;
    private String  departure;
    private String  arrival;
    EndStation      endStation;
    private String  tripDuration;
    private TrainType trainType;
    private int     trainNumber;
    private int     ticketCost;
    private int     ticket1stClass;
    TimetableItem(String departure, String arrival, EndStation endStation, String tripDuration,
                  TrainType trainType, int trainNumber, int ticketCost, int ticket1stClass){ this.direction = direction;
        this.departure=departure; this.arrival=arrival; this.endStation=endStation; this.tripDuration=tripDuration;
        this.trainType=trainType; this.trainNumber=trainNumber; this.ticketCost=ticketCost; this.ticket1stClass=ticket1stClass;
    }
    void setDirection(Direction dir){direction=dir;}
    @Override
    public String toString(){
        String str = departure+" ";
        if(Direction.FCOToRome==direction)
            str+= EndStation.FCOapt.name()+" → "+endStation.name();
        else if(Direction.RomeToFCO==direction)
            str+= endStation.name()+" → "+EndStation.FCOapt.name();
        str+=" ("+arrival+") ";
        str+=trainType.code()+String.valueOf(trainNumber);
        str+=" ["+String.valueOf(ticketCost)+"€]";
        return str;
    }
}

public class Timetable{
    private static TimetableItem[] itemsToRome ={   // Direction.FCOToRome
        new TimetableItem("05:57","06:45",EndStation.RomaTib,"00:48",TrainType.Reg,22001, 8,0),
        new TimetableItem("06:23","06:55",EndStation.RomaTer,"00:32",TrainType.LE,  3231,14,0)
        };
    private int cntToRome = 0;
//    private TimetableItem[] itemsToFCO = null;
    private int cntToFCO = 0;

    Timetable(){    // count items and initialize directions accordingly
        for(TimetableItem item: itemsToRome){
            item.setDirection(Direction.FCOToRome);
            cntToRome++;
        }
//        for(TimetableItem item: itemsToFCO){
//            item.setDirection(Direction.RomeToFCO);
//            cntToFCO++;
//        }
    }

    String[] getTimetableStrings() {
        String[] stringsArray = new String[cntToRome];
        for(int i = 0; i < cntToRome; i++){
            stringsArray[i] = itemsToRome[i].toString();
        }
        return stringsArray;
    }

    String[] testTimetableStrings(){
        String[] timetableArray = { // some pre-produced list items...
                "05:57 ✈Fiumicino → Roma Tiburtina (06:45) R22001 [8€]",
                "06:23 ✈Fiumicino → Roma Termini (06:55) LE3231 [14€]",
                "06:27 ✈Fiumicino → Roma Tiburtina (07:15) R22003 [8€]",
                "06:42 ✈Fiumicino → Roma Tiburtina (07:30) R22005 [8€]",
                "06:53 ✈Fiumicino → Roma Termini (07:25) LE3235 [14€]",
                "06:57 ✈Fiumicino → Roma Tiburtina (07:45) R22007 [8€]",
                "07:12 ✈Fiumicino → Roma Tiburtina (08:00) R22009 [8€]",
                "07:23 ✈Fiumicino → Roma Termini (07:55) LE3239 [14€]",
                "07:27 ✈Fiumicino → Roma Tiburtina (08:15) R22011 [8€]",
                "07:42 ✈Fiumicino → Roma Tiburtina (08:30) R22013 [8€]",
                "07:53 ✈Fiumicino → Roma Termini (07:85) LE3243 [14€]",
                "07:57 ✈Fiumicino → Roma Tiburtina (08:45) R22015 [8€]",
        };
        return timetableArray;
    }
}

