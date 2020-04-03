[![Logo](assets/logo.png)](http://fvcproductions.com)

# Sync
[![License](https://img.shields.io/badge/license-MIT-green)](https://opensource.org/licenses/MIT)

A rhytm game clone of Stepmania designed for mobile use.

## Status

This project is under heavy development and in an unstable state. For this reason contributions are
restricted to bug-fixes of the already present source code.

## Project structure

This project uses the [libGDX](https://libgdx.badlogicgames.com/) game development framework and the project
structure follows the guidelines of this framework. Most of the game source code is contained into the 
[core](core) sub-project which is extended by each backend to add platform-specific implementation. 

The complete list of sub-projects:

- [core](core): the game core sub-project extended by all backends.
- [android](android): the Android backend sub-project.
- [desktop](desktop): the desktop backend sub-project.
- [ios](ios): the IOS backend sub-project.

## Building from sources

Sync uses [Gradle](https://gradle.org/) as its build tool.

The following components are required to get and build the source code:

- [Git](https://git-scm.com/) to obtain the source code.
- Java JDK 1.8 to build the source code.

### Downloading the source code

Cloning the repository:

```shell
git clone https://github.com/vinsfortunato/sync
cd sync
```

### Building 

Building for Desktop:

```shell
gradlew :desktop:build
```

Building for Android:

```shell
gradlew :android:build
```

### Running

You can run the game with the gradle run task.

Running on Desktop:

```shell
gradlew :desktop:run
```

Running on Android:

```shell
gradlew :android:run
```

## Licence
The project is licensed under the [MIT licence](https://opensource.org/licenses/MIT). You can do anything you
want with the project's code as long as you include the original copyright and license notice in all copies 
or substantial portions of this software.