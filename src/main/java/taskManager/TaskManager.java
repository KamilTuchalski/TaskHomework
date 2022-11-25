package taskManager;

import org.apache.commons.lang3.StringUtils;
import pl.coderslab.ConsoleColors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TaskManager {
    public static void main(String[] args) {
        options();
        selectAction();

    }

    /**
     * Selection of program options
     */
    static void options() {
        System.out.println(ConsoleColors.BLUE);
        System.out.println("Please select an option:");
        System.out.print(ConsoleColors.WHITE);
        String[] options = {"add", "remove", "list", "exit"};
        for (String option : options) {
            System.out.println(option);
        }
    }

    /**
     * Loading data from a file
     *
     * @return data tables
     */
    static String[][] downloadingDate() {
        String[][] tasks = null;
        Path path = Paths.get("tasks.csv");
        if (!Files.exists(path)) {
            System.out.println("File does not exist");
        }
        try {
            List<String> strings = Files.readAllLines(path);
            tasks = new String[strings.size()][strings.get(0).split(",").length];

            for (int i = 0; i < strings.size(); i++) {
                String[] split = strings.get(i).split(",");
                for (int j = 0; j < split.length; j++) {
                    tasks[i][j] = split[j];
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return tasks;
    }

    /**
     * Save date to file
     *
     * @param task new task
     */

    static void saveData(String task) {
        Path path = Path.of("tasks.csv");
        if (Files.exists(path)) {
            List<String> outList = new ArrayList<>();
            try {
                File file = new File(String.valueOf(path));
                Scanner scanner1 = new Scanner(file);
                while (scanner1.hasNextLine()) {
                    String s = scanner1.nextLine();
                    outList.add(s);
                }
                outList.add(task);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Files.write(path, outList);
            } catch (IOException ex) {
                System.out.println("Nie można zapisać pliku.");
            }
        }
    }

    /**
     * Selection of the task to be performed
     */
    static void selectAction() {
        Scanner scanner = new Scanner(System.in);
        for (; ; ) {
            System.out.println("Please enter one of the options: ");
            String word = scanner.nextLine();
            if (word.equals("add")) {
                addTask();
                break;
            }
            if (word.equals("list")) {
                listTasks();
                break;
            }
            if (word.equals("remove")) {
                removeTask();
                break;
            }
            if (word.equals("exit")) {
                System.out.println("Program completed successfully");
                break;
            } else {
                System.out.println("Please select a correct option.");
            }

        }
    }

    /**
     * Adding a task to a file
     */
    static void addTask() {
        Scanner scanner = new Scanner(System.in);
        String newTaskStr = "";
        String[] newTask = new String[3];
        for (; ; ) {
            System.out.println("Please add task description: ");
            String description = scanner.nextLine();
            newTask[0] = description;
            System.out.println("Please add task due date: ");
            String date = scanner.nextLine();
            newTask[1] = date;
            System.out.println("Is your task is important: true/false: ");
            String important = scanner.nextLine();
            newTask[2] = important;
            String[][] tasks = downloadingDate();
            tasks = Arrays.copyOf(tasks, tasks.length + 1);
            tasks[tasks.length - 1] = new String[3];
            tasks[tasks.length - 1][0] = description;
            tasks[tasks.length - 1][1] = date;
            tasks[tasks.length - 1][2] = important;
            newTaskStr = StringUtils.join(newTask, ", ");
            saveData(newTaskStr);
            if (newTask[0] != null || newTask[1] != null || newTask[2] != null) {
                break;
            }
        }
        System.out.println("Added task: " + newTaskStr);
        selectAction();
    }

    /**
     * Displays a list of tasks
     */
    static void listTasks() {
        try {
            File file = new File("tasks.csv");
            Scanner scanner = new Scanner(file);
            int i = 0;
            while (scanner.hasNextLine()) {
                String s = scanner.nextLine();
                System.out.println(i + " : " + s);
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        selectAction();
    }

    /**
     * Removes task
     */
    static void removeTask() {
        String[][] tasks = downloadingDate();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select number to remove: ");
        String number = scanner.nextLine();
        if (Integer.parseInt(number) >= 0) {
            if (Integer.parseInt(number) < tasks.length) {
                saveDataToRemove(Integer.parseInt(number));
                System.out.println("Task deleted: " + Integer.parseInt(number));
            } else {
                System.out.println("The given number does not exist in the table");
                System.out.println("Type \"list\" to display tasks");
            }
        } else {
            System.out.println("Wrong job number. Enter the number again");
            scanner.nextLine();

        }
        selectAction();
    }

    /**
     * Tasks save after deletion
     *
     * @param taskIndex the index of the task to be deleted
     */
    static void saveDataToRemove(int taskIndex) {
        Path path = Path.of("tasks.csv");
        if (Files.exists(path)) {
            List<String> outList = new ArrayList<>();
            try {
                File file = new File(String.valueOf(path));
                Scanner scanner1 = new Scanner(file);
                while (scanner1.hasNextLine()) {
                    String s = scanner1.nextLine();
                    outList.add(s);
                }
                outList.remove(taskIndex);
                Files.write(path, outList);
            } catch (IOException ex) {
                System.out.println("Nie można zapisać pliku.");
            }
        }
    }
}

