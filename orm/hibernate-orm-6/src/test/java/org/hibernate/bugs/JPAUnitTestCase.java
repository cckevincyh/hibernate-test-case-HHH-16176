package org.hibernate.bugs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

  private EntityManagerFactory entityManagerFactory;

  @Before
  public void init() {
    entityManagerFactory = Persistence.createEntityManagerFactory("templatePU");
  }

  @After
  public void destroy() {
    entityManagerFactory.close();
  }

  // Entities are auto-discovered, so just add them anywhere on class-path
  // Add your tests, using standard JUnit.
  @Test
  @DisplayName("OneToMany mappedBy inserts duplicate records when saved")
  public void hhh16176Test() throws Exception {
    // save user to db first
    UserContact u = saveUserFirst();

    // find user from db and update contactInfo list
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    UserContact userContact = entityManager.find(UserContact.class, u.getId());
    ContactInfo contactInfo = new ContactInfo();
    contactInfo.setPhoneNumber("123");
    contactInfo.setContactUser(userContact);
    userContact.getContactInfos().add(contactInfo);
    entityManager.merge(userContact);

    assertEquals(1, userContact.getContactInfos().size()); //expect size is 1 but actual is 2

    entityManager.getTransaction().commit();
    entityManager.close();

  }


  @Test
  @DisplayName("This example works fine.")
  public void hhh16176TestOK() throws Exception {
    // save user to db first
    UserContact u = saveUserFirst();

    // find user from db and update contactInfo list
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    UserContact userContact = entityManager.find(UserContact.class, u.getId());
    ContactInfo contactInfo = new ContactInfo();
    contactInfo.setPhoneNumber("123");
    contactInfo.setContactUser(userContact);

    userContact.getContactInfos().size(); //This example works fine.

    userContact.getContactInfos().add(contactInfo);
    entityManager.merge(userContact);

    assertEquals(1, userContact.getContactInfos().size());

    entityManager.getTransaction().commit();
    entityManager.close();

  }

  private UserContact saveUserFirst() {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    // Do stuff...
    UserContact userContact = new UserContact();
    userContact.setName("Kevin");
    entityManager.persist(userContact);
    entityManager.getTransaction().commit();
    entityManager.close();
    return userContact;
  }


}
