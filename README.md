<div align="center">
  
  # ⚙️ Custom Assembly Interpreter (COSP)
  
  **A lightweight, robust virtual machine and assembly language interpreter built entirely in Java.**
  
  [![Java](https://img.shields.io/badge/Java-11%2B-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/)
  [![Architecture: VM](https://img.shields.io/badge/Architecture-Virtual_Machine-4B0082?style=for-the-badge)](#)

</div>

---

## 🎯 Project Overview
This project is a custom Assembly Language Interpreter (COSP) that simulates a basic CPU architecture. It parses, loads, and executes low-level assembly instructions from a text file, providing a controlled virtual environment with its own registers, memory space, and arithmetic logic unit (ALU)[cite: 3, 6, 7].

---

## 🏗️ System Architecture & Specifications

The virtual machine is designed with a strict, memory-safe architecture[cite: 4]:

* **Registers:** 8 General-Purpose Registers (`R0` through `R7`)[cite: 4].
* **Memory Segment:** 256 addressable memory slots (Indices `0` to `255`)[cite: 4].
* **Status Flags:** 
  * `Z` (Zero Flag): Set to true if the result of the last arithmetic or comparison operation is zero[cite: 4].
  * `N` (Negative Flag): Set to true if the result is less than zero[cite: 4].
* **Fetch-Execute Cycle:** Implements a standard CPU pipeline loop that dynamically fetches instructions, decodes opcodes, and routes execution to the ALU or Branching units[cite: 3].

---

## 📜 Supported Instruction Set (ISA)

The interpreter supports a comprehensive custom instruction set, categorized logically[cite: 7]:

### 1. Data Movement & Memory
* `MOV <reg>, <val/reg>`: Moves an immediate value or register content into a register[cite: 5].
* `LOAD <reg>, <address>`: Loads a value from a specific memory address into a register[cite: 4].
* `STORE <reg>, <address>`: Stores the value of a register into a memory address[cite: 4].

### 2. Arithmetic & Logic (ALU)
* **Math:** `ADD`, `SUB`, `MUL`, `DIV` (Supports immediate values and register-to-register operations)[cite: 5].
* **Bitwise:** `AND`, `OR`, `XOR`, `NOT`, `SHL` (Shift Left), `SHR` (Shift Right)[cite: 5].

### 3. Control Flow & Branching
* `CMP <val/reg>, <val/reg>`: Compares two values and updates the `Z` and `N` flags[cite: 2].
* `JMP <label>`: Unconditional jump to a defined label[cite: 2].
* `JZ <label>`: Jump if Zero (Executes if `Z == true`)[cite: 2].
* `JN <label>`: Jump if Negative (Executes if `N == true`)[cite: 2].
* `JNZ <label>`: Jump if Not Zero (Executes if `Z == false`)[cite: 2].

### 4. Utilities
* `PRINT <reg>`: Outputs the integer value of a register to the standard console[cite: 4].
* `HALT`: Safely terminates the execution loop[cite: 3].

---

## 🛡️ Robust Error Handling & Parsing

The `ProgramLoader` and execution core are fortified with strict validation checks to prevent runtime crashes[cite: 7]. Custom exceptions are thrown for specific architectural violations:
* `UnknownOpcodeException`: Prevents execution of unsupported instructions[cite: 7].
* `InvalidRegisterException`: Triggers if operands fall outside `R0-R7`[cite: 7].
* `InvalidMemoryException`: Enforces the strict 256-slot memory bounds[cite: 4].
* `DivisionByZeroException`: Hardware-level math safety[cite: 5].
* **Syntax Support:** The parser automatically ignores inline comments (using `;`) and strictly validates operand counts for every instruction type before execution begins[cite: 7].

---

## 🚀 How to Run

1. Clone the repository and navigate to the root directory.
2. Compile the Java files:
```bash
   javac cosp/app/Main.java