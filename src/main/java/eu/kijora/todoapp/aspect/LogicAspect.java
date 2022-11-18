package eu.kijora.todoapp.aspect;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogicAspect {

    private final Timer projectCreateGroupTimer;

    LogicAspect(final MeterRegistry registry){
        projectCreateGroupTimer = registry.timer("logic.product.create.group");
    }

    @Around("execution(* eu.kijora.todoapp.logic.ProjectService.createGroup(..))")
        //* dowolny typ zwracany
    Object aroundProjectCreateGroup(ProceedingJoinPoint jp) {
        return projectCreateGroupTimer.record(() -> {
            try {
                return jp.proceed();
            } catch (Throwable e) {
                if (e instanceof RuntimeException)
                    throw (RuntimeException) e;
                throw new RuntimeException(e);
            }
        });
    }
}

