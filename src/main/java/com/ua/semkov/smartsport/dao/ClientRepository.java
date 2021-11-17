package com.ua.semkov.smartsport.dao;


import com.ua.semkov.smartsport.entity.Client;
import com.ua.semkov.smartsport.entity.Coach;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Semkov.S
 */
@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {

}
