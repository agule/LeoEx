package it.agule.leoex;

//import java.sql.Time;

/**
 * Actual Timetable data and related types defined here
 */
/*
// direction: from FCO Airport to Rome city and back
enum Direction{ FCOtoRome, RometoFCO}

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
    TimetableItem(Direction direction, String departure, String arrival, EndStation endStation, String tripDuration,
                  TrainType trainType, int trainNumber, int ticketCost, int ticket1stClass){ this.direction = direction;
        this.departure=departure; this.arrival=arrival; this.endStation=endStation; this.tripDuration=tripDuration;
        this.trainType=trainType; this.trainNumber=trainNumber; this.ticketCost=ticketCost; this.ticket1stClass=ticket1stClass;
    }
    @Override
    public String toString(){
        String str = departure+" ";
        if(Direction.FCOtoRome==direction)
            str+= EndStation.FCOapt.name()+" → "+endStation.name();
        else if(Direction.RometoFCO==direction)
            str+= endStation.name()+" → "+EndStation.FCOapt.name();
        str+=" ("+arrival+") ";
        str+=trainType.code()+String.valueOf(trainNumber);
        str+=" ["+String.valueOf(ticketCost)+"€]";
        return str;
    }
}

public class TimetableOneDir {
    TimetableItem[] items;
}

public static TimetableOneDir timetableOneDir = {
    new TimetableItem(Direction.FCOtoRome,"05:57","06:45",EndStation.RomaTib,"00:48",TrainType.Reg,22001, 8,0),
    new TimetableItem(Direction.FCOtoRome,"06:23","06:55",EndStation.RomaTer,"00:32",TrainType.LE,  3231,14,0)
}
*/
//public static TimetableItem[] Timetable =
