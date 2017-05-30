package cc.snser.test.yulai;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Snser on 2017/4/13.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SnserAnnotation {

    int id();

    String label() default "label@snser";

    String forceLabel();

}
