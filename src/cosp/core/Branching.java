package cosp.core;

import cosp.exceptions.InterpreterException;
import java.util.HashMap;

public class Branching {
    private HashMap<String, Integer> labels;
    private SystemState systemState;

    public Branching(HashMap<String, Integer> labels, SystemState systemState) {
        this.labels = labels;
        this.systemState = systemState;
    }

    private int getLabelLine(String label, int currentPC) {
        if (!labels.containsKey(label)) {
            throw new InterpreterException(
                    "Error at line " + (currentPC + 1) + ": Label '" + label + "' is not defined."
            );
        }
        return labels.get(label);
    }

    public void cmp(String[] instruction) {
        int val1 = systemState.getValue(instruction[1]);
        int val2 = systemState.getValue(instruction[2]);
        systemState.updateFlags(val1 - val2);
    }

    public int jmp(String[] instruction, int currentPC) {
        return getLabelLine(instruction[1], currentPC);
    }

    public int jz(String[] instruction, int currentPC) {
        if (systemState.getZFlag()) {
            return getLabelLine(instruction[1], currentPC);
        }
        return currentPC;
    }

    public int jn(String[] instruction, int currentPC) {
        if (systemState.getNFlag()) {
            return getLabelLine(instruction[1], currentPC);
        }
        return currentPC;
    }

    public int jnz(String[] instruction, int currentPC) {
        if (!systemState.getZFlag()) {
            return getLabelLine(instruction[1], currentPC);
        }
        return currentPC;
    }
}