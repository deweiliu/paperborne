# Paperborne
## Introduction
This is a project that our team of five did in level 2 at Queen's University Belfast. It is a [Hearthstone](https://en.wikipedia.org/wiki/Hearthstone)-like card game that users can battle with our built-in AI.

## To check out the game
As we are trying to publish the game on Google Play, at the moment, you can only play it on an Android Virtual Device (AVD).
### Prerequisites
* [Android Studio](https://developer.android.com/studio/)
* Have this project downloaded on the computer.
### Steps
1. Open the project with Android Studio
2. Build the project in Android Studio. (If it prompts you for installing any plug-ins, please install them and build again)
3. Run the project with any AVD.

## Options on Home page
* Help: button at the bottom-right corner
* Sound: button at the bottom-left corner
* Settings: button at the top-left corner
* To play: the Singleplayer button
![home](/screenshots/home.png)

## How to play
Both the user and the enemy (controller by AI) have cards in hands with values shown in 
* ![#1589F0](https://placehold.it/15/1589F0/000000?text=+) blue: Mana. The card costs mana when it is played.
* ![#c5f015](https://placehold.it/15/c5f015/000000?text=+) green: Health. When the card is attacked, the health value is reduced by the attack value of the attacking card. When the health value turns to 0 or less, the card will be destroyed.
* ![#f03c15](https://placehold.it/15/f03c15/000000?text=+) red: Attack. When this card attacks others, it reduces their health by this value.

At the beginning of every turn, the player can get a card added to the hand. Players can attack enemy hero or its cards with the active cards on the battlefield. The player wins when the enemy hero's health value turns to 0 or less. Vice versa.
### Play a card 
Play a card by dragging it from your hand to the battlefield. It costs the mana shown on the card. The blue bar at the bottom-right shows the mana you have. It will be first highlighted in grey because you cannot use it at the first turn you play it.
![play_card](/screenshots/play_card.png)

### Select an attacking card
When you select a card, it will be highlighted in blue. You can now select a target card or the enemy hero to attack.
![Attacking_card](/screenshots/Attacking_card.png)

### Being attacked
When a card's health value turns to 0 or less, it will be highlighted in red, and will be destroyed at the end of the turn.
![kill_card](/screenshots/kill_card.png)

### Result
If your health value turns to 0 or less, you lose the game. Vice versa.
![result](/screenshots/result.png)

### Record your name
![record_name](/screenshots/record_name.png)
### History
Then your records and winning ratio will be calculated and shown, based on the name you have provided. It also shows the top 3 players with the highest winning ratios.
![history](/screenshots/history.png)

## Settings
![Settings](/screenshots/Settings.png)

## Help
![Help information](/screenshots/Help.png)
