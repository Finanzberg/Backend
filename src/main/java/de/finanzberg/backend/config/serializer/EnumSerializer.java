package de.finanzberg.backend.config.serializer;

import io.leangen.geantyref.GenericTypeReflector;
import io.leangen.geantyref.TypeToken;
import org.spongepowered.configurate.serialize.ScalarSerializer;

import java.lang.reflect.Type;
import java.util.function.Predicate;

public final class EnumSerializer extends ScalarSerializer<Enum<?>> {

    public static final EnumSerializer INSTANCE = new EnumSerializer();

    @SuppressWarnings("Convert2Diamond") // can't do this because of type tokens
    private EnumSerializer() {
        // @formatter:off
        super(new TypeToken<Enum<?>>() {});
        // @formatter:on
    }

    @SuppressWarnings({"unchecked", "RedundantSuppression"})
    @Override
    public Enum<?> deserialize(Type type, Object obj) {
        return Enum.valueOf(GenericTypeReflector.erase(type).asSubclass(Enum.class), obj.toString());
    }

    @Override
    protected Object serialize(Enum<?> item, Predicate<Class<?>> typeSupported) {
        return item.name();
    }
}
