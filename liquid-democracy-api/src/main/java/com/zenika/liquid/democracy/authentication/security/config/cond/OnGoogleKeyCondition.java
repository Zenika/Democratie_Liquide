package com.zenika.liquid.democracy.authentication.security.config.cond;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

/**
 * @author Guillaume Gerbaud
 */
public class OnGoogleKeyCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(ConditionnalOnGoogleKey.class.getName()));
        boolean negate = annotationAttributes.getBoolean("negate");

        String property = context.getEnvironment().getProperty("spring.social.google.appId");
        boolean isPresent = false;

        if(StringUtils.hasText(property)) {
            isPresent = true;
        }

        if(negate) {
            isPresent = !isPresent;
        }

        return isPresent;
    }

}
