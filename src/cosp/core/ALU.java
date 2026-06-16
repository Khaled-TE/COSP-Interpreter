package cosp.core;

import cosp.exceptions.DivisionByZeroException;
import cosp.exceptions.InvalidInstructionException;

public class ALU {
    private SystemState systemState;

    public ALU(SystemState systemState) {
        this.systemState = systemState;
    }


    public void mov(String[] instruction) {
        checkLength(instruction,3);
        String destination = instruction[1];
        int value = systemState.getValue(instruction[2]);

        systemState.setRegister(destination, value);
    }

    public void add(String[] instruction) {
        checkLength(instruction,4);
        String destination = instruction[1];

        int value1 = systemState.getValue(instruction[2]);
        int value2 = systemState.getValue(instruction[3]);

        int result = value1 + value2;

        systemState.setRegister(destination, result);
        systemState.updateFlags(result);
    }

    public void sub(String[] instruction) {
        checkLength(instruction,4);
        String destination = instruction[1];

        int value1 = systemState.getValue(instruction[2]);
        int value2 = systemState.getValue(instruction[3]);

        int result = value1 - value2;

        systemState.setRegister(destination, result);
        systemState.updateFlags(result);
    }

    public void mul(String[] instruction) {
        checkLength(instruction,4);
        String destination = instruction[1];

        int value1 = systemState.getValue(instruction[2]);
        int value2 = systemState.getValue(instruction[3]);

        int result = value1 * value2;

        systemState.setRegister(destination, result);
        systemState.updateFlags(result);
    }

    public void div(String[] instruction) {
        checkLength(instruction,4);
        String destination = instruction[1];

        int value1 = systemState.getValue(instruction[2]);
        int value2 = systemState.getValue(instruction[3]);

        if (value2 == 0) {
            throw new DivisionByZeroException("Error: Division by zero.");
        }

        int result = value1 / value2;

        systemState.setRegister(destination, result);
        systemState.updateFlags(result);
    }

    public void and(String[] instruction) {
        checkLength(instruction,4);
        String destination = instruction[1];

        int value1 = systemState.getValue(instruction[2]);
        int value2 = systemState.getValue(instruction[3]);

        int result = value1 & value2;

        systemState.setRegister(destination, result);
    }

    public void or(String[] instruction) {
        checkLength(instruction,4);
        String destination = instruction[1];

        int value1 = systemState.getValue(instruction[2]);
        int value2 = systemState.getValue(instruction[3]);

        int result = value1 | value2;

        systemState.setRegister(destination, result);
    }

    public void xor(String[] instruction) {
        checkLength(instruction,4);
        String destination = instruction[1];

        int value1 = systemState.getValue(instruction[2]);
        int value2 = systemState.getValue(instruction[3]);

        int result = value1 ^ value2;

        systemState.setRegister(destination, result);
    }

    public void not(String[] instruction) {
        checkLength(instruction,3);
        String destination = instruction[1];

        int value = systemState.getValue(instruction[2]);

        int result = ~value;

        systemState.setRegister(destination, result);
    }

    public void shl(String[] instruction) {
        checkLength(instruction,4);
        String destination = instruction[1];

        int value = systemState.getValue(instruction[2]);
        int shiftAmount = systemState.getValue(instruction[3]);

        int result = value << shiftAmount;

        systemState.setRegister(destination, result);
    }

    public void shr(String[] instruction) {
        checkLength(instruction,4);
        String destination = instruction[1];

        int value = systemState.getValue(instruction[2]);
        int shiftAmount = systemState.getValue(instruction[3]);

        int result = value >> shiftAmount;

        systemState.setRegister(destination, result);
    }
    private void checkLength(String[] instruction, int expectedLength) {
        if (instruction.length != expectedLength) {
            throw new InvalidInstructionException(
                    "Invalid number of operands for " + instruction[0]
            );
        }
    }
}

