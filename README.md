# Weave Distance Calculator

This is a Minecraft mod that provides a command called "distance" to calculate the distance between two sets of coordinates using either the Euclidean or Manhattan method. This mod is designed for the Weave Loader platform.

## Table of Contents

- [Usage](#usage)
- [Installation](#installation)
- [Commands](#commands)
- [License](#license)

## Usage

1. Press `T` to open the chat.
2. Type `/distance x1 y1 z1 x2 y2 z2 method`, where:
   - `x1`, `y1`, `z1` are the coordinates of the first point.
   - `x2`, `y2`, `z2` are the coordinates of the second point.
   - `method` is either "euclidean" or "manhattan" to specify the distance calculation method.

For example, to calculate the Euclidean distance between two points with coordinates (392, -43, 81) and (48, 293, 58), you would use the following command:

```
/distance 392 -43 81 48 293 58 euclidean
```

To calculate the Manhattan distance for the same points, use:

```
/distance 392 -43 81 48 293 58 manhattan
```

## Installation

1. Install [Weave](https://github.com/Weave-MC/Weave-Loader)
2. Download the latest jar and put it in `~/.weave/mods`
- '~' is equivalent to %userprofile% on windows

## Commands

- `/distance x1 y1 z1 x2 y2 z2 method`: Calculate the distance between two sets of coordinates using either the Euclidean or Manhattan method. Replace `x1`, `y1`, `z1`, `x2`, `y2`, `z2`, and `method` with the appropriate values as described in the [Usage](#usage) section.

## License

This mod is open-source and available under the [GNU General Public v3.0](LICENSE) License. You are free to use, modify, and distribute this mod as long as you follow the terms of the license. See the [LICENSE](LICENSE) file for more details.
