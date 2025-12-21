# VisceraLib

Multiproject library for general ease of development within a multiloader environment.
This library can be used in single loader projects as well for its unique APIs, but it is primarily for multiloader.

VisceraLib is meant to provide:
- Abstraction between modloader APIs.
- Implementations of Minecraft or modloader features for easier development.
- General cleanliness of code by handling things mostly in common.

This project is licensed under [MIT](LICENSE.md)

> _This is primarily created for my own personal use 
> and I feel it is only fair to hold little restrictions for others in such context._\
> _This is meant to help me, and if other projects want to use it to help them 
> in whatever way possible, then it has succeeded._

## Usage

Here's how to import VisceraLib into your project:

<details>
<summary>Multiloader</summary>

### Architectury Multiloader:

Kotlin DSL
```kotlin
listOf(
    "core",
    "registration",
    "modelloader"
).forEach { module ->
    "net.electrisoma.visceralib:visceralib-$module:${visceralib_version}+mc${minecraft_version}-common".let {
        modImplementation(it)
    }
}
```

Groovy DSL
```groovy
Set<String> modules = [
        "core",
        "registration",
        "modelloader"
]

modules.each {
    def dep = "net.electrisoma.visceralib:visceralib-${it}:${visceralib_version}+mc${minecraft_version}-common"
    modImplementation(dep)
}
```

### Jared's Multiloader Template:

Kotlin DSL
```kotlin
listOf(
    "core",
    "registration",
    "modelloader"
).forEach { module ->
    "net.electrisoma.visceralib:visceralib-$module:${visceralib_version}+mc${minecraft_version}-common".let {
        implementation(it)
    }
}
```

Groovy DSL
```groovy
Set<String> modules = [
        "core",
        "registration",
        "modelloader"
]

modules.each {
    def dep = "net.electrisoma.visceralib:visceralib-${it}:${visceralib_version}+mc${minecraft_version}-common"
    implementation(dep)
}
```

</details>

<details>
<summary>Fabric</summary>

### Loom

Kotlin DSL
```kotlin
listOf(
    "core",
    "registration",
    "modelloader"
).forEach { module ->
    "net.electrisoma.visceralib:visceralib-$module:${visceralib_version}+mc${minecraft_version}-fabric".let {
        modImplementation(it)
    }
}
```

Groovy DSL
```groovy
Set<String> modules = [
        "core",
        "registration",
        "modelloader"
]

modules.each {
    def dep = "net.electrisoma.visceralib:visceralib-${it}:${visceralib_version}+mc${minecraft_version}-fabric"
    modImplementation(dep)
}
```

</details>

<details>
<summary>NeoForge</summary>

### MDG

Kotlin DSL
```kotlin
listOf(
    "core",
    "registration",
    "modelloader"
).forEach { module ->
    "net.electrisoma.visceralib:visceralib-$module:${visceralib_version}+mc${minecraft_version}-neoforge".let {
        implementation(it)
    }
}
```

Groovy DSL
```groovy
Set<String> modules = [
        "core",
        "registration",
        "modelloader"
]

modules.each {
    def dep = "net.electrisoma.visceralib:visceralib-${it}:${visceralib_version}+mc${minecraft_version}-neoforge"
    implementation(dep)
}
```

</details>

## Contributing

If you feel you have something to contribute then I would be happy to accept meaningful contributions 
for the sake of furthering this project.

> _This section may receive updates with time._

## Modules

Each of these modules has some amount of documentation attached to it via a `README.md` file 
(similar to the one you're reading right now).
This is to keep things tidy and clean and to give a general concept of what's inside the module.

> _This section will have all modules and associated `README.md` files documented for reference._

### - VisceraLib-Core
See [VisceraLib-Core README](visceralib-core/README.md)

### - VisceraLib-Registration (half-working)
See [VisceraLib-Registration README](visceralib-registration/README.md)

### - VisceraLib-ModelLoader (not-working)
See [VisceraLib-ModelLoader README](visceralib-modelloader/README.md)