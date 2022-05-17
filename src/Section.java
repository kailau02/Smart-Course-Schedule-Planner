import java.util.ArrayList;
import java.util.List;

public class Section {
    private String courseName;
    private int units;
    private String sectionID;
    private List<TimeSchedule> schedules;

    public Section(String sectionID, String courseName, int units){
        this.courseName = courseName;
        this.units = units;
        this.sectionID = sectionID.toUpperCase();
        this.schedules = new ArrayList<>();
    }

    public String getCourseName(){
        return courseName;
    }

    public int getUnits(){
        return units;
    }

    public void addSchedule(TimeSchedule schedule){
        schedules.add(schedule);
    }

    @Override
    public String toString(){
        String message = "Section " + sectionID + ":";
        for(int i = 0; i < schedules.size(); i++){
            message += " [" + schedules.get(i) + "]";
        }
        return message;
    }

    public boolean isComplete() {
        if(schedules.size() == 0) return false;
        for(int i = 0; i < schedules.size(); i++){
            if(!schedules.get(i).isComplete()) return false;
        }

        return true;
    }

    // check if this section's time schedule conflicts with another
    public boolean conflictsWith(Section courseSection){
        for(int i = 0; i < schedules.size(); i++){
            for(int j = 0; j < courseSection.schedules.size(); j++){
                if(schedules.get(i).conflictsWith(courseSection.schedules.get(j)))
                    return true;
            }
        }
        return false;
    }

    // check if this section's time schedule conflicts with a list of others'
    public boolean conflictsWith(List<Section> courseSections){
        for (Section other :
                courseSections) {
            if(this.conflictsWith(other)) return true;
        }
        return false;
    }
}
