## Description ##
Generic Game Chat Bot easy extendable.
Example Game Zombicide

- A Discord Bot that will listens on a channel and return chat/images according to selected game and command.
- A Website that will print bot chat of a channel to use as streaming overlay.


## Example: ##
### Discord: ###
- \g : shows avaiable commands depending on selected game.


- \g new zombicide : will start new game Zombicide
### Website: ###
- localhost:8090 : 
 - - shows all avaiable games
 - - you can login with discord, 
 - - view streamOverlay.

## Setup ##
- run maven clean install to generate image lists
- enter you discord secret_token into application.yml or into environment Variable SECRET_TOKEN
- StreamingOverlay: localhost:8090/?channelId=$yourchannelId 

## Extension ##
#### New Game ####
- add Class extending Game interface
#### New Method ####
- add Method with GameMethod annotation
#### New Images ####
- add images under resources static/$gamename/$deckname
- extend maven ant plugin to generate image lists on build.

## About ##
I am GameBot builded during outbreak of Covid-19 to help playing boardgames online while isolated.
