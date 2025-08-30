import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class StudentGradesGUI extends JFrame {
    private ArrayList<Student> students = new ArrayList<>();

    public StudentGradesGUI() {
        setTitle("Student Grade Tracker");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ✅ Title and Info
        JLabel title = new JLabel("📊 Student Grade Tracker", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 20));
        JLabel info = new JLabel("Add students with their grades, then generate a report to view results.", SwingConstants.CENTER);
        info.setFont(new Font("Serif", Font.PLAIN, 14));

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(title);
        topPanel.add(info);
        add(topPanel, BorderLayout.NORTH);

        // ✅ Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Add Student");
        JButton reportBtn = new JButton("Generate Report");
        buttonPanel.add(addBtn);
        buttonPanel.add(reportBtn);
        add(buttonPanel, BorderLayout.CENTER);

        addBtn.addActionListener(e -> addStudent());
        reportBtn.addActionListener(e -> generateReport());

        setVisible(true);
    }

    private void addStudent() {
        String name = JOptionPane.showInputDialog(this, "Enter Student Name:");
        if (name == null || name.trim().isEmpty()) return;

        try {
            double grade = Double.parseDouble(
                JOptionPane.showInputDialog(this, "Enter Grade for " + name + ":")
            );
            students.add(new Student(name, grade));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid grade entered.");
        }
    }

    private void generateReport() {
        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No student data available.");
            return;
        }

        students.sort((a, b) -> Double.compare(b.grade, a.grade));

        String[] columnNames = {"Student Name", "Grade"};
        String[][] data = new String[students.size()][2];

        double sum = 0;
        double highest = students.get(0).grade;
        double lowest = students.get(0).grade;

        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            data[i][0] = s.name;
            data[i][1] = String.valueOf(s.grade);
            sum += s.grade;
            highest = Math.max(highest, s.grade);
            lowest = Math.min(lowest, s.grade);
        }

        double avg = sum / students.size();

        ArrayList<String> highestStudents = new ArrayList<>();
        ArrayList<String> lowestStudents = new ArrayList<>();
        for (Student s : students) {
            if (s.grade == highest) highestStudents.add(s.name);
            if (s.grade == lowest) lowestStudents.add(s.name);
        }

        JTable table = new JTable(data, columnNames);
        table.setFont(new Font("Serif", Font.PLAIN, 14));
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);

        String summary = String.format(
            "<html><b>Average Score:</b> %.2f<br>" +
            "<b>Highest Score:</b> %.2f (by %s)<br>" +
            "<b>Lowest Score:</b> %.2f (by %s)</html>",
            avg, highest, String.join(", ", highestStudents),
            lowest, String.join(", ", lowestStudents)
        );
        JLabel summaryLabel = new JLabel(summary);
        summaryLabel.setFont(new Font("Serif", Font.PLAIN, 14));

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(summaryLabel, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(this, panel, "📑 Student Grade Report", JOptionPane.INFORMATION_MESSAGE);
    }

    private static class Student {
        String name;
        double grade;
        Student(String n, double g) {
            name = n;
            grade = g;
        }
    }

    public static void main(String[] args) {
        new StudentGradesGUI();
    }
}
