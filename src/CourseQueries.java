import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CourseQueries {
    private static Connection connection;
    private static PreparedStatement addCourse;
    private static PreparedStatement getCourseList;
    private static ResultSet resultSet;
    
    // Add a new course to the database
    public static void addCourse(String courseCode, String description) {
        connection = DBConnection.getConnection();
        try {
            addCourse = connection.prepareStatement(
                "INSERT INTO COURSE (COURSECODE, DESCRIPTION) VALUES (?, ?)"
            );
            addCourse.setString(1, courseCode);
            addCourse.setString(2, description);
            addCourse.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
    
    // Get list of all courses for combo box
    public static ArrayList<CourseEntry> getCourseList() {
        connection = DBConnection.getConnection();
        ArrayList<CourseEntry> courseList = new ArrayList<>();
        try {
            getCourseList = connection.prepareStatement(
                "SELECT COURSECODE, DESCRIPTION FROM COURSE ORDER BY COURSECODE"
            );
            resultSet = getCourseList.executeQuery();
            while (resultSet.next()) {
                CourseEntry course = new CourseEntry(
                    resultSet.getString(1),
                    resultSet.getString(2)
                );
                courseList.add(course);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return courseList;
    }
}
