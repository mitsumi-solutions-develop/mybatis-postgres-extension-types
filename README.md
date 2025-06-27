# mybatis-postgres-extension-types
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white) [![Licence](https://img.shields.io/github/license/Ileriayo/markdown-badges?style=for-the-badge)](./LICENSE)

# supported

- java version: 21

# supported postgres extension types

- Enum
- Json
- UUID

# dependency

## maven

```xml
<dependency>
    <groupId>io.github.mitsumi-solutions-develop</groupId>
    <artifactId>mybatis-postgres-extension-types</artifactId>
    <version>1.0.0</version>
</dependency>
```

# usage for spring

```java

    private final MybatisCustomTypeConfig customTypeConfig;

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> {
            var registry = configuration.getTypeHandlerRegistry();

            types(customTypeConfig.getJsonModelsPackage(), Class::isEnum).forEach(clazz ->
                registry.register(clazz, JsonTypeHandler.class)
            );

            types(customTypeConfig.getEnumerationsPackage()).forEach(clazz ->
                registry.register(clazz, EnumTypeHandler.class)
            );

            registry.register(UUIDTypeHandler.class);
        };
    }

    @SafeVarargs
    @SneakyThrows
    protected final List<Class<?>> types(String packageName, Function<Class<?>, Boolean>... ignored) {
        return  ClassPath.from(ClassLoader.getSystemClassLoader())
            .getAllClasses()
            .stream()
            .filter(clazz -> clazz.getPackageName().equalsIgnoreCase(packageName))
            .map(ClassPath.ClassInfo::load)
            .filter(clazz -> {
                if (ignored != null && ignored.length > 0) {
                    return Stream.of(ignored).noneMatch(f -> f.apply(clazz));
                }

                return true;
            })
            .collect(Collectors.toList());
    }

```
