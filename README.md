# Smart Bank Management System

A Java Object-Oriented Programming semester project built as a desktop banking application using Swing/AWT.

## Main Features
- Admin login
- Customer and account creation
- Savings, Current, and Student accounts
- Deposit, withdrawal, and transfer
- Transaction history
- Account closing
- Dashboard statistics
- File-based data persistence
- Exception handling and validation

## OOP Concepts Demonstrated
- Classes and objects
- Constructors and constructor overloading concepts
- Encapsulation
- Inheritance
- Abstract classes
- Method overriding
- Compile-time and runtime polymorphism
- Interfaces and multiple interface implementation
- Aggregation and composition
- `this`, `super`, `final`, and `protected`
- Static members
- Packages
- Enums
- Generics and collections
- Exception handling
- File handling
- Swing/AWT event handling and layout managers

## Run in VS Code
Compile from the project root:

```bash
javac -d out $(find src -name "*.java")
```

On Windows PowerShell:

```powershell
javac -d out (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName })
```

Run:

```bash
java -cp out com.smartbank.Main
```

Default login:
- Username: `admin`
- Password: `admin123`
