package org.motechproject.mots.repository;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Assert;
import org.junit.Test;
import org.motechproject.mots.BaseIntegrationTest;
import org.motechproject.mots.domain.BaseTimestampedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class BaseCrudRepositoryIntegrationTest<T extends BaseTimestampedEntity>
    extends BaseIntegrationTest {

  private final AtomicInteger instanceNumber = new AtomicInteger(0);

  protected abstract JpaRepository<T, UUID> getRepository();

  /*
   * Generate a unique instance of given type.
   * @return generated instance
   */
  protected abstract T generateInstance();

  protected int getNextInstanceNumber() {
    return this.instanceNumber.incrementAndGet();
  }

  protected void assertInstance(T instance) {
    Assert.assertNotNull(instance.getId());
  }

  @Test
  public void testCreate() {
    JpaRepository<T, UUID> repository = this.getRepository();

    T instance = this.generateInstance();
    Assert.assertNull(instance.getId());

    instance = repository.save(instance);
    assertInstance(instance);

    Assert.assertTrue(repository.existsById(instance.getId()));
  }

  @Test
  public void testFindOne() {
    JpaRepository<T, UUID> repository = this.getRepository();

    T instance = this.generateInstance();

    instance = repository.save(instance);
    assertInstance(instance);

    UUID id = instance.getId();

    instance = repository.getOne(id);
    assertInstance(instance);
    Assert.assertEquals(id, instance.getId());
  }

  @Test
  public void testDelete() {
    JpaRepository<T, UUID> repository = this.getRepository();

    T instance = this.generateInstance();
    Assert.assertNotNull(instance);

    instance = repository.save(instance);
    assertInstance(instance);

    UUID id = instance.getId();

    repository.deleteById(id);
    Assert.assertFalse(repository.existsById(id));
  }
}
