package com.comsysto.metagrapher.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface MetagrapherService {
    String value();
    MetagrapherServiceType type() default MetagrapherServiceType.CONTEXT_DEPENDENT;
    String[] tags() default {};
}
