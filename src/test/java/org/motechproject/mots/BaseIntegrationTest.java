package org.motechproject.mots;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@DirtiesContext
@RunWith(SpringRunner.class)
public abstract class BaseIntegrationTest {
}
