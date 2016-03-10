package it.agule.leoex;

import java.text.DateFormat;
import android.text.format.Time;
import android.util.Log;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
    private final String TAG = TimetableItem.class.getSimpleName();
    Direction   direction;
    Time        departureTime;          // TODO: Time is deprecated, should be updated to Date(?)
    Time        arrivalTime;
    EndStation  endStation;
    int         tripDurationMin;
    TrainType   trainType;
    int         trainNumber;
    int         ticketCost;
    int         ticket1stClass;

    TimetableItem(String departure, String arrival, EndStation endStation, String tripDuration,
                  TrainType trainType, int trainNumber, int ticketCost, int ticket1stClass){
        departureTime = new Time("Europe/Rome");        // Timetable is in Local Rome time
        int hour = Integer.parseInt(departure.substring(0,2)), min  = Integer.parseInt(departure.substring(3));
        departureTime.set(0, min, hour, 1, 3, 2016);    // bogus date, to be updated later
        arrivalTime = new Time("Europe/Rome");
        hour = Integer.parseInt(arrival.substring(0,2)); min = Integer.parseInt(arrival.substring(3));
        arrivalTime.set(0,  min, hour, 1, 3, 2016);    // bogus date, to be updated later
        hour = Integer.parseInt(tripDuration.substring(0,2)); min = Integer.parseInt(tripDuration.substring(3));
        this.endStation=endStation;
        tripDurationMin = hour * 60 + min;
        this.trainType=trainType; this.trainNumber=trainNumber; this.ticketCost=ticketCost; this.ticket1stClass=ticket1stClass;
    }
    void setDateDirection(Direction dir) {
        direction = dir;
        if (TimetableData.today == null){
            Log.e(TAG, "TimetableData.today == null");
        }else{
            departureTime.set(0, departureTime.minute, departureTime.hour,
                    TimetableData.today.monthDay, TimetableData.today.month, TimetableData.today.year);
            arrivalTime.set  (0, arrivalTime.minute,   arrivalTime.hour,
                    TimetableData.today.monthDay, TimetableData.today.month, TimetableData.today.year);
        }
    }
    @Override
    public String toString(){   // R22001 \n 05:57 ✈Fiumicino → Roma Tiburtina (06:45) [8€]
        String str = trainType.code()+String.valueOf(trainNumber)+"\n";
        str+=TimetableData.dateFormat.format(new Date(departureTime.toMillis(false)))+" ";
        if(Direction.FCOToRome==direction)
            str+= EndStation.FCOapt.Name()+" → "+endStation.Name();
        else if(Direction.RomeToFCO==direction)
            str+= endStation.Name()+" → "+EndStation.FCOapt.Name();
        str+= " (" + TimetableData.dateFormat.format(new Date(arrivalTime.toMillis(false))) +") ";
        str+=" ["+String.valueOf(ticketCost)+"€";
        if(ticket1stClass>0)
            str+="/"+String.valueOf(ticket1stClass)+"€";
        str+="]";
        return str;
    }
}

public class Timetable{     // public interface class
    private final String TAG = Timetable.class.getSimpleName();
    private TimetableData timetableData = null;
    private Time now = new Time();

    Timetable(){    timetableData = new TimetableData();    }

    String[] getTimetableStrings() {
        String[] stringsArray = new String[timetableData.cntToFCO];
        for(int i = 0; i < timetableData.cntToFCO; i++){
            stringsArray[i] = timetableData.itemsToFCO[i].toString();
        }
        return stringsArray;
    }

    int getItemBeforeNow(){
        now.setToNow();
//        Log.v(TAG, "Now: " + now.toString());
//        Log.v(TAG, "Now in Rome: " + TimetableData.dateFormat.format(new Date(now.toMillis(false))));
        int i;
        for(i = 0; i < timetableData.cntToFCO; i++){
            if(timetableData.itemsToFCO[i].departureTime.after(now))
                break;
        }
//        Log.v(TAG, "TimetableItem ID after now: " + String.valueOf(i));
        if(i>0) return i-1;
        else    return i;
    }

    String[] testTimetableStrings(){     // some pre-produced list items...
        return timetableData.testStringArray;
    }
}

