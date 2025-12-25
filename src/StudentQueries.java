import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class StudentQueries {
    private static Connection connection;
    private static PreparedStatement addStudent;
    private static PreparedStatement getStudentList;
    private static PreparedStatement scheduleStudent;
    private static PreparedStatement getScheduledStudentCount;
    private static PreparedStatement getStudentSchedule;
    private static ResultSet resultSet;
    private static PreparedStatement dropStudent;
    private static PreparedStatement dropStudentFromAllSchedules;
    private static PreparedStatement getStudentScheduleAllSemesters;
    private static PreparedStatement dropStudentFromClass;
    private static PreparedStatement getFirstWaitlistedStudent;
    
    public static void dropStudent(String studentID) {
        connection = DBConnection.getConnection();
        try {
            dropStudent = connection.prepareStatement("DELETE FROM STUDENT WHERE STUDENTID = ?");
            dropStudent.setString(1, studentID);
            dropStudent.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
    
    public static void dropStudentFromAllSchedules(String studentID) {
        connection = DBConnection.getConnection();
        try {
            dropStudentFromAllSchedules = connection.prepareStatement("DELETE FROM SCHEDULE WHERE STUDENTID = ?");
            dropStudentFromAllSchedules.setString(1, studentID);
            dropStudentFromAllSchedules.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
    
    public static ArrayList<ScheduleEntry> getStudentScheduleAllSemesters(String studentID) {
        connection = DBConnection.getConnection();
        ArrayList<ScheduleEntry> scheduleList = new ArrayList<>();
        try {
            getStudentScheduleAllSemesters = connection.prepareStatement(
                "SELECT SCHEDULE.SEMESTER, SCHEDULE.COURSECODE, COURSES.DESCRIPTION, SCHEDULE.STATUS " +
                "FROM SCHEDULE INNER JOIN COURSES ON SCHEDULE.COURSECODE = COURSES.COURSECODE " +
                "WHERE SCHEDULE.STUDENTID = ? ORDER BY SCHEDULE.SEMESTER, SCHEDULE.COURSECODE"
            );
            getStudentScheduleAllSemesters.setString(1, studentID);
            resultSet = getStudentScheduleAllSemesters.executeQuery();
            while (resultSet.next()) {
                ScheduleEntry entry = new ScheduleEntry(
                    resultSet.getString(1) + "-" + resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
                );
                scheduleList.add(entry);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return scheduleList;
    }
    
    public static void dropStudentFromClass(String semester, String courseCode, String studentID) {
        connection = DBConnection.getConnection();
        try {
            dropStudentFromClass = connection.prepareStatement(
                "DELETE FROM SCHEDULE WHERE SEMESTER = ? AND COURSECODE = ? AND STUDENTID = ?"
            );
            dropStudentFromClass.setString(1, semester);
            dropStudentFromClass.setString(2, courseCode);
            dropStudentFromClass.setString(3, studentID);
            dropStudentFromClass.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
    
    public static StudentEntry getFirstWaitlistedStudent(String semester, String courseCode) {
        connection = DBConnection.getConnection();
        StudentEntry student = null;
        try {
            getFirstWaitlistedStudent = connection.prepareStatement(
                "SELECT STUDENT.STUDENTID, STUDENT.FIRSTNAME, STUDENT.LASTNAME " +
                "FROM SCHEDULE INNER JOIN STUDENT ON SCHEDULE.STUDENTID = STUDENT.STUDENTID " +
                "WHERE SCHEDULE.SEMESTER = ? AND SCHEDULE.COURSECODE = ? AND SCHEDULE.STATUS = 'W' " +
                "ORDER BY SCHEDULE.TIMESTAMP ASC FETCH FIRST 1 ROW ONLY"
            );
            getFirstWaitlistedStudent.setString(1, semester);
            getFirstWaitlistedStudent.setString(2, courseCode);
            resultSet = getFirstWaitlistedStudent.executeQuery();
            if (resultSet.next()) {
                student = new StudentEntry(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3)
                );
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return student;
    }
    
    public static void updateStudentStatus(String semester, String courseCode, String studentID, String newStatus) {
        connection = DBConnection.getConnection();
        try {
            PreparedStatement updateStatus = connection.prepareStatement(
                "UPDATE SCHEDULE SET STATUS = ? WHERE SEMESTER = ? AND COURSECODE = ? AND STUDENTID = ?"
            );
            updateStatus.setString(1, newStatus);
            updateStatus.setString(2, semester);
            updateStatus.setString(3, courseCode);
            updateStatus.setString(4, studentID);
            updateStatus.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
    
    public static ArrayList<ScheduleEntry> dropAllStudentsFromClass(String semester, String courseCode) {
        connection = DBConnection.getConnection();
        ArrayList<ScheduleEntry> droppedStudents = new ArrayList<>();
        droppedStudents = ClassQueries.getStudentsInClass(semester, courseCode);
        try {
            PreparedStatement dropAll = connection.prepareStatement(
                "DELETE FROM SCHEDULE WHERE SEMESTER = ? AND COURSECODE = ?"
            );
            dropAll.setString(1, semester);
            dropAll.setString(2, courseCode);
            dropAll.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return droppedStudents;
    }
    
    public static void addStudent(String studentID, String firstName, String lastName) {
        connection = DBConnection.getConnection();
        try {
            addStudent = connection.prepareStatement(
                "INSERT INTO STUDENT (STUDENTID, FIRSTNAME, LASTNAME) VALUES (?, ?, ?)"
            );
            addStudent.setString(1, studentID);
            addStudent.setString(2, firstName);
            addStudent.setString(3, lastName);
            addStudent.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
    
    public static ArrayList<StudentEntry> getStudentList() {
        connection = DBConnection.getConnection();
        ArrayList<StudentEntry> studentList = new ArrayList<>();
        try {
            getStudentList = connection.prepareStatement(
                "SELECT STUDENTID, FIRSTNAME, LASTNAME FROM STUDENT ORDER BY LASTNAME"
            );
            resultSet = getStudentList.executeQuery();
            while (resultSet.next()) {
                StudentEntry student = new StudentEntry(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3)
                );
                studentList.add(student);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return studentList;
    }
    
    public static void scheduleStudent(String semester, String courseCode, String studentID, String status) {
        connection = DBConnection.getConnection();
        try {
            scheduleStudent = connection.prepareStatement(
                "INSERT INTO SCHEDULE (SEMESTER, COURSECODE, STUDENTID, STATUS, TIMESTAMP) VALUES (?, ?, ?, ?, ?)"
            );
            scheduleStudent.setString(1, semester);
            scheduleStudent.setString(2, courseCode);
            scheduleStudent.setString(3, studentID);
            scheduleStudent.setString(4, status);
            scheduleStudent.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            scheduleStudent.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
    
    public static int getScheduledStudentCount(String semester, String courseCode) {
        connection = DBConnection.getConnection();
        int count = 0;
        try {
            getScheduledStudentCount = connection.prepareStatement(
                "SELECT COUNT(*) FROM SCHEDULE WHERE SEMESTER = ? AND COURSECODE = ? AND STATUS = 'S'"
            );
            getScheduledStudentCount.setString(1, semester);
            getScheduledStudentCount.setString(2, courseCode);
            resultSet = getScheduledStudentCount.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return count;
    }
    
    public static ArrayList<ScheduleEntry> getStudentSchedule(String semester, String studentID) {
        connection = DBConnection.getConnection();
        ArrayList<ScheduleEntry> scheduleList = new ArrayList<>();
        try {
            getStudentSchedule = connection.prepareStatement(
                "SELECT SCHEDULE.COURSECODE, COURSE.DESCRIPTION, SCHEDULE.STATUS " +
                "FROM SCHEDULE INNER JOIN COURSE ON SCHEDULE.COURSECODE = COURSE.COURSECODE " +
                "WHERE SCHEDULE.SEMESTER = ? AND SCHEDULE.STUDENTID = ? ORDER BY SCHEDULE.COURSECODE"
            );
            getStudentSchedule.setString(1, semester);
            getStudentSchedule.setString(2, studentID);
            resultSet = getStudentSchedule.executeQuery();
            while (resultSet.next()) {
                ScheduleEntry entry = new ScheduleEntry(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3)
                );
                scheduleList.add(entry);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return scheduleList;
    }
}
