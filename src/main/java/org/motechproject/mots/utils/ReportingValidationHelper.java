package org.motechproject.mots.utils;

import static org.motechproject.mots.constants.ReportingMessageConstants.ERROR_REPORTING_FILE_EMPTY;
import static org.motechproject.mots.constants.ReportingMessageConstants.ERROR_REPORTING_FILE_INCORRECT_TYPE;
import static org.motechproject.mots.constants.ReportingMessageConstants.ERROR_REPORTING_FILE_MISSING;

import java.util.Arrays;
import java.util.Locale;
import org.motechproject.mots.exception.ReportingException;
import org.springframework.web.multipart.MultipartFile;

public final class ReportingValidationHelper {

  /**
   * Throws exception if file is empty.
   * @param file The file to check
   */
  public static void throwIfFileIsEmpty(MultipartFile file) {
    if (file.isEmpty()) {
      throw new ReportingException(ERROR_REPORTING_FILE_EMPTY);
    }
  }

  /**
   * Throws exception if file has incorrect type.
   * @param file The file to check
   * @param allowedFileTypes Allowed file types, e.g. png, jpg
   */
  @SuppressWarnings("PMD.UseVarargs")
  public static void throwIfIncorrectFileType(MultipartFile file, String[] allowedFileTypes) {
    if (Arrays.stream(allowedFileTypes).noneMatch(type ->
        file.getOriginalFilename().toLowerCase(Locale.ENGLISH).endsWith("." + type))) {
      throw new ReportingException(ERROR_REPORTING_FILE_INCORRECT_TYPE);
    }
  }

  /**
   * Throws exception if file is null.
   * @param file The file to check
   */
  public static void throwIfFileIsNull(MultipartFile file) {
    if (file == null) {
      throw new ReportingException(ERROR_REPORTING_FILE_MISSING);
    }
  }
}

