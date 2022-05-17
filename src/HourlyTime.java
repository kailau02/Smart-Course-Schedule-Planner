import java.util.regex.Pattern;

public class HourlyTime implements Comparable<HourlyTime> {
    private static final int MINUTES_PER_DAY = 1440;
    private int minutes;

    public HourlyTime(int minutes){
        this.minutes = minutes;
    }

    // Print the required format a parseable string
    public static void printFormatRequirement(){
        System.out.println("Enter time in one format:\n" +
                "TT:TT<A/P>M\n" +
                " T:TT<A/P>M\n" +
                "   TT<A/P>M");
    }

    // Parse a string into minutes
    public static HourlyTime parseTime(String str){
        // Try to parse the string
        try{
            if(!Pattern.matches("[0-9]?[0-9]:[0-9]{2}[apAP]m", str)
                    && !Pattern.matches("[0-9]?[0-9][apAP]m", str))
                throw new IllegalArgumentException("Invalid time format");

            // convert time to integer representing minutes
            char timeChar = str.charAt(str.length() - 2);
            boolean isAm = (timeChar == 'a' || timeChar == 'A');
            int hour;
            int minute;

            if(str.length() > 4){
                if(str.length() == 7){
                    hour = Integer.parseInt(str.substring(0, 2));
                    minute = Integer.parseInt(str.substring(3,5));
                }
                else{
                    hour = Character.getNumericValue(str.charAt(0));
                    minute = Integer.parseInt(str.substring(2,4));
                }
            }
            else{
                if(str.length() == 4)
                    hour = Integer.parseInt(str.substring(0, 2));
                else
                    hour = Character.getNumericValue(str.charAt(0));
                minute = 0;
            }

            // convert parsed integers into minute time
            if(hour == 12 && isAm) {
                hour = 0;
            }

            if(hour > 12 || minute > 59)
                throw new IllegalArgumentException("Invalid time string");

            if(!isAm && hour != 12)
                hour += 12;
            int minutes = hour * 60 + minute;

            if(minutes > MINUTES_PER_DAY)
                throw new IllegalArgumentException("Minutes cannot exceed daily maximum");

            return new HourlyTime(minutes);
        }
        // If failed, print format requirement and return null
        catch (Exception e) {
            printFormatRequirement();
            return null;
        }
    }

    @Override
    public String toString(){
        boolean isAM = true;
        int minute = minutes % 60;
        int hour = minutes / 60;
        if(hour >= 12){
            hour -= 12;
            isAM = false;
        }
        if(hour == 0)
            hour = 12;
        return "" + hour + ":" + (minute == 0 ? "00" : minute) + (isAM ? "AM" : "PM");
    }

    @Override
    public int compareTo(HourlyTime o) {
        return this.minutes - o.minutes;
    }

    @Override
    public boolean equals(Object object){
        if(this == object)
            return true;
        if(object == null || object.getClass() != getClass())
            return false;
        HourlyTime other = (HourlyTime) object;
        return minutes == other.minutes;
    }
}
