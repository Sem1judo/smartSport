package com.ua.semkov.smartsport.dao;


import com.ua.semkov.smartsport.entity.Subscription;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Semkov.S
 */
@Repository
public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {


}
