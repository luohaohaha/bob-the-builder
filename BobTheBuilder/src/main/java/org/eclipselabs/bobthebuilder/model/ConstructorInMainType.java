package org.eclipselabs.bobthebuilder.model;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

@BindingAnnotation
@Target({ java.lang.annotation.ElementType.FIELD,
    java.lang.annotation.ElementType.PARAMETER,
    java.lang.annotation.ElementType.METHOD })
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface ConstructorInMainType {}
