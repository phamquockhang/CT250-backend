package com.dvk.ct250backend.infrastructure.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;

public class EntityUtils {
    public static String getIdFieldName(EntityManager entityManager, Class<?> entityClass) {
        Metamodel metamodel = entityManager.getMetamodel();
        EntityType<?> entityType = metamodel.entity(entityClass);
        return entityType.getId(Object.class).getName();
    }
}
