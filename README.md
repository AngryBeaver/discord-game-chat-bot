## Description ##
Generic Game Chat Bot easy extendable.

- A Discord Bot that will listens on a channel and return chat/images according to selected game and command.
- A Website that will print bot chat of a channel to use as streaming overlay.



## Example: ##
- \g Zombicide

will start new game Zombicide
- \g drawEquipment Card

draws an equipment Card from Zombicide and shows it in channel.

## Setup ##
run maven clean install to generate image lists
enter you discord secret_token into application.yml or into environment Variable SECRET_TOKEN
StreamingOverlay: localhost:8090/?channelId=$yourchannelId 

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