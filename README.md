# AirportSubway
A project to manage the flight routes I will follow for Flight Sim

The current codebase contains a simple Airport Creator, which will create a basic airport node
An airport node has a list of other airports it can reach, distances to said airport(s), callsign, and flight number.

It also contains a non-fully functioning path route finder
TODO is to make some kind of path finder work

Planned feature would be to make some kind of automatic route creator, where you would feed in maybe an Origin and Destination airport, runway length, max node distance, etc. and it would create a route.

The current method of storing airports is under the Data folder. There are V1 and V2 folders which are old and I have yet to purge.
A current partial set of airports is under Data/Snowpiercer, as I have been using my route inspired by it, and creating my route with this code.
The data under the snowpiercer folder are all AirportNode, stored in a subfolder according to their country.
If this program reaches a point of having a too many airports (and I think I may be getting there now), I will reformat the folder structure to break up the folders a bit more.
I have moved some stuff around a bit, so some of the old versions remaining may not work. I do plan on removing the old versions some time soon

A real good chunk of this code was made with GitHub CoPilot, I am really loving it.

The on-disk dataset being used for Airports and Runways was sourced from https://ourairports.com under Public Domain
Retrieved estimated Mar 31, 2022 +- a couple days. Mostly just because I see it changes relatively frequently, and should be updated from time to time.
