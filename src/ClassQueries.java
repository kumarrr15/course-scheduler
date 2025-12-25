import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClassQueries {
    private static Connection connection;
    private static PreparedStatement addClass;
    private static PreparedStatement getClassList;
    private static PreparedStatement getClassSeats;
    private static PreparedStatement updateClassSeats;
    private static ResultSet resultSet;
    private static PreparedStatement dropClass;
    private static PreparedStatement getStudentsInClass;
    
    public static void addClass(String semester, String courseCode, int seats) {
        connection = DBConnection.getConnection();
        try {
            addClass = connection.prepareStatement(
                "INSERT INTO CLASS (SEMESTER, COURSECODE, SEATS) VALUES (?, ?, ?)"
            );
            addClass.setString(1, semester);
            addClass.setString(2, courseCode);
            addClass.setInt(3, seats);
            addClass.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
    
    public static void dropClass(String semester, String courseCode) {
        connection = DBConnection.getConnection();
        try {
            dropClass = connection.prepareStatement(
                "DELETE FROM CLASS WHERE SEMESTER = ? AND COURSECODE = ?"
            );
            dropClass.setString(1, semester);
            dropClass.setString(2, courseCode);
            dropClass.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
    
    public static ArrayList<ScheduleEntry> getStudentsInClass(String semester, String courseCode) {
        connection = DBConnection.getConnection();
        ArrayList<ScheduleEntry> studentList = new ArrayList<>();
        try {
            getStudentsInClass = connection.prepareStatement(
                "SELECT STUDENT.STUDENTID, STUDENT.FIRSTNAME, STUDENT.LASTNAME, SCHEDULE.STATUS, SCHEDULE.TIMESTAMP " +
                "FROM SCHEDULE INNER JOIN STUDENT ON SCHEDULE.STUDENTID = STUDENT.STUDENTID " +
                "WHERE SCHEDULE.SEMESTER = ? AND SCHEDULE.COURSECODE = ? " +
                "ORDER BY SCHEDULE.STATUS DESC, SCHEDULE.TIMESTAMP ASC"
            );
            getStudentsInClass.setString(1, semester);
            getStudentsInClass.setString(2, courseCode);
            resultSet = getStudentsInClass.executeQuery();
            while (resultSet.next()) {
                String studentInfo = resultSet.getString(1) + " - " +
                                   resultSet.getString(2) + " " +
                                   resultSet.getString(3);
                String status = resultSet.getString(4);
                ScheduleEntry entry = new ScheduleEntry(courseCode, studentInfo, status);
                studentList.add(entry);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return studentList;
    }
    
    public static ArrayList<ClassEntry> getClassList(String semester) {
        connection = DBConnection.getConnection();
        ArrayList<ClassEntry> classList = new ArrayList<>();
        try {
            getClassList = connection.prepareStatement(
                "SELECT COURSE.COURSECODE, COURSE.DESCRIPTION, CLASS.SEATS " +
                "FROM CLASS INNER JOIN COURSE ON CLASS.COURSECODE = COURSE.COURSECODE " +
                "WHERE CLASS.SEMESTER = ? ORDER BY COURSE.COURSECODE"
            );
            getClassList.setString(1, semester);
            resultSet = getClassList.executeQuery();
            while (resultSet.next()) {
                ClassEntry classEntry = new ClassEntry(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3)
                );
                classList.add(classEntry);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return classList;
    }
    
    public static int getClassSeats(String semester, String courseCode) {
        connection = DBConnection.getConnection();
        int seats = 0;
        try {
            getClassSeats = connection.prepareStatement(
                "SELECT SEATS FROM CLASS WHERE SEMESTER = ? AND COURSECODE = ?"
            );
            getClassSeats.setString(1, semester);
            getClassSeats.setString(2, courseCode);
            resultSet = getClassSeats.executeQuery();
            if (resultSet.next()) {
                seats = resultSet.getInt(1);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return seats;
    }
    
    public static void updateClassSeats(String semester, String courseCode, int seats) {
        connection = DBConnection.getConnection();
        try {
            updateClassSeats = connection.prepareStatement(
                "UPDATE CLASS SET SEATS = ? WHERE SEMESTER = ? AND COURSECODE = ?"
            );
            updateClassSeats.setInt(1, seats);
            updateClassSeats.setString(2, semester);
            updateClassSeats.setString(3, courseCode);
            updateClassSeats.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
