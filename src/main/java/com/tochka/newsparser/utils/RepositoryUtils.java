package com.tochka.newsparser.utils;

import com.tochka.newsparser.controller.rest.RestExceptionHandler;
import org.springframework.data.repository.CrudRepository;

import javax.validation.ConstraintViolationException;

public class RepositoryUtils {

    public static <S> S save(S entity, CrudRepository crudRepository) throws ConstraintViolationException {
        try {
            return (S) crudRepository.save(entity);
        } catch (Exception ex) {
            if (ex.getCause() != null && (ex.getCause() instanceof javax.validation.ConstraintViolationException
                    || ex.getCause().getCause() instanceof javax.validation.ConstraintViolationException)) {
                StringBuilder message = new StringBuilder();
                ((javax.validation.ConstraintViolationException) (ex.getCause().getCause()))
                        .getConstraintViolations().forEach(constraintViolation -> {
                    message.append(constraintViolation.getPropertyPath());
                    message.append(": ");
                    message.append(constraintViolation.getMessage());
                    message.append("; ");
                });
                throw new RestExceptionHandler.ErrorWithMessage(message.toString());
            }

            if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                throw new RestExceptionHandler.ErrorWithMessage("Don't saved. Check your data");
            }

            throw ex;
        }
    }

}
