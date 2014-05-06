package com.s0lder.droplets.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandHandler {
    String name();
    String[] aliases() default {};
    String[] description() default {};
    String[] help() default {};
}
