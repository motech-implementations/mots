package org.motechproject.mots.validate.constraintvalidators;

import static org.motechproject.mots.validate.ValidationUtils.isValidDateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.motechproject.mots.constants.MotsConstants;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.dto.BulkAssignmentDto;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.EndDateAfterStartDate;

public class BulkAssignmentDtoEndDateAfterStartDateValidator implements
    ConstraintValidator<EndDateAfterStartDate, BulkAssignmentDto> {

  private static final String END_DATE = "endDate";

  @Override
  public void initialize(EndDateAfterStartDate parameters) {
  }

  @Override
  public boolean isValid(BulkAssignmentDto bulkAssignmentDto,
      ConstraintValidatorContext context) {
    String start = bulkAssignmentDto.getStartDate();
    String end = bulkAssignmentDto.getEndDate();
    if (!StringUtils.isEmpty(start) && !StringUtils.isEmpty(end)
        && isValidDateFormat(MotsConstants.SIMPLE_DATE_FORMAT, start)
        && isValidDateFormat(MotsConstants.SIMPLE_DATE_FORMAT, end)) {

      SimpleDateFormat sdf = new SimpleDateFormat(MotsConstants.SIMPLE_DATE_FORMAT);
      Date startDate = null;
      Date endDate = null;
      try {
        startDate = sdf.parse(start);
        endDate = sdf.parse(end);
      } catch (ParseException e) {
        return false;
      }
      if (startDate.after(endDate)) {
        context.disableDefaultConstraintViolation();
        ValidationUtils.addDefaultViolationMessageToInnerField(context, END_DATE,
            ValidationMessages.END_DATE_AFTER_START_DATE);
        return false;
      }
    }
    return true;
  }
}
