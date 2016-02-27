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
    public String Name(){return name;}
}

enum TrainType{
    Reg ("R", "Regionale"),
    LE  ("LE","Leonardo Express"),
    FA  ("FA","FrecciArgento");

    private String code;
    private String name;
    TrainType(String code, String name){this.code=code;    this.name=name;}
    public String code(){return code;}
    public String Name(){return name;}
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
                  TrainType trainType, int trainNumber, int ticketCost, int ticket1stClass){
        this.departure=departure; this.arrival=arrival; this.endStation=endStation; this.tripDuration=tripDuration;
        this.trainType=trainType; this.trainNumber=trainNumber; this.ticketCost=ticketCost; this.ticket1stClass=ticket1stClass;
    }
    void setDirection(Direction dir){direction=dir;}
    @Override
    public String toString(){   // R22001 \n 05:57 ✈Fiumicino → Roma Tiburtina (06:45) [8€]
        String str = trainType.code()+String.valueOf(trainNumber)+"\n";
        str += departure+" ";
        if(Direction.FCOToRome==direction)
            str+= EndStation.FCOapt.Name()+" → "+endStation.Name();
        else if(Direction.RomeToFCO==direction)
            str+= endStation.Name()+" → "+EndStation.FCOapt.Name();
        str+=" ("+arrival+") ";
        str+=" ["+String.valueOf(ticketCost)+"€";
        if(ticket1stClass>0)
            str+="/"+String.valueOf(ticket1stClass)+"€";
        str+="]";
        return str;
    }
}

public class Timetable{     // public interface class
    private TimetableData timetableData = null;

    Timetable(){    timetableData = new TimetableData();    }

    String[] getTimetableStrings() {
        String[] stringsArray = new String[timetableData.cntToRome+1];
        for(int i = 0; i < timetableData.cntToRome; i++){
            stringsArray[i] = timetableData.itemsToRome[i].toString();
        }
        stringsArray[timetableData.cntToRome] = "R22011";
        return stringsArray;
    }

    String[] testTimetableStrings(){     // some pre-produced list items...
        return timetableData.testStringArray;
    }
}

