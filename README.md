## Description ##
Generic Game Chat Bot easy extendable.

A Discord Bot that will listens on a channel and return chat/images according to selected game and command.

##Example: ##
- \g Zombicide

will start new game Zombicide
- \g drawEquipment Card

draws an equipment Card from Zombicide and shows it in channel.

## Setup ##
run maven clean install to generate image lists
enter you discord secret_token into application.yml or into environment Variable SECRET_TOKEN 

##Extension ##
####new Game####
- add Class extending Game interface
####new Method####
- add Method with GameMethod annotation
####new Images####
- add images under resources static/$gamename/$deckname
- extend maven ant plugin to generate image lists on build.

## Todo ##
Webpage alternative for discord.
GameMethod Anotation should define how to handle that response e.g. print as Image or print as Message

## About ##
I am GameBot builded during outbreak of Covid-19 to help playing boardgames online while isolated.