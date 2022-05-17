import java.util.ArrayList;
import java.util.List;

public class CourseInfo {
    private int units;
    private String courseName;
    private List<Section> sections;

    public CourseInfo(int units, String courseName){
        this.sections = new ArrayList<>();
        this.courseName = courseName.toUpperCase();
        this.units = units;
    }

    public int getUnits() { return units; }

    public void setCourseName(String courseName){
        this.courseName = courseName.toUpperCase();
    }

    public String getCourseName(){
        return this.courseName;
    }

    public void addSection(Section section){
        sections.add(section);
    }

    public List<Section> getSections(){
        return new ArrayList<>(sections);
    }

    @Override
    public String toString(){
        String message = "\n" + courseName + " (" + units + " units" + ")-------------------\n";
        for(int i = 0; i < sections.size(); i++){
            message += sections.get(i) + "\n";
        }
        return message;
    }

    public boolean isComplete() {
        if(sections.size() == 0) return false;
        for(int i = 0; i < sections.size(); i++){
            if(!sections.get(i).isComplete()) return false;
        }

        return true;
    }
}
