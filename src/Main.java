import java.util.*;
import java.util.regex.Pattern;
// Purpose:
// Given a list of sections of courses,
// find a schedule that fits all the classes

public class Main {

    private enum UserAction {
        END,
        ADD_NEXT,
        DELETE,
        SHOW
    }


    // Main high level method calls to run full program
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // Show greeting message
        greetUser();

        // Get input for the courses to consider
        List<CourseInfo> courses = getUserCourses(in);

        // Process the input to find possible schedules
        List<CourseSchedule> courseSchedules = findCourseSchedules(in, courses);

        // Output the possible schedules
        printCourseSchedules(courseSchedules);

    }

    // PART 0
    // Print the welcome message
    private static void greetUser(){
        System.out.println("Welcome to the smart schedule planner.\n" +
                "Here you will enter all courses you plan to take,\n" +
                "the available sections of these courses,\n" +
                "and the days and times these sections meet.\n" +
                "Then you will be displayed with the best possible\n" +
                "combinations of course sections you can take together.\n" +
                "It is not possible to correct mistakes so make sure to\n" +
                "double check all values before inputting them.\n");
    }

    // PART 1
    // Get user input of courses in consideration and store them in a Course List to return
    private static List<CourseInfo> getUserCourses(Scanner in){
        // List to store all user courses
        List<CourseInfo> courses = new ArrayList<>();

        while(true){ // Get Courses
            CourseInfo courseInfo = getCourseFromInput(in);
            if(courseInfo == null) break;

            while(true){ // Get Sections for Course
                Section section = getSectionFromInput(in, courseInfo);
                if(section == null) break;
                courseInfo.addSection(section);

                while(true){ // Get Schedules of Section
                    TimeSchedule schedule = getScheduleFromInput(in);
                    if(schedule == null) break;
                    section.addSchedule(schedule);
                }
            }
            // Check that course is fully filled-out
            if(!courseInfo.isComplete()){
                printIncompleteCourseError();
                continue;
            }

            UserAction userAction = getEndAction(in, courseInfo);

            while(userAction == UserAction.SHOW){
                System.out.println(courses);
                System.out.println("Press enter to continue...");
                in.nextLine();
                userAction = getEndAction(in, courseInfo);
            }

            if(userAction == UserAction.END){
                courses.add(courseInfo);
                break;
            }
            if(userAction == UserAction.ADD_NEXT){
                courses.add(courseInfo);
                continue;
            }
            if(userAction == UserAction.DELETE){
                continue;
            }
        }

        return courses;
    }

    // Prompt the user to get the name of a course
    private static CourseInfo getCourseFromInput(Scanner in) {
        // Get name of course
        System.out.println("What is the course name?\n(type 'finish' if finished adding courses)");
        String courseName = in.nextLine().trim().toUpperCase();

        if(courseName.equalsIgnoreCase("finish")) return null;

        // Error handle invalid course name
        while(!Pattern.matches("^.*[a-zA-Z]+.*$", courseName)){
            System.out.println("Invalid course name, try again...");
            courseName = in.nextLine().trim();
        }

        // Get units of course
        System.out.println("How many units is " + courseName + " worth?");
        int units = getUnitsInput(in);

        return new CourseInfo(units, courseName);
    }

    // Prompt the user to get the section of a course
    private static Section getSectionFromInput(Scanner in, CourseInfo courseInfo) {
        System.out.println("Enter a section ID for " + courseInfo.getCourseName() + " (you can add more later).\n(type 'done' if completed)");
        String sectionID = in.nextLine().trim();
        if(sectionID.equalsIgnoreCase("done")) return null;

        while(!Pattern.matches(".*[a-zA-Z].*", sectionID)){
            System.out.println("Invalid section ID, try again...");
            sectionID = in.nextLine().trim();
        }
        return new Section(sectionID, courseInfo.getCourseName(), courseInfo.getUnits());
    }

    // Prompt the user to get the time schedule of a course section
    private static TimeSchedule getScheduleFromInput(Scanner in) {
        // Get the days to meet
        System.out.println("Enter days of the week this class meets at a specific time separated by commas\n" +
                "(ex: M,T,W,R,F,SA,SU).\n" +
                "Note: You can add additional dates with different meeting hours later as needed.\n" +
                "(type 'done' if completed)");
        String days = in.nextLine();
        if(days.equalsIgnoreCase("done")) return null;
        List<TimeSchedule.Weekdays> weekdays = TimeSchedule.parseWeekdays(days);

        while(weekdays == null){
            System.out.println("Invalid weekday format, try again...");
            days = in.nextLine();
            weekdays = TimeSchedule.parseWeekdays(days);
        }

        // Get start time
        System.out.println("Enter the start time on " + days + " in the format:\n" +
                "TT:TT<A/P>M\n" +
                " T:TT<A/P>M\n" +
                "   TT<A/P>M");
        String startTimeStr = in.nextLine().trim();
        HourlyTime startTime = HourlyTime.parseTime(startTimeStr);
        while(startTime == null){
            System.out.println("Invalid time, try again...");
            startTimeStr = in.nextLine().trim();
            startTime = HourlyTime.parseTime(startTimeStr);
        }

        // Get end time
        System.out.println("Enter the end time on " + days + " in the format: HH:MM<a/p>m or H:MM<a/p>m");
        String endTimeStr = in.nextLine().trim();
        HourlyTime endTime = HourlyTime.parseTime(endTimeStr);
        while(endTime == null){
            System.out.println("Invalid time, try again...");
            endTimeStr = in.nextLine().trim();
            endTime = HourlyTime.parseTime(endTimeStr);
        }

        return new TimeSchedule(weekdays, startTime, endTime);
    }

    // Prompt the user to choose an action after a course has been fully filled out
    private static UserAction getEndAction(Scanner in, CourseInfo courseInfo) {
        System.out.println("Current course info:\n" + courseInfo +"\n" +
                "Actions:\n" +
                "done - Add this course to the planner and continue adding courses\n" +
                "delete - Delete this course and continue adding courses\n" +
                "finish - Finish adding courses and proceed to finding available schedules\n" +
                "show - Show current courses not including the current course");
        String action = in.nextLine().trim();
        if(action.equalsIgnoreCase("done"))
            return UserAction.ADD_NEXT;
        if(action.equalsIgnoreCase("delete"))
            return UserAction.DELETE;
        if(action.equalsIgnoreCase("finish"))
            return UserAction.END;
        if(action.equalsIgnoreCase("show"))
            return UserAction.SHOW;
        // Invalid user input was given
        System.out.println("Invalid command, try again...");
        return getEndAction(in, courseInfo);
    }

    // Print an error message to the user that their course was not fully filled out, an unexpected state
    private static void printIncompleteCourseError() {
        System.out.println("Error, the current course is not entirely filled out." +
                "This course will not be added to the list of courses in consideration.");
    }

    // PART 2
    // Given a List of Course's, determine all schedules that meet minimum course unit requirements
    private static List<CourseSchedule> findCourseSchedules(Scanner in, List<CourseInfo> courses) {
        // Get minimum number of units to take
        int minUnits = getMinimumScheduleUnits(in);
        // Perform recursive backtracking to find the best schedules
        List<CourseSchedule> schedules = findOptimalSchedules(courses, minUnits);
        // Return discovered schedules
        CourseSchedule.quickSort(schedules);
        return schedules;
    }

    private static int getMinimumScheduleUnits(Scanner in){
        // Get units of course
        System.out.println("What is the minimum number of units you are willing to take?");
        return getUnitsInput(in);
    }

    private static int getUnitsInput(Scanner in){
        String unitsStr = in.nextLine().trim();

        // Error handle invalid units
        while(!Pattern.matches(".*[0-9].*", unitsStr) || Integer.parseInt(unitsStr) < 0){
            System.out.println("Invalid number of units, try again...");
            unitsStr = in.nextLine().trim();
        }

        int units = Integer.parseInt(unitsStr);
        return units;
    }

    private static List<CourseSchedule> findOptimalSchedules(List<CourseInfo> possibleCourses, int minUnits){
        List<CourseSchedule> optimalSchedules = new ArrayList<>();
        findOptimalSchedules(possibleCourses, optimalSchedules, new CourseSchedule(),
                minUnits, 0, false);
        return optimalSchedules;
    }

    private static void findOptimalSchedules(
            List<CourseInfo> desiredCourses, List<CourseSchedule> availableSchedules,
            CourseSchedule currSchedule, int minUnits, int courseIndex, boolean currScheduleIsUnique){

        // BASE CASE: try adding schedule if it is unique and minimum units is fulfilled
        if(currScheduleIsUnique && currSchedule.getTotalUnits() >= minUnits)
            availableSchedules.add(currSchedule.deepCopy());

        // BASE CASE: end of courses index
        if(courseIndex == desiredCourses.size())
            return;

        // RECURSIVE CASE:

        // Check if course sections at `courseIndex` of `desiredCourses` can be added to `currSchedule`
        List<Section> possibleSections = desiredCourses.get(courseIndex).getSections();
        for(int j = 0; j < possibleSections.size(); j++){
            // if there is no time conflict with this section, add it to current schedule
            Section possibleSection = possibleSections.get(j);
            if(!possibleSection.conflictsWith(currSchedule.getCourseSections())){
                currSchedule.addCourseSection(possibleSection);
                // add and continue backtracking
                findOptimalSchedules(desiredCourses, availableSchedules, currSchedule,
                        minUnits, courseIndex + 1, true);
                // remove course section afterwards
                currSchedule.removeLastSection();
            }
        }
        // search for next possible schedule not including this course
        findOptimalSchedules(desiredCourses, availableSchedules, currSchedule,
                minUnits, courseIndex + 1, false);
    }

    // PART 3
    // Print a list of CourseSchedule's
    private static void printCourseSchedules(List<CourseSchedule> courseSchedules){

        for(int i = 0; i < courseSchedules.size(); i++){
            System.out.println("\nSchedule #" + (i + 1) + ":");
            System.out.println(courseSchedules.get(i));
        }
    }
}

