package org.motechproject.mots.domain;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.validate.CourseReleaseCheck;

@MappedSuperclass
@NoArgsConstructor
public abstract class IvrObject extends BaseTimestampedEntity {

  @NotBlank(message = ValidationMessages.EMPTY_IVR_ID, groups = CourseReleaseCheck.class)
  @Column(name = "ivr_id")
  @Getter
  @Setter
  private String ivrId;

  @Column(name = "ivr_name")
  @Getter
  @Setter
  private String ivrName;

  public IvrObject(UUID id) {
    super(id);
  }

  public IvrObject(String ivrId, String ivrName) {
    this.ivrId = ivrId;
    this.ivrName = ivrName;
  }
}
