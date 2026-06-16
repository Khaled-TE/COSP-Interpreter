<div align="center">
  
  # ⚙️ Custom Assembly Interpreter (COSP)
  
  **A lightweight, robust virtual machine and assembly language interpreter built entirely in Java.**
  
  [![Java](https://img.shields.io/badge/Java-11%2B-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/)
  [![Architecture: VM](https://img.shields.io/badge/Architecture-Virtual_Machine-4B0082?style=for-the-badge)](#)

</div>

---

## 🎯 Project Overview
This project is a custom Assembly Language Interpreter (COSP) that simulates a basic CPU architecture. It parses, loads, and executes low-level assembly instructions from a text file, providing a controlled virtual environment with its own registers, memory space, and arithmetic logic unit (ALU).

---

## 🏗️ System Architecture & Specifications

The virtual machine is designed with a strict, memory-safe architecture:

* **Registers:** 8 General-Purpose Registers (`R0` through `R7`).
* **Memory Segment:** 256 addressable memory slots (Indices `0` to `255`).
* **Status Flags:** 
  * `Z` (Zero Flag): Set to true if the result of the last arithmetic or comparison operation is zero.
  * `N` (Negative Flag): Set to true if the result is less than zero.
* **Fetch-Execute Cycle:** Implements a standard CPU pipeline loop that dynamically fetches instructions, decodes opcodes, and routes execution to the ALU or Branching units.

---

## 📜 Supported Instruction Set (ISA)

The interpreter supports a comprehensive custom instruction set, categorized logically:

### 1. Data Movement & Memory
* `MOV <reg>, <val/reg>`: Moves an immediate value or register content into a register.
* `LOAD <reg>, <address>`: Loads a value from a specific memory address into a register.
* `STORE <reg>, <address>`: Stores the value of a register into a memory address.

### 2. Arithmetic & Logic (ALU)
* **Math:** `ADD`, `SUB`, `MUL`, `DIV` (Supports immediate values and register-to-register operations).
* **Bitwise:** `AND`, `OR`, `XOR`, `NOT`, `SHL` (Shift Left), `SHR` (Shift Right).

### 3. Control Flow & Branching
* `CMP <val/reg>, <val/reg>`: Compares two values and updates the `Z` and `N` flags.
* `JMP <label>`: Unconditional jump to a defined label.
* `JZ <label>`: Jump if Zero (Executes if `Z == true`).
* `JN <label>`: Jump if Negative (Executes if `N == true`).
* `JNZ <label>`: Jump if Not Zero (Executes if `Z == false`).

### 4. Utilities
* `PRINT <reg>`: Outputs the integer value of a register to the standard console.
* `HALT`: Safely terminates the execution loop.

---

## 🛡️ Robust Error Handling & Parsing

The `ProgramLoader` and execution core are fortified with strict validation checks to prevent runtime crashes. Custom exceptions are thrown for specific architectural violations:
* `UnknownOpcodeException`: Prevents execution of unsupported instructions.
* `InvalidRegisterException`: Triggers if operands fall outside `R0-R7`.
* `InvalidMemoryException`: Enforces the strict 256-slot memory bounds.
* `DivisionByZeroException`: Hardware-level math safety.
* **Syntax Support:** The parser automatically ignores inline comments (using `;`) and strictly validates operand counts for every instruction type before execution begins.

---

## 🚀 How to Run

```bash
# 1. Clone the repository
git clone https://github.com/Khaled-TE/COSP-Interpreter.git

# 2. Navigate to the project directory
cd COSP-Interpreter

# 3. Clean, build, and run via Maven
javac cosp/app/Main.java