class TimetableData {
    private final String TAG = TimetableData.class.getSimpleName();
    static TimetableItem[] itemsToRome = {   // Direction.FCOToRome
            new TimetableItem("05:57", "06:45", EndStation.RomaTib, "00:48", TrainType.Reg, 22001, 8, 0),
            new TimetableItem("06:23", "06:55", EndStation.RomaTer, "00:32", TrainType.LE,  3231, 14, 0),
            new TimetableItem("06:27", "07:15", EndStation.RomaTib, "00:48", TrainType.Reg, 22003, 8, 0),
            new TimetableItem("06:42", "07:30", EndStation.RomaTib, "00:48", TrainType.Reg, 22005, 8, 0),
            new TimetableItem("06:53", "07:25", EndStation.RomaTer, "00:32", TrainType.LE,  3235, 14, 0),
            new TimetableItem("06:57", "07:45", EndStation.RomaTib, "00:48", TrainType.Reg, 22007, 8, 0),
            new TimetableItem("07:12", "08:00", EndStation.RomaTib, "00:48", TrainType.Reg, 22009, 8, 0),
            new TimetableItem("07:23", "07:55", EndStation.RomaTer, "00:32", TrainType.LE,  3239, 14, 0),
            new TimetableItem("07:27", "08:15", EndStation.RomaTib, "00:48", TrainType.Reg, 22011, 8, 0),
            new TimetableItem("07:42", "08:30", EndStation.RomaTib, "00:48", TrainType.Reg, 22013, 8, 0),
            new TimetableItem("07:53", "08:25", EndStation.RomaTer, "00:32", TrainType.LE,  3243, 14, 0),
            new TimetableItem("07:57", "08:45", EndStation.RomaTib, "00:48", TrainType.Reg, 22015, 8, 0),
            new TimetableItem("08:12", "09:00", EndStation.RomaTib, "00:48", TrainType.Reg, 22017, 8, 0),
            new TimetableItem("08:23", "08:55", EndStation.RomaTer, "00:32", TrainType.LE,  3247, 14, 0),
            new TimetableItem("08:27", "09:15", EndStation.RomaTib, "00:48", TrainType.Reg, 22019, 8, 0),
            new TimetableItem("08:42", "09:30", EndStation.RomaTib, "00:48", TrainType.Reg, 22021, 8, 0),
            new TimetableItem("08:53", "09:25", EndStation.RomaTer, "00:32", TrainType.LE,  3251, 14, 0),
            new TimetableItem("08:57", "09:45", EndStation.RomaTib, "00:48", TrainType.Reg, 22023, 8, 0),
            new TimetableItem("09:12", "10:00", EndStation.RomaTib, "00:48", TrainType.Reg, 22025, 8, 0),
            new TimetableItem("09:23", "09:55", EndStation.RomaTer, "00:32", TrainType.LE,  3255, 14, 0),
            new TimetableItem("09:27", "10:15", EndStation.RomaTib, "00:48", TrainType.Reg, 22027, 8, 0),
            new TimetableItem("09:38", "10:10", EndStation.RomaTer, "00:32", TrainType.LE,  3257, 14, 0),
            new TimetableItem("09:42", "10:30", EndStation.RomaTib, "00:48", TrainType.Reg, 22029, 8, 0),
            new TimetableItem("09:53", "10:25", EndStation.RomaTer, "00:32", TrainType.LE,  3259, 14, 0),
            new TimetableItem("09:57", "10:45", EndStation.RomaTib, "00:48", TrainType.Reg, 22031, 8, 0),
            new TimetableItem("10:12", "11:00", EndStation.RomaTib, "00:48", TrainType.Reg, 22033, 8, 0),
            new TimetableItem("10:23", "10:55", EndStation.RomaTer, "00:32", TrainType.LE,  3263, 14, 0),
            new TimetableItem("10:27", "11:15", EndStation.RomaTib, "00:48", TrainType.Reg, 22035, 8, 0),
            new TimetableItem("10:42", "11:30", EndStation.RomaTib, "00:48", TrainType.Reg, 22037, 8, 0),
            new TimetableItem("10:53", "11:25", EndStation.RomaTer, "00:32", TrainType.LE,  3267, 14, 0),
            new TimetableItem("10:57", "11:45", EndStation.RomaTib, "00:48", TrainType.Reg, 22039, 8, 0),
            new TimetableItem("11:08", "11:40", EndStation.RomaTer, "00:32", TrainType.FA,  9491, 18, 21),
            new TimetableItem("11:12", "12:00", EndStation.RomaTib, "00:48", TrainType.Reg, 22041, 8, 0),
            new TimetableItem("11:23", "11:55", EndStation.RomaTer, "00:32", TrainType.LE,  3271, 14, 0),
            new TimetableItem("11:27", "12:15", EndStation.RomaTib, "00:48", TrainType.Reg, 22043, 8, 0),
            new TimetableItem("11:38", "12:10", EndStation.RomaTer, "00:32", TrainType.LE,  3273, 14, 0),
            new TimetableItem("11:42", "12:30", EndStation.RomaTib, "00:48", TrainType.Reg, 22045, 8, 0),
            new TimetableItem("11:53", "12:25", EndStation.RomaTer, "00:32", TrainType.LE,  3275, 14, 0),
            new TimetableItem("11:57", "12:45", EndStation.RomaTib, "00:48", TrainType.Reg, 22047, 8, 0),
            new TimetableItem("12:08", "12:40", EndStation.RomaTer, "00:32", TrainType.LE,  3277, 14, 0),
            new TimetableItem("12:12", "13:00", EndStation.RomaTib, "00:48", TrainType.Reg, 22049, 8, 0),
            new TimetableItem("12:23", "12:55", EndStation.RomaTer, "00:32", TrainType.LE,  3279, 14, 0),
            new TimetableItem("12:27", "13:15", EndStation.RomaTib, "00:48", TrainType.Reg, 22051, 8, 0),
            new TimetableItem("12:38", "13:10", EndStation.RomaTer, "00:32", TrainType.LE,  3281, 14, 0),
            new TimetableItem("12:42", "13:30", EndStation.RomaTib, "00:48", TrainType.Reg, 22053, 8, 0),
            new TimetableItem("12:53", "13:25", EndStation.RomaTer, "00:32", TrainType.LE,  3283, 14, 0),
            new TimetableItem("12:57", "13:45", EndStation.RomaTib, "00:48", TrainType.Reg, 22055, 8, 0),
            new TimetableItem("13:08", "13:40", EndStation.RomaTer, "00:32", TrainType.LE,  3285, 14, 0),
            new TimetableItem("13:12", "14:00", EndStation.RomaTib, "00:48", TrainType.Reg, 22057, 8, 0),
            new TimetableItem("13:23", "13:55", EndStation.RomaTer, "00:32", TrainType.LE,  3287, 14, 0),
            new TimetableItem("13:27", "14:15", EndStation.RomaTib, "00:48", TrainType.Reg, 22059, 8, 0),
            new TimetableItem("13:38", "14:10", EndStation.RomaTer, "00:32", TrainType.LE,  3289, 14, 0),
            new TimetableItem("13:42", "14:30", EndStation.RomaTib, "00:48", TrainType.Reg, 22061, 8, 0),
            new TimetableItem("13:53", "14:25", EndStation.RomaTer, "00:32", TrainType.LE,  3291, 14, 0),
            new TimetableItem("13:57", "14:45", EndStation.RomaTib, "00:48", TrainType.Reg, 22063, 8, 0),
            new TimetableItem("14:12", "15:00", EndStation.RomaTib, "00:48", TrainType.Reg, 22065, 8, 0),
            new TimetableItem("14:23", "14:55", EndStation.RomaTer, "00:32", TrainType.LE,  3295, 14, 0),
            new TimetableItem("14:27", "15:15", EndStation.RomaTib, "00:48", TrainType.Reg, 22067, 8, 0),
            new TimetableItem("14:42", "15:30", EndStation.RomaTib, "00:48", TrainType.Reg, 22069, 8, 0),
            new TimetableItem("14:53", "15:25", EndStation.RomaTer, "00:32", TrainType.LE,  3299, 14, 0),
            new TimetableItem("14:57", "15:45", EndStation.RomaTib, "00:48", TrainType.Reg, 22071, 8, 0),
            new TimetableItem("15:08", "15:40", EndStation.RomaTer, "00:32", TrainType.FA,  9493, 18,21),
            new TimetableItem("15:12", "16:00", EndStation.RomaTib, "00:48", TrainType.Reg, 22073, 8, 0),
            new TimetableItem("15:23", "15:55", EndStation.RomaTer, "00:32", TrainType.LE,  3303, 14, 0),
            new TimetableItem("15:27", "16:15", EndStation.RomaTib, "00:48", TrainType.Reg, 22075, 8, 0),
            new TimetableItem("15:38", "16:10", EndStation.RomaTer, "00:32", TrainType.LE,  3305, 14, 0),
            new TimetableItem("15:42", "16:30", EndStation.RomaTib, "00:48", TrainType.Reg, 22077, 8, 0),
            new TimetableItem("15:53", "16:25", EndStation.RomaTer, "00:32", TrainType.LE,  3307, 14, 0),
            new TimetableItem("15:57", "16:45", EndStation.RomaTib, "00:48", TrainType.Reg, 22079, 8, 0),
            new TimetableItem("16:08", "16:40", EndStation.RomaTer, "00:32", TrainType.LE,  3309, 14, 0),
            new TimetableItem("16:12", "17:00", EndStation.RomaTib, "00:48", TrainType.Reg, 22081, 8, 0),
            new TimetableItem("16:23", "16:55", EndStation.RomaTer, "00:32", TrainType.LE,  3311, 14, 0),
            new TimetableItem("16:27", "17:15", EndStation.RomaTib, "00:48", TrainType.Reg, 22083, 8, 0),
            new TimetableItem("16:38", "17:10", EndStation.RomaTer, "00:32", TrainType.LE,  3313, 14, 0),
            new TimetableItem("16:42", "17:30", EndStation.RomaTib, "00:48", TrainType.Reg, 22085, 8, 0),
            new TimetableItem("16:53", "17:25", EndStation.RomaTer, "00:32", TrainType.LE,  3315, 14, 0),
            new TimetableItem("16:57", "17:45", EndStation.RomaTib, "00:48", TrainType.Reg, 22087, 8, 0),
            new TimetableItem("17:08", "17:40", EndStation.RomaTer, "00:32", TrainType.LE,  3317, 14, 0),
            new TimetableItem("17:12", "18:00", EndStation.RomaTib, "00:48", TrainType.Reg, 22089, 8, 0),
            new TimetableItem("17:23", "17:55", EndStation.RomaTer, "00:32", TrainType.LE,  3319, 14, 0),
            new TimetableItem("17:27", "18:15", EndStation.RomaTib, "00:48", TrainType.Reg, 22091, 8, 0),
            new TimetableItem("17:38", "18:10", EndStation.RomaTer, "00:32", TrainType.LE,  3321, 14, 0),
            new TimetableItem("17:42", "18:30", EndStation.RomaTib, "00:48", TrainType.Reg, 22093, 8, 0),
            new TimetableItem("17:53", "18:25", EndStation.RomaTer, "00:32", TrainType.LE,  3323, 14, 0),
            new TimetableItem("17:57", "18:45", EndStation.RomaTib, "00:48", TrainType.Reg, 22095, 8, 0),
            new TimetableItem("18:08", "18:40", EndStation.RomaTer, "00:32", TrainType.LE,  3325, 14, 0),
            new TimetableItem("18:12", "19:00", EndStation.RomaTib, "00:48", TrainType.Reg, 22097, 8, 0),
            new TimetableItem("18:23", "18:55", EndStation.RomaTer, "00:32", TrainType.LE,  3327, 14, 0),
            new TimetableItem("18:27", "19:15", EndStation.RomaTib, "00:48", TrainType.Reg, 22099, 8, 0),
            new TimetableItem("18:38", "19:10", EndStation.RomaTer, "00:32", TrainType.LE,  3329, 14, 0),
            new TimetableItem("18:42", "19:30", EndStation.RomaTib, "00:48", TrainType.Reg, 22101, 8, 0),
            new TimetableItem("18:53", "19:25", EndStation.RomaTer, "00:32", TrainType.LE,  3331, 14, 0),
            new TimetableItem("18:57", "19:45", EndStation.RomaTib, "00:48", TrainType.Reg, 22103, 8, 0),
            new TimetableItem("19:08", "19:40", EndStation.RomaTer, "00:32", TrainType.LE,  3333, 14, 0),
            new TimetableItem("19:12", "20:00", EndStation.RomaTib, "00:48", TrainType.Reg, 22105, 8, 0),
            new TimetableItem("19:23", "19:55", EndStation.RomaTer, "00:32", TrainType.LE,  3335, 14, 0),
            new TimetableItem("19:27", "20:15", EndStation.RomaTib, "00:48", TrainType.Reg, 22107, 8, 0),
            new TimetableItem("19:38", "20:10", EndStation.RomaTer, "00:32", TrainType.LE,  3337, 14, 0),
            new TimetableItem("19:42", "20:30", EndStation.RomaTib, "00:48", TrainType.Reg, 22109, 8, 0),
            new TimetableItem("19:53", "20:25", EndStation.RomaTer, "00:32", TrainType.LE,  3339, 14, 0),
            new TimetableItem("19:57", "20:45", EndStation.RomaTib, "00:48", TrainType.Reg, 22111, 8, 0),
            new TimetableItem("20:08", "20:40", EndStation.RomaTer, "00:32", TrainType.LE,  3341, 14, 0),
            new TimetableItem("20:12", "21:00", EndStation.RomaTib, "00:48", TrainType.Reg, 22113, 8, 0),
            new TimetableItem("20:23", "20:55", EndStation.RomaTer, "00:32", TrainType.LE,  3343, 14, 0),
            new TimetableItem("20:42", "21:30", EndStation.RomaTib, "00:48", TrainType.Reg, 22115, 8, 0),
            new TimetableItem("20:53", "21:25", EndStation.RomaTer, "00:32", TrainType.LE,  3347, 14, 0),
            new TimetableItem("21:12", "22:00", EndStation.RomaTib, "00:48", TrainType.Reg, 22117, 8, 0),
            new TimetableItem("21:23", "21:55", EndStation.RomaTer, "00:32", TrainType.LE,  3351, 14, 0),
            new TimetableItem("21:42", "22:30", EndStation.RomaTib, "00:48", TrainType.Reg, 22119, 8, 0),
            new TimetableItem("21:53", "22:25", EndStation.RomaTer, "00:32", TrainType.LE,  3355, 14, 0),
            new TimetableItem("22:12", "23:00", EndStation.RomaTib, "00:48", TrainType.Reg, 22121, 8, 0),
            new TimetableItem("22:23", "22:55", EndStation.RomaTer, "00:32", TrainType.LE,  3359, 14, 0),
            new TimetableItem("22:42", "23:30", EndStation.RomaTib, "00:48", TrainType.Reg, 22123, 8, 0),
            new TimetableItem("22:53", "23:25", EndStation.RomaTer, "00:32", TrainType.LE,  3363, 14, 0),
            new TimetableItem("23:23", "23:55", EndStation.RomaTer, "00:32", TrainType.LE,  3367, 14, 0),
            new TimetableItem("23:27", "23:59", EndStation.RomaOst, "00:32", TrainType.Reg, 22125, 8, 0)
    };
    int cntToRome = 0;
    static TimetableItem[] itemsToFCO = {     // Direction.RomeToFCO
            new TimetableItem("05:01", "05:48", EndStation.RomaTib, "00:47", TrainType.Reg, 21996, 8, 0),
            new TimetableItem("05:35", "06:07", EndStation.RomaTer, "00:32", TrainType.LE,  3230, 14, 0),
            new TimetableItem("05:46", "06:33", EndStation.RomaTib, "00:47", TrainType.Reg, 22000, 8, 0),
            new TimetableItem("05:47", "06:18", EndStation.RomaOst, "00:31", TrainType.Reg, 21998, 8, 0),
            new TimetableItem("06:01", "06:48", EndStation.RomaTib, "00:47", TrainType.Reg, 22002, 8, 0),
            new TimetableItem("06:05", "06:37", EndStation.RomaTer, "00:32", TrainType.LE,  3234, 14, 0),
            new TimetableItem("06:16", "07:03", EndStation.RomaTib, "00:47", TrainType.Reg, 22004, 8, 0),
            new TimetableItem("06:31", "07:18", EndStation.RomaTib, "00:47", TrainType.Reg, 22006, 8, 0),
            new TimetableItem("06:35", "07:07", EndStation.RomaTer, "00:32", TrainType.LE,  3238, 14, 0),
            new TimetableItem("06:46", "07:33", EndStation.RomaTib, "00:47", TrainType.Reg, 22008, 8, 0),
            new TimetableItem("07:01", "07:48", EndStation.RomaTib, "00:47", TrainType.Reg, 22010, 8, 0),
            new TimetableItem("07:05", "07:37", EndStation.RomaTer, "00:32", TrainType.LE,  3242, 14, 0),
            new TimetableItem("07:16", "08:03", EndStation.RomaTib, "00:47", TrainType.Reg, 22012, 8, 0),
            new TimetableItem("07:31", "08:18", EndStation.RomaTib, "00:47", TrainType.Reg, 22014, 8, 0),
            new TimetableItem("07:35", "08:07", EndStation.RomaTer, "00:32", TrainType.LE,  3246, 14, 0),
            new TimetableItem("07:46", "08:33", EndStation.RomaTib, "00:47", TrainType.Reg, 22016, 8, 0),
            new TimetableItem("08:01", "08:48", EndStation.RomaTib, "00:47", TrainType.Reg, 22018, 8, 0),
            new TimetableItem("08:05", "08:37", EndStation.RomaTer, "00:32", TrainType.LE,  3250, 14, 0),
            new TimetableItem("08:16", "09:03", EndStation.RomaTib, "00:47", TrainType.Reg, 22020, 8, 0),
            new TimetableItem("08:31", "09:18", EndStation.RomaTib, "00:47", TrainType.Reg, 22022, 8, 0),
            new TimetableItem("08:35", "09:07", EndStation.RomaTer, "00:32", TrainType.LE,  3254, 14, 0),
            new TimetableItem("08:46", "09:33", EndStation.RomaTib, "00:47", TrainType.Reg, 22024, 8, 0),
            new TimetableItem("08:50", "09:22", EndStation.RomaTer, "00:32", TrainType.LE,  3256, 14, 0),
            new TimetableItem("09:01", "09:48", EndStation.RomaTib, "00:47", TrainType.Reg, 22026, 8, 0),
            new TimetableItem("09:05", "09:37", EndStation.RomaTer, "00:32", TrainType.LE,  3258, 14, 0),
            new TimetableItem("09:16", "10:03", EndStation.RomaTib, "00:47", TrainType.Reg, 22028, 8, 0),
            new TimetableItem("09:20", "09:52", EndStation.RomaTer, "00:32", TrainType.FA,  9490, 18,21),
            new TimetableItem("09:31", "10:18", EndStation.RomaTib, "00:47", TrainType.Reg, 22030, 8, 0),
            new TimetableItem("09:35", "10:07", EndStation.RomaTer, "00:32", TrainType.LE,  3262, 14, 0),
            new TimetableItem("09:46", "10:33", EndStation.RomaTib, "00:47", TrainType.Reg, 22032, 8, 0),
            new TimetableItem("10:01", "10:48", EndStation.RomaTib, "00:47", TrainType.Reg, 22034, 8, 0),
            new TimetableItem("10:05", "10:37", EndStation.RomaTer, "00:32", TrainType.LE,  3266, 14, 0),
            new TimetableItem("10:16", "11:03", EndStation.RomaTib, "00:47", TrainType.Reg, 22036, 8, 0),
            new TimetableItem("10:31", "11:18", EndStation.RomaTib, "00:47", TrainType.Reg, 22038, 8, 0),
            new TimetableItem("10:35", "11:07", EndStation.RomaTer, "00:32", TrainType.LE,  3270, 14, 0),
            new TimetableItem("10:46", "11:33", EndStation.RomaTib, "00:47", TrainType.Reg, 22040, 8, 0),
            new TimetableItem("10:50", "11:22", EndStation.RomaTer, "00:32", TrainType.LE,  3272, 14, 0),
            new TimetableItem("11:01", "11:48", EndStation.RomaTib, "00:47", TrainType.Reg, 22042, 8, 0),
            new TimetableItem("11:05", "11:37", EndStation.RomaTer, "00:32", TrainType.LE,  3274, 14, 0),
            new TimetableItem("11:16", "12:03", EndStation.RomaTib, "00:47", TrainType.Reg, 22044, 8, 0),
            new TimetableItem("11:20", "11:52", EndStation.RomaTer, "00:32", TrainType.LE,  3268, 14, 0),
            new TimetableItem("11:31", "12:18", EndStation.RomaTib, "00:47", TrainType.Reg, 22046, 8, 0),
            new TimetableItem("11:35", "12:07", EndStation.RomaTer, "00:32", TrainType.LE,  3278, 14, 0),
            new TimetableItem("11:46", "12:33", EndStation.RomaTib, "00:47", TrainType.Reg, 22048, 8, 0),
            new TimetableItem("11:50", "12:22", EndStation.RomaTer, "00:32", TrainType.LE,  3280, 14, 0),
            new TimetableItem("12:01", "12:48", EndStation.RomaTib, "00:47", TrainType.Reg, 22050, 8, 0),
            new TimetableItem("12:05", "12:37", EndStation.RomaTer, "00:32", TrainType.LE,  3282, 14, 0),
            new TimetableItem("12:16", "13:03", EndStation.RomaTib, "00:47", TrainType.Reg, 22052, 8, 0),
            new TimetableItem("12:20", "12:52", EndStation.RomaTer, "00:32", TrainType.LE,  3284, 14, 0),
            new TimetableItem("12:31", "13:18", EndStation.RomaTib, "00:47", TrainType.Reg, 22054, 8, 0),
            new TimetableItem("12:35", "13:07", EndStation.RomaTer, "00:32", TrainType.LE,  3286, 14, 0),
            new TimetableItem("12:46", "13:33", EndStation.RomaTib, "00:47", TrainType.Reg, 22056, 8, 0),
            new TimetableItem("12:50", "13:22", EndStation.RomaTer, "00:32", TrainType.LE,  3288, 14, 0),
            new TimetableItem("13:01", "13:48", EndStation.RomaTib, "00:47", TrainType.Reg, 22058, 8, 0),
            new TimetableItem("13:05", "13:37", EndStation.RomaTer, "00:32", TrainType.LE,  3290, 14, 0),
            new TimetableItem("13:16", "14:03", EndStation.RomaTib, "00:47", TrainType.Reg, 22060, 8, 0),
            new TimetableItem("13:20", "13:52", EndStation.RomaTer, "00:32", TrainType.FA,  9490, 18,21),
            new TimetableItem("13:31", "14:18", EndStation.RomaTib, "00:47", TrainType.Reg, 22062, 8, 0),
            new TimetableItem("13:35", "14:07", EndStation.RomaTer, "00:32", TrainType.LE,  3294, 14, 0),
            new TimetableItem("13:46", "14:33", EndStation.RomaTib, "00:47", TrainType.Reg, 22064, 8, 0),
            new TimetableItem("14:01", "14:48", EndStation.RomaTib, "00:47", TrainType.Reg, 22066, 8, 0),
            new TimetableItem("14:05", "14:37", EndStation.RomaTer, "00:32", TrainType.LE,  3298, 14, 0),
            new TimetableItem("14:16", "15:03", EndStation.RomaTib, "00:47", TrainType.Reg, 22068, 8, 0),
            new TimetableItem("14:31", "15:18", EndStation.RomaTib, "00:47", TrainType.Reg, 22070, 8, 0),
            new TimetableItem("14:35", "15:07", EndStation.RomaTer, "00:32", TrainType.LE,  3302, 14, 0),
            new TimetableItem("14:46", "15:33", EndStation.RomaTib, "00:47", TrainType.Reg, 22072, 8, 0),
            new TimetableItem("14:50", "15:22", EndStation.RomaTer, "00:32", TrainType.LE,  3304, 14, 0),
            new TimetableItem("15:01", "15:48", EndStation.RomaTib, "00:47", TrainType.Reg, 22074, 8, 0),
            new TimetableItem("15:05", "15:37", EndStation.RomaTer, "00:32", TrainType.LE,  3306, 14, 0),
            new TimetableItem("15:16", "16:03", EndStation.RomaTib, "00:47", TrainType.Reg, 22076, 8, 0),
            new TimetableItem("15:20", "15:52", EndStation.RomaTer, "00:32", TrainType.LE,  3308, 14, 0),
            new TimetableItem("15:31", "16:18", EndStation.RomaTib, "00:47", TrainType.Reg, 22078, 8, 0),
            new TimetableItem("15:35", "16:07", EndStation.RomaTer, "00:32", TrainType.LE,  3310, 14, 0),
            new TimetableItem("15:46", "16:33", EndStation.RomaTib, "00:47", TrainType.Reg, 22080, 8, 0),
            new TimetableItem("15:50", "16:22", EndStation.RomaTer, "00:32", TrainType.LE,  3312, 14, 0),
            new TimetableItem("16:01", "16:48", EndStation.RomaTib, "00:47", TrainType.Reg, 22082, 8, 0),
            new TimetableItem("16:05", "16:37", EndStation.RomaTer, "00:32", TrainType.LE,  3314, 14, 0),
            new TimetableItem("16:16", "17:03", EndStation.RomaTib, "00:47", TrainType.Reg, 22084, 8, 0),
            new TimetableItem("16:20", "16:52", EndStation.RomaTer, "00:32", TrainType.LE,  3316, 14, 0),
            new TimetableItem("16:31", "17:18", EndStation.RomaTib, "00:47", TrainType.Reg, 22086, 8, 0),
            new TimetableItem("16:35", "17:07", EndStation.RomaTer, "00:32", TrainType.LE,  3318, 14, 0),
            new TimetableItem("16:46", "17:33", EndStation.RomaTib, "00:47", TrainType.Reg, 22088, 8, 0),
            new TimetableItem("16:50", "17:22", EndStation.RomaTer, "00:32", TrainType.LE,  3320, 14, 0),
            new TimetableItem("17:01", "17:48", EndStation.RomaTib, "00:47", TrainType.Reg, 22090, 8, 0),
            new TimetableItem("17:05", "17:37", EndStation.RomaTer, "00:32", TrainType.LE,  3322, 14, 0),
            new TimetableItem("17:16", "18:03", EndStation.RomaTib, "00:47", TrainType.Reg, 22092, 8, 0),
            new TimetableItem("17:20", "17:52", EndStation.RomaTer, "00:32", TrainType.LE,  3324, 14, 0),
            new TimetableItem("17:31", "18:18", EndStation.RomaTib, "00:47", TrainType.Reg, 22094, 8, 0),
            new TimetableItem("17:35", "18:07", EndStation.RomaTer, "00:32", TrainType.LE,  3326, 14, 0),
            new TimetableItem("17:46", "18:33", EndStation.RomaTib, "00:47", TrainType.Reg, 22096, 8, 0),
            new TimetableItem("17:50", "18:22", EndStation.RomaTer, "00:32", TrainType.LE,  3328, 14, 0),
            new TimetableItem("18:01", "18:48", EndStation.RomaTib, "00:47", TrainType.Reg, 22098, 8, 0),
            new TimetableItem("18:05", "18:37", EndStation.RomaTer, "00:32", TrainType.LE,  3330, 14, 0),
            new TimetableItem("18:16", "19:03", EndStation.RomaTib, "00:47", TrainType.Reg, 22100, 8, 0),
            new TimetableItem("18:20", "18:52", EndStation.RomaTer, "00:32", TrainType.LE,  3332, 14, 0),
            new TimetableItem("18:31", "19:18", EndStation.RomaTib, "00:47", TrainType.Reg, 22102, 8, 0),
            new TimetableItem("18:35", "19:07", EndStation.RomaTer, "00:32", TrainType.LE,  3334, 14, 0),
            new TimetableItem("18:46", "19:33", EndStation.RomaTib, "00:47", TrainType.Reg, 22104, 8, 0),
            new TimetableItem("18:50", "19:22", EndStation.RomaTer, "00:32", TrainType.LE,  3336, 14, 0),
            new TimetableItem("19:01", "19:48", EndStation.RomaTib, "00:47", TrainType.Reg, 22106, 8, 0),
            new TimetableItem("19:05", "19:37", EndStation.RomaTer, "00:32", TrainType.LE,  3338, 14, 0),
            new TimetableItem("19:16", "20:03", EndStation.RomaTib, "00:47", TrainType.Reg, 22108, 8, 0),
            new TimetableItem("19:20", "19:52", EndStation.RomaTer, "00:32", TrainType.LE,  3340, 14, 0),
            new TimetableItem("19:31", "20:18", EndStation.RomaTib, "00:47", TrainType.Reg, 22110, 8, 0),
            new TimetableItem("19:35", "20:07", EndStation.RomaTer, "00:32", TrainType.LE,  3342, 14, 0),
            new TimetableItem("20:01", "20:48", EndStation.RomaTib, "00:47", TrainType.Reg, 22112, 8, 0),
            new TimetableItem("20:05", "20:37", EndStation.RomaTer, "00:32", TrainType.LE,  3346, 14, 0),
            new TimetableItem("20:31", "21:18", EndStation.RomaTib, "00:47", TrainType.Reg, 22114, 8, 0),
            new TimetableItem("20:35", "21:07", EndStation.RomaTer, "00:32", TrainType.LE,  3350, 14, 0),
            new TimetableItem("21:01", "21:48", EndStation.RomaTib, "00:47", TrainType.Reg, 22116, 8, 0),
            new TimetableItem("21:05", "21:37", EndStation.RomaTer, "00:32", TrainType.LE,  3354, 14, 0),
            new TimetableItem("21:35", "22:07", EndStation.RomaTer, "00:32", TrainType.LE,  3358, 14, 0),
            new TimetableItem("21:46", "22:33", EndStation.RomaTib, "00:47", TrainType.Reg, 22118, 8, 0),
            new TimetableItem("22:01", "22:48", EndStation.RomaTib, "00:47", TrainType.Reg, 22120, 8, 0),
            new TimetableItem("22:05", "22:37", EndStation.RomaTer, "00:32", TrainType.LE,  3362, 14, 0),
            new TimetableItem("22:35", "23:07", EndStation.RomaTer, "00:32", TrainType.LE,  3366, 14, 0)
    };
    int cntToFCO = 0;
    static DateFormat dateFormat = null;
    static Time today = null;
    int mTimezoneOffsetSec = 0;

