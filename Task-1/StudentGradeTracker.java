// Code Alpha Internship -  TASK-1
// TASK - 1 ( Student Grade Tracker )
// Follow on GitHub -> @Warrior-Gosai


import java.util.*;

public class StudentGradeTracker {

    // Student class to hold individual student data
    static class Student {
        String name;
        double marks;

        Student(String name, double marks) {
            this.name = name;
            this.marks = marks;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Student> students = new ArrayList<>();
        int choice;

        do {
            System.out.println("\n=== STUDENT GRADE TRACKER ===");
            System.out.println("1. Add Student Grade");
            System.out.println("2. View All Students");
            System.out.println("3. Show Statistics (Average, Highest, Lowest)");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter student name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter marks (0-100): ");
                    double marks = sc.nextDouble();
                    students.add(new Student(name, marks));
                    System.out.println("Grade added successfully!");
                    break;

                case 2:
                    if (students.isEmpty()) {
                        System.out.println("No students added yet!");
                    } else {
                        System.out.println("\n--- All Students ---");
                        for (Student s : students) {
                            System.out.println("Name: " + s.name + " | Marks: " + s.marks);
                        }
                    }
                    break;

                case 3:
                    if (students.isEmpty()) {
                        System.out.println("No data to analyze!");
                    } else {
                        double total = 0;
                        double highest = Double.MIN_VALUE;
                        double lowest = Double.MAX_VALUE;

                        for (Student s : students) {
                            total += s.marks;
                            if (s.marks > highest) highest = s.marks;
                            if (s.marks < lowest) lowest = s.marks;
                        }

                        double average = total / students.size();

                        System.out.println("\n--- Grade Statistics ---");
                        System.out.println("Total Students: " + students.size());
                        System.out.println("Average Marks: " + String.format("%.2f", average));
                        System.out.println("Highest Marks: " + highest);
                        System.out.println("Lowest Marks: " + lowest);
                    }
                    break;

                case 4:
                    System.out.println("\nThank you for using the Student Grade Tracker!");
                    break;

                default:
                    System.out.println("Invalid choice! Try again.");
            }
        } while (choice != 4);

        sc.close();
    }
}
