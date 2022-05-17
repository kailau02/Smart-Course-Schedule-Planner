import java.util.ArrayList;
import java.util.List;

public class TimeSchedule {

    public enum Weekdays{
        M,
        T,
        W,
        R,
        F,
        SA,
        SU
    }

    public List<Weekdays> weekdays;
    public HourlyTime startTime;
    public HourlyTime endTime;

    public TimeSchedule(List<Weekdays> weekdays, HourlyTime startTime, HourlyTime endTime){
        this.weekdays = weekdays;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public boolean isComplete() {
        return weekdays.size() != 0;
    }

    public static List<Weekdays> parseWeekdays(String s){
        String s_clean = s.replace(" ", "").toLowerCase();
        String[] s_days = s_clean.split(",");
        List<Weekdays> weekdays = new ArrayList<>();
        for(String s_day : s_days){
            switch (s_day){
                case "m":
                    weekdays.add(Weekdays.M);
                    break;
                case "t":
                    weekdays.add(Weekdays.T);
                    break;
                case "w":
                    weekdays.add(Weekdays.W);
                    break;
                case "r":
                    weekdays.add(Weekdays.R);
                    break;
                case "f":
                    weekdays.add(Weekdays.F);
                    break;
                case "sa":
                    weekdays.add(Weekdays.SA);
                    break;
                case "su":
                    weekdays.add(Weekdays.SU);
                    break;
                default:
                    return null;
            }
        }
        return weekdays;
    }

    // Check if a time schedule conflicts with another time schedule
    public boolean conflictsWith(TimeSchedule otherSchedule){
        for(int i = 0; i < weekdays.size(); i++){
            for(int j = 0; j < otherSchedule.weekdays.size(); j++){
                Weekdays thisDay = weekdays.get(i);
                Weekdays otherDay = otherSchedule.weekdays.get(j);
                if(thisDay == otherDay &&
                   startTime.compareTo(otherSchedule.endTime) < 0 &&
                   otherSchedule.startTime.compareTo(endTime) < 0) return true;
            }
        }
        return false;
    }

    @Override
    public String toString(){
        String message = "";
        for(int i = 0; i < weekdays.size(); i++){
            message += weekdays.get(i).toString();
            if(i < weekdays.size() - 1) message += "/";
        }
        message += " " + startTime + "-" + endTime;
        return message;
    }
}
