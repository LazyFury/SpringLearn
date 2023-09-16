package io.lazyfury.utils;

import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AOPUtils {

    public <T> Optional<T> getParamByType(JoinPoint joinPoint, Class<T> paramClass) {
        for (Object arg : joinPoint.getArgs()) {
            if (paramClass.isInstance(arg)) {
                return Optional.of(paramClass.cast(arg));
            }
        }
        return Optional.empty();
    }
}
