package vn.iotstar.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class ValidPriceValidator implements ConstraintValidator<ValidPrice, BigDecimal> {

    @Override
    public void initialize(ValidPrice constraintAnnotation) {
        // Không cần khởi tạo gì thêm
    }

    @Override
    public boolean isValid(BigDecimal price, ConstraintValidatorContext context) {
        if (price == null) {
            return false;
        }
        return price.compareTo(BigDecimal.ZERO) > 0;
    }
}
