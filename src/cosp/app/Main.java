package cosp.app;

import cosp.core.Interpreter;
import cosp.exceptions.InterpreterException;
import cosp.loader.ProgramLoader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the file path: ");
        String filePath = scanner.nextLine().trim();
        scanner.close();

        try {
            ProgramLoader loader = new ProgramLoader();
            loader.load(filePath);
            Interpreter interpreter = new Interpreter(
                    loader.getInstructions(),
                    loader.getLabels()
            );
            interpreter.run();

        } catch (InterpreterException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
}