    TimetableData() {
        if(dateFormat==null){
            dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.ITALY);
            dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Rome"));
        }
        // find out which day is in Rome now and store it as today
        if(today==null) {
            today = new Time();
            today.setToNow();
//            Log.v(TAG, "Today/now:" + today.toString());
            TimeZone userTimezone = TimeZone.getDefault(),
                     romeTimezone = TimeZone.getTimeZone("Europe/Rome");
            mTimezoneOffsetSec = (userTimezone.getRawOffset() - romeTimezone.getRawOffset()) / 1000;
            if(mTimezoneOffsetSec != 0) {
                today.switchTimezone("Europe/Rome");    // today in Rome is this day
//                Log.v(TAG, "Switched: " + today.toString()+
//                        "  mTimezoneOffsetHour: " + (String.valueOf(mTimezoneOffsetSec/3600)));
                // TODO: move to pop-up 'Now in Rome' panel and update by timer
                Time now = new Time();
                now.setToNow();
//                Log.v(TAG, "Time in Rome: " + dateFormat.format(new Date(now.toMillis(false))));
                ////////////////////////////////////////////////////////////////
            }
            today.set(0, 0, 0, today.monthDay, today.month, today.year);   // today in Rome
        }
//        Log.v(TAG, "Today: " + today.toString());
        // count items and initialize directions accordingly
        for (TimetableItem item : itemsToRome) {
            item.setDateDirection(Direction.FCOToRome);
            cntToRome++;
        }
        for (TimetableItem item : itemsToFCO) {
            item.setDateDirection(Direction.RomeToFCO);
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