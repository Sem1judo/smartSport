package com.ua.semkov.smartsport.service;


import com.ua.semkov.smartsport.dao.SubscriptionRepository;
import com.ua.semkov.smartsport.entity.Subscription;
import com.ua.semkov.smartsport.exceptions.NoSuchEntitiesException;
import com.ua.semkov.smartsport.exceptions.NoSuchEntityException;
import com.ua.semkov.smartsport.validation.ValidatorEntity;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@AllArgsConstructor
@ToString
public class SubscriptionService {

    private static final String MISSING_ID_ERROR_MESSAGE = "Missing id subscription";
    private static final String NOT_EXIST_ENTITY = "Doesn't exist such subscription";
    private static final String NOT_EXIST_ENTITIES = "Doesn't exist such subscriptions";

    private final ValidatorEntity<Subscription> validator;

    private final SubscriptionRepository subscriptionRepository;


    public List<Subscription> getAll() {
        log.debug("Trying to get all subscription");
        try {
            return (List<Subscription>) subscriptionRepository.findAll();
        } catch (EmptyResultDataAccessException e) {
            log.warn(NOT_EXIST_ENTITIES);
            throw new NoSuchEntitiesException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("Failed to get list of subscriptions", e);
            throw new ServiceException("Failed to get list of subscriptions", e);
        }
    }

    public Subscription create(Subscription subscription) {
        log.debug("Trying to create subscription: {}", subscription);

        validator.validate(subscription);

        try {
            subscription = subscriptionRepository.save(subscription);
        } catch (DataAccessException e) {
            log.error("Failed to create subscription: {}", subscription, e);
            throw new ServiceException("Failed to create subscription", e);
        }
        return subscription;
    }


    public void deleteById(long id) {
        log.debug("Trying to delete subscription with id={}", id);
        if (id == 0) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }
        try {
            subscriptionRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing subscription with id={}", id);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("failed to delete subscription with id={}", id, e);
            throw new ServiceException("Failed to delete subscription by id", e);
        }
    }

    public void delete(Subscription subscription) {
        log.debug("Trying to delete subscription = {}", subscription);

        if (subscription == null) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }
        try {
            subscriptionRepository.delete(subscription);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing subscription = {}", subscription);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("failed to delete subscription= {}", subscription, e);
            throw new ServiceException("Failed to delete subscription", e);
        }
    }


    public Subscription getById(long id) {
        log.debug("Trying to get subscription with id={}", id);

        if (id == 0) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }
        Subscription subscription;
        try {
            subscription = subscriptionRepository.findById(id)
                    .orElseThrow(() -> new NoSuchEntityException("Invalid subscription ID"));
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing subscription with id={}", id);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("Failed to retrieve subscription with id={}", id, e);
            throw new ServiceException("Failed to retrieve subscription with such id", e);
        }
        return subscription;
    }


    public void update(Subscription subscription) {
        log.debug("Trying to update subscription: {}", subscription);

        if (subscription.getId() == 0) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }

        validator.validate(subscription);
        try {
            subscriptionRepository.findById(subscription.getId());
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing subscription: {}", subscription);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("Failed to retrieve subscription: {}", subscription);
            throw new ServiceException("Failed to retrieve subscription: ", e);
        }
        try {
            subscriptionRepository.save(subscription);
        } catch (DataAccessException e) {
            log.error("Failed to update subscription: {}", subscription);
            throw new ServiceException("Problem with updating subscription");
        }
    }

}