class TimetableData {
    static TimetableItem[] itemsToRome = {   // Direction.FCOToRome
            new TimetableItem("05:57", "06:45", EndStation.RomaTib, "00:48", TrainType.Reg, 22001, 8, 0),
            new TimetableItem("06:23", "06:55", EndStation.RomaTer, "00:32", TrainType.LE, 3231, 14, 0),
            new TimetableItem("06:27", "07:15", EndStation.RomaTib, "00:48", TrainType.Reg, 22003, 8, 0),
            new TimetableItem("06:42", "07:30", EndStation.RomaTib, "00:48", TrainType.Reg, 22005, 8, 0),
            new TimetableItem("06:53", "07:25", EndStation.RomaTer, "00:32", TrainType.LE, 3235, 14, 0),
            new TimetableItem("06:57", "07:45", EndStation.RomaTib, "00:48", TrainType.Reg, 22007, 8, 0),
            new TimetableItem("07:12", "08:00", EndStation.RomaTib, "00:48", TrainType.Reg, 22009, 8, 0),
            new TimetableItem("07:23", "07:55", EndStation.RomaTer, "00:32", TrainType.LE, 3239, 14, 0),
            new TimetableItem("07:27", "08:15", EndStation.RomaTib, "00:48", TrainType.Reg, 22011, 8, 0),
            new TimetableItem("07:42", "08:30", EndStation.RomaTib, "00:48", TrainType.Reg, 22013, 8, 0),
            new TimetableItem("07:53", "08:25", EndStation.RomaTer, "00:32", TrainType.LE, 3243, 14, 0),
            new TimetableItem("07:57", "08:45", EndStation.RomaTib, "00:48", TrainType.Reg, 22015, 8, 0),
            new TimetableItem("08:12", "09:00", EndStation.RomaTib, "00:48", TrainType.Reg, 22017, 8, 0),
            new TimetableItem("08:23", "08:55", EndStation.RomaTer, "00:32", TrainType.LE, 3247, 14, 0),
            new TimetableItem("08:27", "09:15", EndStation.RomaTib, "00:48", TrainType.Reg, 22019, 8, 0),
            new TimetableItem("08:42", "09:30", EndStation.RomaTib, "00:48", TrainType.Reg, 22021, 8, 0),
            new TimetableItem("08:53", "09:25", EndStation.RomaTer, "00:32", TrainType.LE, 3251, 14, 0),
            new TimetableItem("08:57", "09:45", EndStation.RomaTib, "00:48", TrainType.Reg, 22023, 8, 0),
            new TimetableItem("09:12", "10:00", EndStation.RomaTib, "00:48", TrainType.Reg, 22025, 8, 0),
            new TimetableItem("09:23", "09:55", EndStation.RomaTer, "00:32", TrainType.LE, 3255, 14, 0),
            new TimetableItem("09:27", "10:15", EndStation.RomaTib, "00:48", TrainType.Reg, 22027, 8, 0),
            new TimetableItem("09:38", "10:10", EndStation.RomaTer, "00:32", TrainType.LE, 3257, 14, 0),
            new TimetableItem("09:42", "10:30", EndStation.RomaTib, "00:48", TrainType.Reg, 22029, 8, 0),
            new TimetableItem("09:53", "10:25", EndStation.RomaTer, "00:32", TrainType.LE, 3259, 14, 0),
            new TimetableItem("09:57", "10:45", EndStation.RomaTib, "00:48", TrainType.Reg, 22031, 8, 0),
            new TimetableItem("10:12", "11:00", EndStation.RomaTib, "00:48", TrainType.Reg, 22033, 8, 0),
            new TimetableItem("10:23", "10:55", EndStation.RomaTer, "00:32", TrainType.LE, 3263, 14, 0),
            new TimetableItem("10:27", "11:15", EndStation.RomaTib, "00:48", TrainType.Reg, 22035, 8, 0),
            new TimetableItem("10:42", "11:30", EndStation.RomaTib, "00:48", TrainType.Reg, 22037, 8, 0),
            new TimetableItem("10:53", "11:25", EndStation.RomaTer, "00:32", TrainType.LE, 3267, 14, 0),
            new TimetableItem("10:57", "11:45", EndStation.RomaTib, "00:48", TrainType.Reg, 22039, 8, 0),
            new TimetableItem("11:08", "11:40", EndStation.RomaTer, "00:32", TrainType.FA, 9491, 18, 21),
            new TimetableItem("11:12", "12:00", EndStation.RomaTib, "00:48", TrainType.Reg, 22041, 8, 0),
            new TimetableItem("11:23", "11:55", EndStation.RomaTer, "00:32", TrainType.LE, 3271, 14, 0),
            new TimetableItem("11:27", "12:15", EndStation.RomaTib, "00:48", TrainType.Reg, 22043, 8, 0),
            new TimetableItem("11:38", "12:10", EndStation.RomaTer, "00:32", TrainType.LE, 3273, 14, 0),
            new TimetableItem("11:42", "12:30", EndStation.RomaTib, "00:48", TrainType.Reg, 22045, 8, 0),
            new TimetableItem("11:53", "12:25", EndStation.RomaTer, "00:32", TrainType.LE, 3275, 14, 0),
            new TimetableItem("11:57", "12:45", EndStation.RomaTib, "00:48", TrainType.Reg, 22047, 8, 0)
    };
    int cntToRome = 0;
    static TimetableItem[] itemsToFCO = {     // Direction.RomeToFCO
            new TimetableItem("05:57", "06:45", EndStation.RomaTib, "00:48", TrainType.Reg, 22001, 8, 0),
            new TimetableItem("05:01", "05:48", EndStation.RomaTib, "00:47", TrainType.Reg, 21996, 8, 0),
            new TimetableItem("05:35", "06:07", EndStation.RomaTer, "00:32", TrainType.LE, 3230, 14, 0),
            new TimetableItem("05:46", "06:33", EndStation.RomaTib, "00:47", TrainType.Reg, 22000, 8, 0),
            new TimetableItem("05:47", "06:18", EndStation.RomaOst, "00:31", TrainType.Reg, 21998, 8, 0),
            new TimetableItem("06:01", "06:48", EndStation.RomaTib, "00:47", TrainType.Reg, 22002, 8, 0),
            new TimetableItem("06:05", "06:37", EndStation.RomaTer, "00:32", TrainType.LE, 3234, 14, 0),
            new TimetableItem("06:16", "07:03", EndStation.RomaTib, "00:47", TrainType.Reg, 22004, 8, 0),
            new TimetableItem("06:31", "07:18", EndStation.RomaTib, "00:47", TrainType.Reg, 22006, 8, 0),
            new TimetableItem("06:35", "07:07", EndStation.RomaTer, "00:32", TrainType.LE, 3238, 14, 0),
            new TimetableItem("06:46", "07:33", EndStation.RomaTib, "00:47", TrainType.Reg, 22008, 8, 0),
            new TimetableItem("07:01", "07:48", EndStation.RomaTib, "00:47", TrainType.Reg, 22010, 8, 0),
            new TimetableItem("07:05", "07:37", EndStation.RomaTer, "00:32", TrainType.LE, 3242, 14, 0),
            new TimetableItem("07:16", "08:03", EndStation.RomaTib, "00:47", TrainType.Reg, 22012, 8, 0),
            new TimetableItem("07:31", "08:18", EndStation.RomaTib, "00:47", TrainType.Reg, 22014, 8, 0),
            new TimetableItem("07:35", "08:07", EndStation.RomaTer, "00:32", TrainType.LE, 3246, 14, 0),
            new TimetableItem("07:46", "08:33", EndStation.RomaTib, "00:47", TrainType.Reg, 22016, 8, 0),
            new TimetableItem("08:01", "08:48", EndStation.RomaTib, "00:47", TrainType.Reg, 22018, 8, 0),
            new TimetableItem("08:05", "08:37", EndStation.RomaTer, "00:32", TrainType.LE, 3250, 14, 0)
    };
    int cntToFCO = 0;

    TimetableData() {    // count items and initialize directions accordingly
        for (TimetableItem item : itemsToRome) {
            item.setDirection(Direction.FCOToRome);
            cntToRome++;
        }
        for (TimetableItem item : itemsToFCO) {
            item.setDirection(Direction.RomeToFCO);
            cntToFCO++;
        }
    }

    String[] testStringArray = { // some pre-produced list items...
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
            "07:53 ✈Fiumicino → Roma Termini (08:25) LE3243 [14€]",
            "07:57 ✈Fiumicino → Roma Tiburtina (08:45) R22015 [8€]",
    };

}