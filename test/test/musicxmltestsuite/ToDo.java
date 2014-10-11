package musicxmltestsuite;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ToDo {

	String value() default "";
	
}
