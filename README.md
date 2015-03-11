Mega Man 2: Java Remix
=====

Faithful recreation of Mega Man 2 in Java (always a work in progress).

Building and Running
-----
```
./compile.sh && ./run.sh
```

On Windows, use Git Bash terminal or something similar.

Development
-----
We are using [Tiled Map Editor](http://www.mapeditor.org) to create the tile maps.

Every **tileset** in res/images/tilesets/ has a corresponding **mapping** file.
This file specifies the type and attributes of various all tiles in that tileset.

Tileset Mapping Syntax
-----
```
{
  "tile_types": [
    [ID, ID, ...],
    [ID, ID, ...],
    ...
  ]
}
```

where each `ID` matches one of the following known IDs, and the number of rows/columns in the array matches
the number of rows/columns of tiles in the corresponding tileset.

**Format:** ID => type

* 0 => background
* 10 => foreground, simple (e.g. ground, wall)
* 20 => ladder

Contributors
-----
* Joshua Selbo (@jselbo)
* Hunter Martin (@hunterbmartin)
