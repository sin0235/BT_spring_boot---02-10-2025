package vn.iotstar.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class VietnamesePhoneValidator implements ConstraintValidator<VietnamesePhone, String> {

    private static final String PHONE_PATTERN = "^0[0-9]{8,9}$";

    private static final Pattern pattern = Pattern.compile(PHONE_PATTERN);

    @Override
    public void initialize(VietnamesePhone constraintAnnotation) {
        // Không cần khởi tạo gì thêm
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null || phone.isEmpty()) {
            return true; // Cho phép null hoặc empty nếu không bắt buộc
        }
        return pattern.matcher(phone).matches();
    }
}
