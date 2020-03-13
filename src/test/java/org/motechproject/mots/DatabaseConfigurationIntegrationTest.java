package org.motechproject.mots;

import static org.junit.Assert.assertEquals;

import javax.sql.DataSource;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DatabaseConfigurationIntegrationTest extends BaseIntegrationTest {

  @Autowired
  private DataSource dataSource;

  @Test
  public void shouldUseHikariDataSource() {
    assertEquals("com.zaxxer.hikari.HikariDataSource", dataSource.getClass().getName());
  }
}
