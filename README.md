# Droplets

Droplets provides an easy way to write Bukkit microplugins. Each droplet is a single class and can access
Bukkit's powerful API. Their simplicity allows for easy development and debugging. Droplets are perfect for those
tiny features that don't need their own plugins.

More to come.

## Building

### Dependencies
* Apache Ant
* Bukkit

To build Droplets, make a `lib` directory and place the [Bukkit API](https://dl.bukkit.org) jar you'd like
to target in it. Next, run the following:

    ant dist

`Droplets.jar` will be built in the `dist` directory. To build with debugging symbols, run:

    ant build-debug jar

Lastly, to clean your build directory, run:

    ant clean
