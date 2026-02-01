package com.everest.hibiscus.api.modules.compat.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.TYPE,
        ElementType.FIELD,
        ElementType.METHOD
})
public @interface RequiresMod {

    /**
     * Primary modid (backwards-compat).
     * Equivalent to mods = { value }.
     */
    String mod();

    /**
     * Optional list of modids.
     * If present, this supersedes 'value'.
     */
    String[] mods() default {};

    /**
     * Version constraints for each mod in mods().
     * - Must match length of mods()
     * - Empty string means "any version"
     */
    String[] versions() default {};

    /**
     * Whether ALL or ANY mods must be present.
     */
    Policy policy() default Policy.ANY;

    /**
     * Optional explanation shown in the error.
     */
    String reason() default "";

    enum Policy {
        ANY,   // At least one mod present
        ALL,   // All mods present
        NONE   // None of the mods present
    }
}
