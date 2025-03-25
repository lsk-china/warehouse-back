package com.lp.sale.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lp.sale.util.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class PostProcessAspect {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Pointcut("within(com.lp.sale.controller.*) " +
              "&& !within(com.lp.sale.controller.RedirectController)")
    public void pointcut() {}

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object result = joinPoint.proceed();
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", 200);
            resultMap.put("data", result);
            resultMap.put("message", "Success");
            return objectMapper.writeValueAsString(resultMap);
        } catch (Throwable throwable) {
            if (throwable instanceof StatusCode) {
                StatusCode statusCode = (StatusCode) throwable;
                Map<String, Object> map = new HashMap<>();
                map.put("code", statusCode.getCode());
                map.put("message", statusCode.getMessage());
                return objectMapper.writeValueAsString(map);
            } else {
                log.error("Caught unhandled error: ", throwable);
                Map<String, Object> map = new HashMap<>();
                map.put("code", 500);
                map.put("message", throwable.getMessage());
                return objectMapper.writeValueAsString(map);
            }
        }
    }
}


