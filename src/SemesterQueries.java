import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * SemesterQueries - Database queries for Semester operations
 * @author acv
 */
public class SemesterQueries {
    
    private static Connection connection;
    private static PreparedStatement addSemester;
    private static PreparedStatement getSemesterList;
    private static ResultSet resultSet;
    
    // Add a new semester to the database
    public static void addSemester(SemesterEntry semester) {
        connection = DBConnection.getConnection();
        try {
            addSemester = connection.prepareStatement(
                "INSERT INTO SEMESTER (SEMESTERTERM, SEMESTERYEAR) VALUES (?, ?)"
            );
            addSemester.setString(1, semester.getTerm());
            addSemester.setString(2, semester.getYear());
            addSemester.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
    
    // Get list of all semesters
    public static ArrayList<SemesterEntry> getSemesterList() {
        connection = DBConnection.getConnection();
        ArrayList<SemesterEntry> semesterList = new ArrayList<>();
        
        try {
            getSemesterList = connection.prepareStatement(
                "SELECT SEMESTERTERM, SEMESTERYEAR FROM SEMESTER ORDER BY SEMESTERYEAR"
            );
            resultSet = getSemesterList.executeQuery();
            
            while (resultSet.next()) {
                SemesterEntry semester = new SemesterEntry(
                    resultSet.getString(1),
                    resultSet.getString(2)
                );
                semesterList.add(semester);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        
        return semesterList;
    }
}
