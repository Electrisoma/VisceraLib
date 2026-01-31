<p style="text-align: center;">
  <img src="./branding/big/logo.png" alt="Logo" width="256"><br>
  <span style="font-size: 2em; font-weight: bold;">VisceraLib</span><br>
  <a href="https://github.com/Electrisoma/VisceraLib/blob/main/LICENSE">
    <img src="https://img.shields.io/github/license/Electrisoma/VisceraLib?style=flat&color=900c3f" alt="License">
  </a>
</p>

Multiproject library for general ease of development within a multiloader environment.
This library can be used in single loader projects as well for its unique APIs, but it is primarily for multiloader.

VisceraLib is meant to provide:
- Abstraction between modloader APIs.
- Implementations of Minecraft or modloader features for easier development.
- General cleanliness of code by handling things mostly in common.

This project is licensed under [MIT-0](LICENSE)

> [!NOTE]
> _This is primarily created for my own personal use 
> and I feel it is only fair to hold little restrictions for others in such context._\
> _This is meant to help me, and if other projects want to use it to help them 
> in whatever way possible, then it has succeeded._

## Usage

> [!WARNING]
> This library has yet to be released.

Here's how to import VisceraLib into your project:

<details>
<summary>Multiloader</summary>

### Architectury Multiloader:

Kotlin DSL

```kotlin
modImplementation("net.electrisoma.visceralib:visceralib:${visceralib_version}-common")
```

Groovy DSL

```groovy
modImplementation "net.electrisoma.visceralib:visceralib:${visceralib_version}-common"
```

or

Kotlin DSL

```kotlin
listOf(
    "core",
    "registration-api-v1",
    "item-hooks-v1"
).forEach { module ->
    "net.electrisoma.visceralib:visceralib-$module:${visceralib_version}-common".let {
        modImplementation(it)
    }
}
```

Groovy DSL
```groovy
Set<String> modules = [
        "core",
        "registration-api-v1",
        "item-hooks-v1"
]

modules.each {
    def dep = "net.electrisoma.visceralib:visceralib-${it}:${visceralib_version}-common"
    modImplementation(dep)
}
```

### Jared's Multiloader Template:

Kotlin DSL

```kotlin
implementation("net.electrisoma.visceralib:visceralib:${visceralib_version}-common")
```

Groovy DSL

```groovy
implementation "net.electrisoma.visceralib:visceralib:${visceralib_version}-common"
```

or

Kotlin DSL
```kotlin
listOf(
    "core",
    "registration-api-v1",
    "item-hooks-v1"
).forEach { module ->
    "net.electrisoma.visceralib:visceralib-$module:${visceralib_version}-common".let {
        implementation(it)
    }
}
```

Groovy DSL
```groovy
Set<String> modules = [
        "core",
        "registration-api-v1",
        "item-hooks-v1"
]

modules.each {
    def dep = "net.electrisoma.visceralib:visceralib-${it}:${visceralib_version}-common"
    implementation(dep)
}
```

</details>

<details>
<summary>Fabric</summary>

### Loom

Kotlin DSL

```kotlin
modImplementation("net.electrisoma.visceralib:visceralib:${visceralib_version}-fabric")
```

Groovy DSL

```groovy
modImplementation "net.electrisoma.visceralib:visceralib:${visceralib_version}-fabric"
```

Kotlin DSL
```kotlin
listOf(
    "core",
    "registration-api-v1",
    "item-hooks-v1"
).forEach { module ->
    "net.electrisoma.visceralib:visceralib-$module:${visceralib_version}-fabric".let {
        modImplementation(it)
        include(it)
    }
}
```

Groovy DSL
```groovy
Set<String> modules = [
        "core", 
        "registration-api-v1", 
        "item-hooks-v1"
]

modules.each {
    def dep = "net.electrisoma.visceralib:visceralib-${it}:${visceralib_version}-fabric"
    modImplementation(dep)
    include(dep)
}
```

</details>

<details>
<summary>NeoForge</summary>

### MDG

Kotlin DSL

```kotlin
implementation("net.electrisoma.visceralib:visceralib:${visceralib_version}-neoforge")
```

Groovy DSL

```groovy
implementation "net.electrisoma.visceralib:visceralib:${visceralib_version}-neoforge"
```

Kotlin DSL
```kotlin
listOf(
    "core",
    "registration-api-v1",
    "item-hooks-v1"
).forEach { module ->
    "net.electrisoma.visceralib:visceralib-$module:${visceralib_version}-neoforge".let {
        implementation(it)
        jarJar(it)
    }
}
```

Groovy DSL
```groovy
Set<String> modules = [
        "core",
        "registration-api-v1",
        "item-hooks-v1"
]

modules.each {
    def dep = "net.electrisoma.visceralib:visceralib-${it}:${visceralib_version}-neoforge"
    implementation(dep)
    jarJar(dep)
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

### - VisceraLib Core
See [VisceraLib-Core README](visceralib-core/README.md)

### - VisceraLib Item Hooks (v1)
See [VisceraLib-Registration README](visceralib-item-hooks-v1/README.md)

### - VisceraLib Registration API (v1)
See [VisceraLib-Registration README](visceralib-registration-api-v1/README.md)