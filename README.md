# DH Blackjack

A simple game of Blackjack written in Java. Uses the command-line interface for gameplay.

## Overview

The project contains `src`, `test` and `res` folders for source code, JUnit test cases and other resources (e.g. configuration file), respectively. Unit tests were written against JUnit 4.

## How to Play

The game requires no external dependencies to be played. One can download the code, edit the config file (if desired), compile and run the code to play.
The config file is located at `res/Blackjack.cfg`. It contains various adjustable game parameters and their description.

From the command line, in the project directory, the code can be compiled as follows:
```
mkdir cls
javac -d cls src/*
```

Then the game can be run as follows:
```
java -cp cls BlackjackGame res/Blackjack.cfg
```

## Screenshot

```
Welcome to DH Blackjack!

Please enter a number of players  [2 to 7]: 3
Player 0, please enter your bet [1 to 1000]: 1000
Player 1, please enter your bet [1 to 1000]: 500
Player 2, please enter your bet [1 to 1000]: 250
Two cards will now be dealt for each player.
Press Enter to continue...

***************************************
***********   DH Blackjack   **********
***************************************
Dealer
	♠A ???
Player 0
	Cash balance: $0
	♦7 ♣A	Value: 18	Wager: $1000
Player 1
	Cash balance: $500
	♥2 ♥3	Value: 5	Wager: $500
Player 2
	Cash balance: $750
	♥J ♦2	Value: 12	Wager: $250
***************************************

Player 0, would you want to hit or stand for this hand?
	♦7 ♣A	Value: 18	Wager: $1000
 [h, s]: s

***************************************
***********   DH Blackjack   **********
***************************************
Dealer
	♠A ???
Player 0
	Cash balance: $0
	♦7 ♣A	Value: 18	Wager: $1000
Player 1
	Cash balance: $500
	♥2 ♥3	Value: 5	Wager: $500
Player 2
	Cash balance: $750
	♥J ♦2	Value: 12	Wager: $250
***************************************

Player 1, would you want to double down for this hand?
	♥2 ♥3	Value: 5	Wager: $500
 [y, n]: y
```
