package com.dpp.subscriptionupgrade.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ModelMapperUtil {
    // Entity to DTO (Response Model) mapping
    public static <E, D> D mapEntityToDto(E entity, Class<D> dtoClass) {
        try {
            D dto = dtoClass.getDeclaredConstructor().newInstance();
            for (Field entityField : entity.getClass().getDeclaredFields()) {
                entityField.setAccessible(true);
                Object value = entityField.get(entity);

                try {
                    Field dtoField = dtoClass.getDeclaredField(entityField.getName());
                    dtoField.setAccessible(true);
                    dtoField.set(dto, value);
                } catch (NoSuchFieldException ignored) {
                    // If the field does not exist in DTO, ignore it
                }
            }
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to map entity to DTO", e);
        }
    }


    // DTO (Request Model) to Entity mapping
    public static <E, D> E mapDtoToEntity(D dto, Class<E> entityClass) {
        try {
            E entity = entityClass.getDeclaredConstructor().newInstance();
            for (Field dtoField : dto.getClass().getDeclaredFields()) {
                dtoField.setAccessible(true);
                Object value = dtoField.get(dto);

                try {
                    Field entityField = entityClass.getDeclaredField(dtoField.getName());
                    entityField.setAccessible(true);
                    entityField.set(entity, value);
                } catch (NoSuchFieldException ignored) {
                    // If the field does not exist in Entity, ignore it
                }
            }
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Failed to map DTO to entity", e);
        }
    }

    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        List<String> emptyNames = new ArrayList<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            if (src.getPropertyValue(pd.getName()) == null) {
                emptyNames.add(pd.getName());
            }
        }
        return emptyNames.toArray(new String[0]);
    }
}
