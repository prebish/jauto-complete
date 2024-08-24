
# AutoComplete Implementation

A Java-based autocomplete system using a DLB Trie for fast word predictions and insertions, designed for efficient text completion.

### Table of Contents

- [Installation & Setup](#installation--setup)
    1. [Clone the Repository](#1-clone-the-repository)
    2. [Compile the Program](#2-compile-the-program)
- [Usage](#usage)
    - [Menu Options](#menu-options)
- [License](#license)

## Installation & Setup

Ensure that you have the latest version of the Java Development Kit installed.  
You can download it [<u>**here**</u>](https://www.oracle.com/java/technologies/downloads/).

### 1. Clone the Repository
```bash
git clone https://github.com/prebish/autocomplete-implementation.git
```

### 2. Compile the Program
```bash
cd ./autocomplete-implementation
javac -d build ./src/*.java
```

## Usage

Run the program using the following command:

```bash
java -cp build src.Main
```

### Menu Options

Upon running the program, you will be presented with the following menu options:

*********************************
1. **Advance a character**: Add a character to the running prefix.
2. **Retreat a character**: Remove the last character from the running prefix.
3. **Reset the prefix**: Reset the running prefix to an empty string.
4. **Check if current prefix is a word**: Verify if the current prefix is a valid word in the dictionary.
5. **Add current prefix as a new word**: Add the current prefix as a new word to the dictionary.
6. **Get number of predictions**: Retrieve the number of word predictions based on the current prefix.
7. **Retrieve one prediction**: Retrieve a single word prediction based on the current prefix.
8. **Retrieve all predictions**: Retrieve all possible word predictions based on the current prefix.
9. **Delete a word**: Remove a word from the dictionary.
10. **Exit**: Exit the program.
*********************************

Simply enter the number corresponding to your desired option and follow the prompts.

## License
This project is licensed under the **MIT License** - see the `LICENSE` file for details.
