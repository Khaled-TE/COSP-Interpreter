package cosp.core;

import cosp.exceptions.InvalidMemoryException;
import cosp.exceptions.InvalidRegisterException;

public class SystemState {
    private int[] registers = new int[8];
    private int[] memory = new int[256];
    private boolean Z = false;
    private boolean N = false;

    public int getRegister(String regName) {
        regName = regName.toUpperCase();
        if (!regName.matches("R[0-7]")) {
            throw new InvalidRegisterException("Invalid register '" + regName + "'. Must be R0 to R7.");
        }
        int index = Integer.parseInt(regName.substring(1));
        return registers[index];
    }
    public void setRegister(String regName, int value) {
        regName = regName.toUpperCase();
        if (!regName.matches("R[0-7]")) {
            throw new InvalidRegisterException("Invalid register '" + regName + "'. Must be R0 to R7.");
        }
        int index = Integer.parseInt(regName.substring(1));
        registers[index] = value;
    }
    public int loadFromMemory(int address) {
        if (address < 0 || address > 255) {
            throw new InvalidMemoryException("Invalid memory address: " + address + ". Must be between 0 and 255.");
        }
        return memory[address];
    }
    public void storeToMemory(int address, int value) {
        if (address < 0 || address > 255) {
            throw new InvalidMemoryException("Invalid memory address: " + address + ". Must be between 0 and 255." );
        }
        memory[address] = value;
    }
    public void load(String[] instruction) {
        String regName = instruction[1];
        int address = Integer.parseInt(instruction[2]);
        int value = loadFromMemory(address);
        setRegister(regName, value);
    }
    public void store(String[] instruction) {
        String regName = instruction[1];
        int address = Integer.parseInt(instruction[2]);
        int value = getRegister(regName);
        storeToMemory(address, value);
    }
    public void print(String[] instruction) {
        String regName = instruction[1];
        int value = getRegister(regName);
        System.out.println(regName + "=" + value);
    }

    public boolean getZFlag(){
        return Z;
    }
    public boolean getNFlag(){
        return N;
    }
    public void updateFlags(int result) {
        Z=false;
        N=false;
        Z = (result == 0);
        N = (result < 0);
    }
    public int getValue(String operand) {
        if (operand.matches("R[0-7]")) {
            return getRegister(operand);
        }
        return Integer.parseInt(operand);
    }
}