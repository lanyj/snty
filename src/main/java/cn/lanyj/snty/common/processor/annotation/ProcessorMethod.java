package cn.lanyj.snty.common.processor.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.lanyj.snty.common.processor.Processor;

/**
 * used for annotation processor, which method must match with on of
 * {@link Processor} methods signature
 * 
 * @see Processor
 * @see AnnotationBasedProcessor
 * @author lanyj
 *
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProcessorMethod {
	int order() default 0;
}
