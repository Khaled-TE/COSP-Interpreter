package cosp.core;

import cosp.exceptions.UnknownOpcodeException;
import java.util.ArrayList;
import java.util.HashMap;

public class Interpreter {
    private int PC = 0;
    private ArrayList<String[]> instructions;
    private boolean running = true;
    private ALU alu;
    private SystemState systemState;
    private Branching branching;

    public Interpreter(ArrayList<String[]> instructions, HashMap<String, Integer> labels) {
        this.instructions = instructions;
        this.systemState = new SystemState();
        this.alu = new ALU(systemState);
        this.branching = new Branching(labels, systemState);
    }

    public String[] fetch() {
        return instructions.get(PC);

    }

    public String decode(String[] instruction) {
        return instruction[0];
    }

    public void execute(String[] instruction) {
        String opcode = decode(instruction);
        switch (opcode) {
            case "MOV": alu.mov(instruction); break;
            case "ADD": alu.add(instruction); break;
            case "SUB": alu.sub(instruction); break;
            case "MUL": alu.mul(instruction); break;
            case "DIV": alu.div(instruction); break;
            case "AND": alu.and(instruction); break;
            case "OR":  alu.or(instruction);  break;
            case "XOR": alu.xor(instruction); break;
            case "NOT": alu.not(instruction); break;
            case "SHL": alu.shl(instruction); break;
            case "SHR": alu.shr(instruction); break;
            case "LOAD":  systemState.load(instruction);  break;
            case "STORE": systemState.store(instruction); break;
            case "PRINT": systemState.print(instruction); break;
            case "CMP": branching.cmp(instruction); break;
            case "JMP": PC = branching.jmp(instruction, PC); break;
            case "JZ":  PC = branching.jz(instruction, PC);  break;
            case "JN":  PC = branching.jn(instruction, PC);  break;
            case "JNZ": PC = branching.jnz(instruction, PC); break;
            case "HALT": running = false; break;
            default: throw new UnknownOpcodeException("Unknown opcode: " + opcode);
        }
    }

    public void run() {
        while (running && PC < instructions.size()) {
            String[] instruction = fetch();
            PC++;
            execute(instruction);
        }
        if (running) {
            System.out.println("Warning: program ended without HALT");
        }
    }

}