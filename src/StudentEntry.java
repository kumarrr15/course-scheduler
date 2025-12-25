public class StudentEntry {
    private final String studentId;
    private final String fullName;
    private final String password;
    
    public StudentEntry(String studentId, String fullName, String password) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.password = password;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public String getPassword() {
        return password;
    }
}
