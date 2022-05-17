import java.util.*;

public class CourseSchedule {

    private List<Section> courseSections;

    public CourseSchedule(){
        courseSections = new ArrayList<>();
    }

    private CourseSchedule(List<Section> courseSections){
        this.courseSections = new ArrayList<>(courseSections);
    }

    public CourseSchedule deepCopy(){
        return new CourseSchedule(courseSections);
    }

    public List<Section> getCourseSections(){
        return new ArrayList<>(courseSections);
    }

    public void addCourseSection(Section section){
        courseSections.add(section);
    }

    public void removeLastSection(){
        courseSections.remove(courseSections.size() - 1);
    }

    public int getTotalUnits(){
        if(courseSections == null) return 0;
        int totalUnits = 0;

        for (Section section :
                courseSections) {
            totalUnits += section.getUnits();
        }
        return totalUnits;
    }

    // Sort schedules from most to least units
    public static void quickSort(List<CourseSchedule> list){
        quickSort(list, 0, list.size() - 1);
    }

    private static void quickSort(List<CourseSchedule> list, int low, int high){

        if(low >= high)
            return;

        int pi = partition(list, low, high);

        quickSort(list, low, pi - 1);
        quickSort(list, pi + 1, high);
    }

    private static int partition(List<CourseSchedule> list, int low, int high){
        int pivot = list.get(high).getTotalUnits();
        int i = low;
        int j = high;

        while(i < j){
            while(list.get(i).getTotalUnits() >= pivot && i < j)
                i++;

            while(list.get(j).getTotalUnits() <= pivot && i < j)
                j--;

            if(i < j)
                Collections.swap(list, i, j);
        }
        Collections.swap(list, i, high);
        return i;
    }

    @Override
    public String toString(){
        if(this.courseSections == null)
            throw new IllegalStateException("courseSections has not been defined.");
        String str = "";
        for (Section section :
                courseSections) {
            str += section.getCourseName();
            str += " (" + section.getUnits() + " units)";
            str += section + "\n";
        }
        str += "Total units: " + getTotalUnits();
        return str;
    }
}
