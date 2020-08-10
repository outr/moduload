# moduload
Runtime module loading for Scala

## Simple usage:

- Create a class that mixes in the trait `Moduload` and defines an implementation for the `load()` method.
- Create a `moduload.list` file in the `resources` path with the full class name of the module (ex. com.company.MyModule).
- Invoke `Moduload.load()` to automatically pick up all `moduload.list` files and load them.