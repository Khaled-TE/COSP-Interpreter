package cosp.loader;

import cosp.exceptions.InterpreterException;
import cosp.exceptions.InvalidInstructionException;
import cosp.exceptions.InvalidRegisterException;
import cosp.exceptions.UnknownOpcodeException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ProgramLoader {
    private ArrayList<String[]> instructions = new ArrayList<>();
    private HashMap<String, Integer> labels = new HashMap<>();
    private static final String[] VALID_OPCODES = {
            "MOV", "ADD", "SUB", "MUL", "DIV",
            "AND", "OR", "XOR", "NOT", "SHL", "SHR",
            "LOAD", "STORE", "PRINT", "CMP",
            "JMP", "JZ", "JN", "JNZ", "HALT"
    };
    public void load(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new InterpreterException("Error: No file path provided.");
        }
        File file = new File(filePath);
        if (!file.exists()) {
            throw new InterpreterException("Error: File not found -> " + filePath);
        }
        if (!file.isFile()) {
            throw new InterpreterException("Error: Path is not a valid file -> " + filePath);
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line=line.toUpperCase();

                //remove comments
                if (line.contains(";")) {
                    line = line.substring(0, line.indexOf(";"));
                }
                line = line.trim();
                if(line.isEmpty()) continue;

                //handle labels
                if (line.endsWith(":")) {
                    String labelName = line.substring(0, line.length() - 1).trim();
                    if (labelName.isEmpty()) {
                        throw new InterpreterException("Error at line " + lineNumber + ": Empty label name.");
                    }
                    if (labels.containsKey(labelName)) {
                        throw new InterpreterException("Error at line " + lineNumber + ": Duplicate label -> " + labelName);
                    }
                    labels.put(labelName, instructions.size());
                } else {
                    String[]tokens = line.split("[,\\s]+");
                    if (tokens.length == 0) {
                        continue;
                    }
                    String opcode = tokens[0];

                    // Error 7 — unknown opcode
                    if (!isValidOpcode(opcode)) {
                        throw new UnknownOpcodeException("Error at line " + lineNumber + ": Unknown opcode -> " + opcode);
                    }
                    validateOperandCount(opcode, tokens, lineNumber);

                    // Error 9 — validate register names
                    validateRegisters(opcode, tokens, lineNumber);

                    instructions.add(tokens);
                }
            }
            reader.close();
            if (instructions.isEmpty()) {
                throw new InterpreterException("Error: The file contains no valid instructions.");
            }

            for (String[] instruction : instructions) {
                String opcode = instruction[0];
                if (opcode.equals("JMP") || opcode.equals("JZ") ||
                        opcode.equals("JN") || opcode.equals("JNZ")) {
                    String label = instruction[1];
                    if (!labels.containsKey(label)) {
                        throw new InterpreterException(
                                "Error: Jump to undefined label -> " + label
                        );
                    }
                }
            }

        } catch (IOException e) {
            throw new InterpreterException("Error reading file: " + e.getMessage());
        }
    }

    private void validateOperandCount(String opcode, String[] tokens, int lineNumber) {
        int expected = getExpectedOperandCount(opcode);
        int actual = tokens.length - 1;

        if (expected != -1 && actual != expected) {
            throw new InvalidInstructionException("Error at line " + lineNumber + ": " + opcode +" expects " + expected + " operand(s) but got " + actual + ".");
        }
    }

    private boolean isValidOpcode(String opcode) {
        for (String valid : VALID_OPCODES) {
            if (valid.equals(opcode)) return true;
        }
        return false;
    }

    private int getExpectedOperandCount(String opcode) {
        switch (opcode) {
            case "HALT":  return 0;
            case "PRINT": return 1;
            case "JMP":
            case "JZ":
            case "JN":
            case "JNZ":   return 1;
            case "NOT":
            case "MOV":
            case "LOAD":
            case "STORE":
            case "CMP":   return 2;
            case "ADD":
            case "SUB":
            case "MUL":
            case "DIV":
            case "AND":
            case "OR":
            case "XOR":
            case "SHL":
            case "SHR":   return 3;
            default:      return -1;
        }
    }

    private void validateRegisters(String opcode, String[] tokens, int lineNumber) {
        switch (opcode) {


            case "MOV":
                checkRegister(tokens[1], lineNumber, opcode);
                checkRegisterOrImmediate(tokens[2], lineNumber, opcode);
                break;


            case "ADD":
            case "SUB":
            case "MUL":
            case "DIV":
            case "AND":
            case "OR":
            case "XOR":
                checkRegister(tokens[1], lineNumber, opcode);
                checkRegisterOrImmediate(tokens[2], lineNumber, opcode);
                checkRegisterOrImmediate(tokens[3], lineNumber, opcode);
                break;


            case "NOT":
                checkRegister(tokens[1], lineNumber, opcode);
                checkRegisterOrImmediate(tokens[2], lineNumber, opcode);
                break;


            case "SHL":
            case "SHR":
                checkRegister(tokens[1], lineNumber, opcode);
                checkRegisterOrImmediate(tokens[2], lineNumber, opcode);
                checkImmediate(tokens[3], lineNumber, opcode);
                break;


            case "CMP":
                checkRegisterOrImmediate(tokens[1], lineNumber, opcode);
                checkRegisterOrImmediate(tokens[2], lineNumber, opcode);
                break;


            case "LOAD":
            case "STORE":
                checkRegister(tokens[1], lineNumber, opcode);
                checkImmediate(tokens[2], lineNumber, opcode);
                break;


            case "PRINT":
                checkRegister(tokens[1], lineNumber, opcode);
                break;


            case "JMP":
            case "JZ":
            case "JN":
            case "JNZ":
                break;

            case "HALT":
                break;
        }
    }


    private void checkRegister(String token, int lineNumber, String opcode) {
        if (!token.matches("R[0-7]")) {
            throw new InvalidRegisterException(
                    "Error at line " + lineNumber + ": Invalid register '" + token +
                            "' in " + opcode + ". Valid registers are R0-R7."
            );
        }
    }


    private void checkRegisterOrImmediate(String token, int lineNumber, String opcode) {
        if (!token.matches("R[0-7]")) {
            try {
                Integer.parseInt(token);
            } catch (NumberFormatException e) {
                throw new InvalidInstructionException(
                        "Error at line " + lineNumber + ": Expected a register or number but got '" +
                                token + "' in " + opcode + "."
                );
            }
        }
    }

    // must be an integer
    private void checkImmediate(String token, int lineNumber, String opcode) {
        try {
            Integer.parseInt(token);
        } catch (NumberFormatException e) {
            throw new InvalidInstructionException(
                    "Error at line " + lineNumber + ": Expected a number but got '" +
                            token + "' in " + opcode + "."
            );
        }
    }

    public ArrayList<String[]> getInstructions() {
        return instructions;
    }

    public HashMap<String, Integer> getLabels() {
        return labels;
    }
}