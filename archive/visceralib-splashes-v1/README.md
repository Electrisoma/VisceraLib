# VisceraLib Splashes (v1)

VisceraLib Splashes (v1) provides a unified, multi-loader system for managing Minecraft splash texts.
While NeoForge offers native splash modification,
Fabric lacks a built-in way to interact with the splash pool without overwriting vanilla files.
This module bridges that gap and adds a powerful event-driven layer for dynamic splash logic.

## How to use

### 1. Basic Implementation (Resource Packs)

The simplest way to add splashes is via a standard resource pack. 
Unlike vanilla Fabric, which requires you to overwrite the entire splashes.txt file,
VisceraLib merges all found splash files into one master pool.

#### Create the Splash File

Create a text file at the following path in your resource pack:
`assets/<modid>/texts/splashes.txt`

#### File Format
The format follows standard Minecraft conventions. 
Each line is a new splash. Lines starting with `#` are ignored as comments.

```Plaintext
# Splash Pack
Mankind is Dead
Blood is Fuel
Hell is Full
```