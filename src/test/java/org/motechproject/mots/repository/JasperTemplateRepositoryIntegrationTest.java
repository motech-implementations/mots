package org.motechproject.mots.repository;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import org.junit.Test;
import org.motechproject.mots.domain.JasperTemplate;
import org.motechproject.mots.domain.JasperTemplateParameter;
import org.springframework.beans.factory.annotation.Autowired;

public class JasperTemplateRepositoryIntegrationTest extends
    BaseCrudRepositoryIntegrationTest<JasperTemplate> {

  private static final String NAME = "TemplateRepositoryIntegrationTest";

  @Autowired
  private JasperTemplateRepository jasperTemplateRepository;

  @Override
  JasperTemplateRepository getRepository() {
    return this.jasperTemplateRepository;
  }

  @Override
  protected JasperTemplate generateInstance() {
    JasperTemplate jasperTemplate = new JasperTemplate();
    jasperTemplate.setName(NAME + getNextInstanceNumber());
    return jasperTemplate;
  }

  @Test
  public void shouldFindTemplateByName() {
    // given
    JasperTemplate entity = generateInstance();
    jasperTemplateRepository.save(entity);

    // when
    JasperTemplate found = jasperTemplateRepository.findByName(entity.getName());

    // then
    assertThat(found.getName(), is(entity.getName()));
  }

  @Test
  public void shouldBindParametersToTemplateOnSave() {
    // given
    JasperTemplateParameter templateParameter = new JasperTemplateParameter();
    templateParameter.setName("parameter");
    templateParameter.setRequired(true);

    JasperTemplate template = generateInstance();
    template.setTemplateParameters(Collections.singletonList(templateParameter));

    // when
    JasperTemplate result = jasperTemplateRepository.save(template);

    // then
    assertEquals(templateParameter.getTemplate().getId(), result.getId());
  }

  @Test
  public void shouldFindByVisibilityFlag() {
    JasperTemplate visibleTemplate = generateInstance();
    JasperTemplate hiddenTemplate = generateInstance();
    hiddenTemplate.setVisible(false);

    jasperTemplateRepository.save(visibleTemplate);
    jasperTemplateRepository.save(hiddenTemplate);

    assertThat(
        jasperTemplateRepository.findByVisible(true),
        hasItem(hasProperty("id", is(visibleTemplate.getId()))));

    assertThat(
        jasperTemplateRepository.findByVisible(false),
        hasItem(hasProperty("id", is(hiddenTemplate.getId()))));
  }
}
