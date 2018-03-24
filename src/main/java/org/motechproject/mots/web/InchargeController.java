package org.motechproject.mots.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.validation.Valid;
import org.motechproject.mots.domain.Incharge;
import org.motechproject.mots.dto.InchargeDto;
import org.motechproject.mots.exception.ReportingException;
import org.motechproject.mots.mapper.InchargeMapper;
import org.motechproject.mots.service.InchargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class InchargeController extends BaseController {

  public static final String FACILITY_NAME_PARAM = "facilityName";

  @Autowired
  private InchargeService inchargeService;

  private InchargeMapper inchargeMapper = InchargeMapper.INSTANCE;

  /**
   * Get list of incharges.
   * @return list of all incharges
   */
  @RequestMapping(value = "/incharge", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<InchargeDto> getIncharges() {
    Iterable<Incharge> incharges = inchargeService.getIncharges();

    return inchargeMapper.toDtos(incharges);
  }

  /**
   * Finds Incharges matching all of the provided parameters.
   * If there are no parameters, return all Incharges.
   */
  @RequestMapping(value = "/incharge/search", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Page<InchargeDto> searchIncharges(
      @RequestParam(value = "firstName", required = false) String firstName,
      @RequestParam(value = "secondName", required = false) String secondName,
      @RequestParam(value = "otherName", required = false) String otherName,
      @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
      @RequestParam(value = "email", required = false) String email,
      @RequestParam(value = FACILITY_NAME_PARAM, required = false) String facilityName,
      @RequestParam(value = "selected", required = false) Boolean selected,
      Pageable pageable) throws IllegalArgumentException {

    Page<Incharge> incharges = inchargeService.searchIncharges(firstName, secondName, otherName,
        phoneNumber, email, facilityName, selected, pageable);
    List<InchargeDto> inchargesDto = inchargeMapper.toDtos(incharges.getContent());

    return new PageImpl<>(inchargesDto, pageable, incharges.getTotalElements());
  }

  /**
   * Get Incharge with given id.
   * @param id id of Incharge to find
   * @return Incharge with given id
   */
  @RequestMapping(value = "/incharge/{id}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public InchargeDto getIncharge(@PathVariable("id") UUID id) {
    Incharge incharge = inchargeService.getIncharge(id);

    return inchargeMapper.toDto(incharge);
  }

  /**
   * Find by Facility Id.
   * @param facilityId id of Facility to find Incharge by
   * @return Incharge with given facility
   */
  @RequestMapping(value = "/incharge/findByFacilityId/{facilityId}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public InchargeDto findByFacilityId(@PathVariable("facilityId") UUID facilityId) {
    Incharge incharge = inchargeService.findByFacilityId(facilityId);

    return inchargeMapper.toDto(incharge);
  }

  /**
   * Select Incharge.
   * @param id id of Incharge to select
   * @param inchargeDto DTO of Incharge to be selected
   * @return selected Incharge
   */
  @RequestMapping(value = "/incharge/{id}/select", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public InchargeDto selectIncharge(@PathVariable("id") UUID id,
      @RequestBody @Valid InchargeDto inchargeDto, BindingResult bindingResult) {
    checkBindingResult(bindingResult);
    Incharge existingIncharge = inchargeService.getIncharge(id);

    if (existingIncharge.getSelected()) {
      throw new IllegalArgumentException("This incharge is already selected");
    }

    inchargeMapper.updateFromDto(inchargeDto, existingIncharge);
    existingIncharge.setSelected(true);
    return inchargeMapper.toDto(inchargeService.saveIncharge(existingIncharge));
  }

  /**
   * Update incharge.
   * @param id id of Incharge to update
   * @param inchargeDto DTO of Incharge to update
   * @return updated Incharge
   */
  @RequestMapping(value = "/incharge/{id}", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public InchargeDto saveIncharge(@PathVariable("id") UUID id,
      @RequestBody @Valid InchargeDto inchargeDto, BindingResult bindingResult) {
    checkBindingResult(bindingResult);
    Incharge existingIncharge = inchargeService.getIncharge(id);
    inchargeMapper.updateFromDto(inchargeDto, existingIncharge);
    return inchargeMapper.toDto(inchargeService.saveIncharge(existingIncharge));
  }

  /**
   * Upload list of Inchages in ".csv" format to mots, parse it and save records in DB.
   * @param file File in ".csv" format to upload
   */
  @RequestMapping(value = "/incharge/upload", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Map<Integer, String> uploadInchagesSpreadsheet(
      @RequestPart("file") MultipartFile file) throws ReportingException, IOException {
    return inchargeService.processInchageCsv(file);
  }
}
