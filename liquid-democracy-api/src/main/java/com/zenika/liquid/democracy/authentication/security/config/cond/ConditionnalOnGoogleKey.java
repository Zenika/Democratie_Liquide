package com.zenika.liquid.democracy.authentication.security.config.cond;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Guillaume Gerbaud
 */
@Conditional(OnGoogleKeyCondition.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface ConditionnalOnGoogleKey {

    boolean negate() default false;
}
