package org.motechproject.mots.domain;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.validate.CourseReleaseCheck;

/**
 * This class is a base object that all IVR object must extend. It contains identification
 *        info form IVR.
 */
@MappedSuperclass
@NoArgsConstructor
public abstract class IvrObject extends BaseTimestampedEntity {

  @NotBlank(message = ValidationMessageConstants.EMPTY_IVR_ID, groups = CourseReleaseCheck.class)
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
