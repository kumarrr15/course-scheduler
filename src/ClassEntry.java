public class ClassEntry {
    private final String scheduleNumber;
    private final String courseCode;
    private final String semester;
    private final int seats;
    
    public ClassEntry(String scheduleNumber, String courseCode, String semester, int seats) {
        this.scheduleNumber = scheduleNumber;
        this.courseCode = courseCode;
        this.semester = semester;
        this.seats = seats;
    }
    
    public String getScheduleNumber() {
        return scheduleNumber;
    }
    
    public String getCourseCode() {
        return courseCode;
    }
    
    public String getSemester() {
        return semester;
    }
    
    public int getSeats() {
        return seats;
    }
}
