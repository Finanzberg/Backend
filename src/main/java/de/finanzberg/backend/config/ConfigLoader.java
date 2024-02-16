package de.finanzberg.backend.config;

import de.finanzberg.backend.config.serializer.EnumSerializer;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class ConfigLoader {

    private static final Map<Path, YamlConfigurationLoader> LOADER_CACHE = new HashMap<>();

    private ConfigLoader() {
    }

    public static <T extends AbstractConfigurationLoader.Builder<T, ?>> T setDefaultSerializers(T loaderBuilder) {
        return loaderBuilder.defaultOptions(opts -> opts.serializers(builder -> builder
                .register(EnumSerializer.INSTANCE)));
    }

    public static YamlConfigurationLoader createYamlLoader(Path path) {
        return LOADER_CACHE.computeIfAbsent(path, $ -> setDefaultSerializers(YamlConfigurationLoader.builder())
                .path(path).nodeStyle(NodeStyle.BLOCK).indent(2).build());
    }

    public static <T> T loadYamlObject(Path path, Class<T> clazz) {
        try {
            YamlConfigurationLoader loader = createYamlLoader(path);
            T obj;

            if (Files.exists(path)) {
                obj = loader.load().get(clazz);
                Objects.requireNonNull(obj, "Yaml object at " + path + " is null");
            } else {
                try {
                    Constructor<T> ctor = clazz.getDeclaredConstructor();
                    ctor.setAccessible(true);
                    obj = ctor.newInstance();
                } catch (ReflectiveOperationException exception) {
                    throw new RuntimeException(exception);
                }
            }

            CommentedConfigurationNode node = loader.createNode();
            node.set(clazz, obj);

            loader.save(node);
            return obj;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static <T> void saveYamlObject(Path path, T obj) {
        try {
            YamlConfigurationLoader loader = createYamlLoader(path);
            CommentedConfigurationNode node = loader.createNode();
            node.set(obj.getClass(), obj);
            loader.save(node);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}