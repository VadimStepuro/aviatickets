package com.stepuro.aviatickets.utils.aspects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Autowired
    private ObjectMapper mapper;
    @Pointcut("@annotation(com.stepuro.aviatickets.api.annotations.Loggable)")
    public void pointcut() {
    }


    @Before("pointcut()")
    public void logMethod(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> parameters = getParameters(joinPoint);
        try {
            logger.info("==> user: {} class name: {}, method name: {}, arguments: {} ",
                    auth.getPrincipal(), className, methodName, mapper.writeValueAsString(parameters));
        } catch (JsonProcessingException e) {
            logger.error("Error while converting", e);
        }
    }

    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        logger.error("Exception in class: {}, in method: {}, with message = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), e.getMessage() != null ? e.getMessage() : "NULL");
    }

    @AfterReturning(pointcut = "pointcut()", returning = "entity")
    public void logMethodAfter(JoinPoint joinPoint, ResponseEntity<?> entity) {
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();


        try {
            logger.info("<== user: {} class name: {}, method name: {}, retuning: {}",
                    auth.getPrincipal(), className, methodName, mapper.writeValueAsString(entity));
        } catch (JsonProcessingException e) {
            logger.error("Error while converting", e);
        }
    }

    private Map<String, Object> getParameters(JoinPoint joinPoint) {
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();

        HashMap<String, Object> map = new HashMap<>();

        String[] parameterNames = signature.getParameterNames();

        for (int i = 0; i < parameterNames.length; i++) {
            map.put(parameterNames[i], joinPoint.getArgs()[i]);
        }

        return map;
    }
}
