# Developer

## Build

For now, since I do not deploy the maven artificacts, I am using a Intellij workspace solution.

I have [Hunspell2WordList](https://github.com/isirode/Hunspell2WordList) and this project at the same level, in a folder.

The folder is an Intellij project, I added both modules in this project.

If you have build issues, make sure you add them as Maven modules, and to use JDK 16 (adopt-openjdk-16).

You also need to put the .aff and .dic in the resources/data folder of the project.

You can find them in the [0.0.1](https://github.com/isirode/HunspellSQLiteGenerator/releases/tag/0.0.1) release.

## TODO

- [ ] Use a command line system for the app
- [ ] Try supporting another dictionary
- [ ] Add the license of the database in the database

## Features

- [x] Creating a list of sequences that the words are containing
  - [x] Indicate the number of occurrences of this sequences in the dictionary
  - [ ] Pass the length of the sequence as an argument
- [x] Insert the words that were generated in the SQLite database
  - [ ] Customize the word filters using a command argument
