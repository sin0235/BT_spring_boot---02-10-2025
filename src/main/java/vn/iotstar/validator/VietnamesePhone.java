package vn.iotstar.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = VietnamesePhoneValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface VietnamesePhone {
    String message() default "Số điện thoại không hợp lệ (phải bắt đầu bằng 0 và có 10-11 chữ số)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
