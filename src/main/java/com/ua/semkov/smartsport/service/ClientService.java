package com.ua.semkov.smartsport.service;


import com.ua.semkov.smartsport.dao.ClientRepository;
import com.ua.semkov.smartsport.entity.Client;
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
public class ClientService {
    private static final String MISSING_ID_ERROR_MESSAGE = "Missing id client";
    private static final String NOT_EXIST_ENTITIES = "Doesn't exist list with clients";
    private static final String NOT_EXIST_ENTITY = "Doesn't exist list with client";

    private final ValidatorEntity<Client> validator;
    private final ClientRepository clientRepository;

    public List<Client> getAll() {
        log.debug("Trying to get all client");
        try {
            return (List<Client>) clientRepository.findAll();
        } catch (EmptyResultDataAccessException e) {
            log.warn("Clients list is not exist");
            throw new NoSuchEntitiesException(NOT_EXIST_ENTITIES);
        } catch (DataAccessException e) {
            log.error("Failed to get list of all clients", e);
            throw new ServiceException("Failed to get list of all clients", e);
        }
    }

    public Client create(Client client) {
        log.debug("Trying to create client: {}", client);

        validator.validate(client);
        try {
            client = clientRepository.save(client);
        } catch (DataAccessException e) {
            log.error("Failed to create client: {}", client, e);
            throw new ServiceException("Failed to create client", e);
        }
        return client;
    }


    public void deleteById(long id) {
        log.debug("Trying to delete client with id={}", id);
        if (id == 0) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }
        try {
            clientRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing client with id={}", id);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("Failed to delete client with id={}", id, e);
            throw new ServiceException("Failed to delete client by id", e);
        }
    }

    public void delete(Client client) {
        log.debug("Trying to delete client = {}", client);

        if (client == null) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }
        try {
            clientRepository.delete(client);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing client = {}", client);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("failed to delete client = {}", client, e);
            throw new ServiceException("Failed to delete client", e);
        }
    }


    public Client getById(long id) {
        log.debug("Trying to get client with id={}", id);

        if (id == 0) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }
        Client client;
        try {
            client = clientRepository.findById(id)
                    .orElseThrow(() -> new NoSuchEntityException("Invalid client ID"));
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing client with id={}", id);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("Failed to retrieve client with id={}", id, e);
            throw new ServiceException("Failed to retrieve client with such id", e);
        }
        return client;
    }


    public void update(Client client) {
        log.debug("Trying to update client: {}", client);

        if (client.getId() == 0) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }

        validator.validate(client);
        try {
            clientRepository.findById(client.getId());
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing client: {}", client);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("Failed to retrieve client: {}", client);
            throw new ServiceException("Failed to retrieve client: ", e);
        }
        try {
            clientRepository.save(client);
        } catch (DataAccessException e) {
            log.error("Failed to update client: {}", client);
            throw new ServiceException("Problem with updating client");
        }
    }

}


