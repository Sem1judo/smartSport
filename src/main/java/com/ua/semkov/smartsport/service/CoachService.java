package com.ua.semkov.smartsport.service;


import com.ua.semkov.smartsport.dao.CoachRepository;
import com.ua.semkov.smartsport.entity.Coach;
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
public class CoachService {
    private static final String MISSING_ID_ERROR_MESSAGE = "Missing id coach";
    private static final String NOT_EXIST_ENTITIES = "Doesn't exist list with coaches";
    private static final String NOT_EXIST_ENTITY = "Doesn't exist list with coach";

    private final ValidatorEntity<Coach> validator;
    private final CoachRepository coachRepository;

    public List<Coach> getAll() {
        log.debug("Trying to get all coaches");
        try {
            return (List<Coach>) coachRepository.findAll();
        } catch (EmptyResultDataAccessException e) {
            log.warn("Coaches list is not exist");
            throw new NoSuchEntitiesException(NOT_EXIST_ENTITIES);
        } catch (DataAccessException e) {
            log.error("Failed to get list of all coaches", e);
            throw new ServiceException("Failed to get list of all coaches", e);
        }
    }

    public Coach create(Coach coach) {
        log.debug("Trying to create coach: {}", coach);

        validator.validate(coach);
        try {
            coach = coachRepository.save(coach);
        } catch (DataAccessException e) {
            log.error("Failed to create coach: {}", coach, e);
            throw new ServiceException("Failed to create coach", e);
        }
        return coach;
    }


    public void deleteById(long id) {
        log.debug("Trying to delete coach with id={}", id);
        if (id == 0) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }
        try {
            coachRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing coach with id={}", id);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("Failed to delete coach with id={}", id, e);
            throw new ServiceException("Failed to delete coach by id", e);
        }
    }

    public void delete(Coach coach) {
        log.debug("Trying to delete coach = {}", coach);

        if (coach == null) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }
        try {
            coachRepository.delete(coach);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing coach = {}", coach);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("failed to delete coach = {}", coach, e);
            throw new ServiceException("Failed to delete coach", e);
        }
    }


    public Coach getById(long id) {
        log.debug("Trying to get user with id={}", id);

        if (id == 0) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }
        Coach coach;
        try {
            coach = coachRepository.findById(id)
                    .orElseThrow(() -> new NoSuchEntityException("Invalid coach ID"));
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing coach with id={}", id);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("Failed to retrieve coach with id={}", id, e);
            throw new ServiceException("Failed to retrieve coach with such id", e);
        }
        return coach;
    }


    public void update(Coach coach) {
        log.debug("Trying to update coach: {}", coach);

        if (coach.getId() == 0) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }

        validator.validate(coach);
        try {
            coachRepository.findById(coach.getId());
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing coach: {}", coach);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("Failed to retrieve coach: {}", coach);
            throw new ServiceException("Failed to retrieve coach: ", e);
        }
        try {
            coachRepository.save(coach);
        } catch (DataAccessException e) {
            log.error("Failed to update coach: {}", coach);
            throw new ServiceException("Problem with updating coach");
        }
    }

}


