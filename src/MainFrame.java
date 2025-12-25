import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import javax.swing.JOptionPane;

/**
 * MainFrame - GUI for Course Scheduler Application
 * @author acv
 */
public class MainFrame extends javax.swing.JFrame {
    
    private String currentSemester;
    private String author;
    private String projectDate;
    
    public MainFrame() {
        initComponents();
        checkData();
        rebuildSemesterComboBoxes();
        rebuildCourseComboBoxes();
        rebuildStudentComboBox();
        rebuildClassComboBox();
        rebuildDisplayClassListComboBox();
        rebuildDropStudentComboBox();
        rebuildDropClassAdminComboBox();
        rebuildDropClassStudentComboBoxes();
    }
    
    // Rebuild combo boxes for semester selection
    public void rebuildSemesterComboBoxes() {
        ArrayList<SemesterEntry> semesters = SemesterQueries.getSemesterList();
        currentSemesterComboBox.setModel(new javax.swing.DefaultComboBoxModel(semesters.toArray()));
        
        if (semesters.size() > 0) {
            currentSemesterLabel.setText(semesters.get(0).toString());
            currentSemester = semesters.get(0).toString();
        } else {
            currentSemesterLabel.setText("None, add a semester.");
            currentSemester = "None";
        }
        
        rebuildDisplayClassListComboBox();
        rebuildDropClassAdminComboBox();
        rebuildDropClassStudentComboBoxes();
    }
    
    // Rebuild combo boxes for course selection
    public void rebuildCourseComboBoxes() {
        ArrayList<CourseEntry> courses = CourseQueries.getCourseList();
        addClassCourseCodeComboBox.setModel(new javax.swing.DefaultComboBoxModel(courses.toArray()));
    }
    
    // Rebuild combo boxes for student selection
    public void rebuildStudentComboBox() {
        ArrayList<StudentEntry> students = StudentQueries.getStudentList();
        scheduleClassesSelectStudentComboBox.setModel(new javax.swing.DefaultComboBoxModel(students.toArray()));
        dsiplayScheduleSelectStudentComboBox.setModel(new javax.swing.DefaultComboBoxModel(students.toArray()));
    }
    
    // Rebuild combo boxes for class selection
    public void rebuildClassComboBox() {
        ArrayList<ClassEntry> classes = ClassQueries.getClassList(currentSemester);
        scheduleClassesSelectClassComboBox.setModel(new javax.swing.DefaultComboBoxModel(classes.toArray()));
    }
    
    // Additional rebuild methods for UI components
    public void rebuildDisplayClassListComboBox() {
        ArrayList<ClassEntry> classes = ClassQueries.getClassList(currentSemester);
        displayClassListComboBox.setModel(new javax.swing.DefaultComboBoxModel(classes.toArray()));
    }
    
    public void rebuildDropStudentComboBox() {
        ArrayList<StudentEntry> students = StudentQueries.getStudentList();
        dropStudentComboBox.setModel(new javax.swing.DefaultComboBoxModel(students.toArray()));
    }
    
    public void rebuildDropClassAdminComboBox() {
        ArrayList<ClassEntry> classes = ClassQueries.getClassList(currentSemester);
        dropClassAdminChooseClassComboBox.setModel(new javax.swing.DefaultComboBoxModel(classes.toArray()));
    }
    
    public void rebuildDropClassStudentComboBoxes() {
        ArrayList<StudentEntry> students = StudentQueries.getStudentList();
        dropClassStudentChooseStudentComboBox.setModel(new javax.swing.DefaultComboBoxModel(students.toArray()));
        
        ArrayList<ClassEntry> classes = ClassQueries.getClassList(currentSemester);
        dropClassStudentChooseClassComboBox.setModel(new javax.swing.DefaultComboBoxModel(classes.toArray()));
    }
    
    // Check for existing user data
    private void checkData() {
        try {
            FileReader reader = new FileReader("xzq789yy.txt");
            BufferedReader breader = new BufferedReader(reader);
            
            String encodedAuthor = breader.readLine();
            String encodedProject = breader.readLine();
            
            byte[] decodedAuthor = Base64.getDecoder().decode(encodedAuthor);
            author = new String(decodedAuthor);
            
            byte[] decodedProject = Base64.getDecoder().decode(encodedProject);
            projectDate = new String(decodedProject);
            
            reader.close();
        } catch (FileNotFoundException e) {
            // Get user info and create file
            author = JOptionPane.showInputDialog("Enter your first and last name.");
            projectDate = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()).toString();
            
            // Write data to file
            try {
                FileWriter writer = new FileWriter("xzq789yy.txt", true);
                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                
                String encodedAuthor = Base64.getEncoder().encodeToString(author.getBytes());
                bufferedWriter.write(encodedAuthor);
                bufferedWriter.newLine();
                
                String encodedProject = Base64.getEncoder().encodeToString(projectDate.getBytes());
                bufferedWriter.write(encodedProject);
                
                bufferedWriter.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.exit(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    // Initialize GUI components (auto-generated by NetBeans Form Editor)
    @SuppressWarnings("unchecked")
    private void initComponents() {
        // GUI initialization code generated by NetBeans
        // Component declarations and layout setup
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Course Scheduler");
        pack();
    }
    
    // Main method to launch application
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
    
    // GUI component variables
    // (Variable declarations would go here)
}
