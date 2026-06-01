package com.omar.remittance_management.common.exception;



public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String entityName, Object id) {
        super(entityName + " with id '" + id + "' was not found");
    }

    public EntityNotFoundException(String entityName, String fieldName, Object value) {
        super(entityName + " with " + fieldName + " '" + value + "' was not found");
    }
}