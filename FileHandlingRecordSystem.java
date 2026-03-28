import java.io.*;
import java.util.*;

public class FileHandlingRecordSystem {
    private static final String DATA_FILE = "student_records.csv";
    private static List<StudentRecord> recordsArray = new ArrayList<>();

    static class StudentRecord {
        private String id;
        private String name;
        private String age;
        private String email;

        public StudentRecord(String id, String name, String age, String email) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.email = email;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getAge() { return age; }
        public String getEmail() { return email; }

        public void setName(String name) { this.name = name; }
        public void setAge(String age) { this.age = age; }
        public void setEmail(String email) { this.email = email; }

        @Override
        public String toString() {
            return id + "," + name + "," + age + "," + email;
        }

        public String toDisplayString() {
            return String.format("%-4s %-15s %-6s %s", id, name, age, email);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== FILE HANDLING RECORD SYSTEM ===");
        
        recordsArray = loadRecordsWhenProgramStarts();
        
        while (true) {
            showMenu();
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addNewRecord(scanner);
                    break;
                case "2":
                    updateRecord(scanner);
                    break;
                case "3":
                    deleteRecord(scanner);
                    break;
                case "4":
                    displayRecords();
                    break;
                case "5":
                    saveAllRecords();
                    System.out.println("Program ended. All data saved.");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
            
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    private static void saveRecordsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            writer.println("id,name,age,email");
            
            for (StudentRecord record : recordsArray) {
                writer.println(record.toString());
            }
            System.out.println("✓ Records array saved to " + DATA_FILE + " (" + recordsArray.size() + " records)");
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    private static List<StudentRecord> loadRecordsWhenProgramStarts() {
        List<StudentRecord> loadedArray = new ArrayList<>();
        
        File file = new File(DATA_FILE);
        
        if (file.exists()) {
            System.out.println("File '" + DATA_FILE + "' exists. Loading records...");
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                boolean firstLine = true; 
                while ((line = reader.readLine()) != null) {
                    if (firstLine) {
                        firstLine = false;
                        continue;
                    }
                    String[] parts = line.split(",", 4);
                    if (parts.length == 4) {
                        StudentRecord record = new StudentRecord(
                            parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim()
                        );
                        loadedArray.add(record); 
                    }
                }
                System.out.println("Loaded " + loadedArray.size() + " records into array");
            } catch (IOException e) {
                System.out.println("Error loading file: " + e.getMessage());
            }
        } else {
            System.out.println("No existing file. Starting with empty array.");
            saveRecordsToFile();
        }
        
        displayRecords(loadedArray);
        return loadedArray;
    }

    private static void addNewRecord(Scanner scanner) {
        System.out.println("\n--- ADD NEW RECORD ---");
        
        System.out.print("ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Age: ");
        String age = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        StudentRecord newRecord = new StudentRecord(id, name, age, email);
        recordsArray.add(newRecord);
        System.out.println("✓ Stored in array (size: " + recordsArray.size() + ")");

        saveRecordsToFile();
    }

    private static void updateRecord(Scanner scanner) {
        if (recordsArray.isEmpty()) {
            System.out.println("No records to update!");
            return;
        }

        displayRecords();
        System.out.print("Enter record index to update: ");
        
        try {
            int index = Integer.parseInt(scanner.nextLine().trim());
            if (index >= 0 && index < recordsArray.size()) {
                StudentRecord record = recordsArray.get(index);
                System.out.println("Updating: " + record.getName());

                System.out.print("New name [" + record.getName() + "]: ");
                String name = scanner.nextLine().trim();
                if (!name.isEmpty()) record.setName(name);

                System.out.print("New age [" + record.getAge() + "]: ");
                String age = scanner.nextLine().trim();
                if (!age.isEmpty()) record.setAge(age);

                System.out.print("New email [" + record.getEmail() + "]: ");
                String email = scanner.nextLine().trim();
                if (!email.isEmpty()) record.setEmail(email);

                System.out.println("✓ Record updated in array");
                
                saveRecordsToFile();
            } else {
                System.out.println("Invalid index!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid index! Enter a number.");
        }
    }

    private static void deleteRecord(Scanner scanner) {
        if (recordsArray.isEmpty()) {
            System.out.println("No records to delete!");
            return;
        }

        displayRecords();
        System.out.print("Enter record index to delete: ");
        
        try {
            int index = Integer.parseInt(scanner.nextLine().trim());
            if (index >= 0 && index < recordsArray.size()) {
                StudentRecord record = recordsArray.get(index);
                System.out.print("Delete '" + record.getName() + "'? (y/n): ");
                String confirm = scanner.nextLine().trim().toLowerCase();
                
                if (confirm.equals("y")) {
                    recordsArray.remove(index);
                    System.out.println("✓ Removed from array (size: " + recordsArray.size() + ")");
                    
                    saveRecordsToFile();
                } else {
                    System.out.println("Delete cancelled.");
                }
            } else {
                System.out.println("Invalid index!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid index! Enter a number.");
        }
    }

    private static void displayRecords() {
        displayRecords(recordsArray);
    }

    private static void displayRecords(List<StudentRecord> records) {
        System.out.println("\nRecords Array:");
        System.out.println("-".repeat(60));
        System.out.printf("%-4s %-15s %-6s %s%n", "ID", "Name", "Age", "Email");
        System.out.println("-".repeat(60));
        
        if (records.isEmpty()) {
            System.out.println("Array is empty.");
        } else {
            for (int i = 0; i < records.size(); i++) {
                System.out.println(i + ": " + records.get(i).toDisplayString());
            }
        }
        System.out.println("-".repeat(60));
    }

    private static void saveAllRecords() {
        saveRecordsToFile();
    }

    private static void showMenu() {
        System.out.println("\n1. Add Record");
        System.out.println("2. Update Record (by index)");
        System.out.println("3. Delete Record (by index)");
        System.out.println("4. Display Records");
        System.out.println("5. Exit & Save");
    }
}