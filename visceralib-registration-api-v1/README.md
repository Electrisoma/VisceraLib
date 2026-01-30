# VisceraLib Registration API (v1)

VisceraLib Registration API (v1) provides a unified, multi-loader registration layer. 
It allows you to define your content once in your common project, 
eliminating the need to write boilerplate registration code for each individual modloader.

## How to use

### 1. Basic Implementation

Setting up registration is straightforward. 
You can start by initializing a registration helper in your mod's constants or a utility class.

#### Define the Helper
```java
public class Constants {

    public static final String MOD_ID = "my_mod";

    // Initialize the helper once
    public static final VisceralRegistrationHelper REGISTRY =
            VisceralRegistrationHelper.of(MOD_ID);
}
```

#### Registering Content
```java
public class ModItems {

    public static final VisceralRegistrationHelper REGISTRY = Constants.REGISTRY;

    public static RegistryObject<Item> MY_ITEM = REGISTRY.item(
            "my_item",
            () -> new Item(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1))
    );

/** 
 * Also compatible with 1.21.11 formatting
 * public static RegistryObject<Item> MY_ITEM = REGISTRY.item(
 *         "my_item",
 *         Item::new,
 *         () -> new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)
 * );
**/

    // Required to force class loading
    public static void init() {}
}
```

### 2. Advanced: Fluent Builders

If you require more flexibility you can extend `AbstractRegistrationHelper` 
to provide more specific helpers.

In our case we'll be setting up fluent builders by extending `AbstractRegistrationHelper`
to return custom builders instead of raw RegistryObjects.

#### Creating a Custom Helper
```java
public class MyRegistrationHelper extends AbstractRegistrationHelper<MyRegistrationHelper> {

    // ... constructor and of() method ...

    // Returns a builder instead of a RegistryObject immediately    
    public <T extends Item> ItemBuilder<T, TestRegistrationHelper> item(
            String name,
            Function<Item.Properties, T> factory
    ) {
        return new ItemBuilder<>(this, name, factory);
    }
}
```

#### Defining the builder
The builder captures configurations (like properties) 
and defers the actual registration until `register()` is called.
```java
public class ItemBuilder<T extends Item, H extends AbstractRegistrationHelper<?>> {

    private final H helper;
    private final String name;
    private final Function<Item.Properties, T> factory;
    private UnaryOperator<Item.Properties> propertyModifier = op -> op;

    public ItemBuilder(H helper, String name, Function<Item.Properties, T> factory) {
        this.helper = helper;
        this.name = name;
        this.factory = factory;
    }

    public ItemBuilder<T, H> properties(UnaryOperator<Item.Properties> op) {
        this.propertyModifier = this.propertyModifier.andThen(op);
        return this;
    }

    public RegistryObject<T> register() {
        return helper.register(BuiltInRegistries.ITEM, name, () ->
                factory.apply(propertyModifier.apply(new Item.Properties()))
        );
    }
}
```

#### Implementing the builder
Now with this new builder style registration helper we can revisit our item registration:
```java
public class ModItems {

    public static final MyRegistrationHelper REGISTRY = Constants.REGISTRY;

    public static RegistryObject<Item> MY_ITEM = REGISTRY
            .item("my_item", Item::new)
            .properties(p -> p.rarity(Rarity.EPIC))
            .properties(p -> p.stacksTo(1))
            .register();

    // Required to force class loading
    public static void init() {}
}
```

### 3. Final integration
Finally, ensure your static fields are initialized during your mod's entry point 
(e.g., the `ModInitializer` on Fabric or the constructor on NeoForge).

#### Main Mod Class
```java
public class MyMod {

    public static void init() {
        // Calling `init()` forces the JVM to load the class and process static fields
        ModItems.init();
        // ModBlocks.init();
        // ModEntities.init();
        // etc
    }
}
```

#### Loader Entrypoints
Call your common `init()` during the earliest possible phase of each loader.

#### NeoForge
```java
@Mod(Constants.MOD_ID)
public class MyModNeoForge {

    public MyModNeoForge(IEventBus modBus) {
        MyMod.init();
    }
}
```

#### Fabric
```java
public class MyModFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        MyMod.init();
    }
}
```