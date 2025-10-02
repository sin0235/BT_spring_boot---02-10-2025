package vn.iotstar.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidPriceValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPrice {
    String message() default "Giá sản phẩm phải lớn hơn 0";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
