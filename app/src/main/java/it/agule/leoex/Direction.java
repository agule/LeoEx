package it.agule.leoex;

/**
 * Actual Timetable data and related types defined here
 */

// direction: from FCO Airport to Rome city and back
public enum Direction{ FCOToRome("Rome"), RomeToFCO("✈Fiumicino");
    public static int COUNT = 2;
    private String name;
    Direction(String toName){name = toName;}
    public String Name(){return name;}
}
