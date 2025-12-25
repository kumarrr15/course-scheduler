public class ScheduleEntry {
    private final String studentId;
    private final String scheduleNumber;
    private final String status;
    private final String semester;
    
    public ScheduleEntry(String studentId, String scheduleNumber, String status, String semester) {
        this.studentId = studentId;
        this.scheduleNumber = scheduleNumber;
        this.status = status;
        this.semester = semester;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public String getScheduleNumber() {
        return scheduleNumber;
    }
    
    public String getStatus() {
        return status;
    }
    
    public String getSemester() {
        return semester;
    }
}
