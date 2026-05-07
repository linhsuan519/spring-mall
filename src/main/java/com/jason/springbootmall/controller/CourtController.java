package com.jason.springbootmall.controller;

import com.jason.springbootmall.constant.CourtStatus;
import com.jason.springbootmall.constant.CourtType;
import com.jason.springbootmall.dto.CourtQueryParams;
import com.jason.springbootmall.dto.CourtRequest;
import com.jason.springbootmall.model.Court;
import com.jason.springbootmall.service.CourtService;
import com.jason.springbootmall.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Valid
@RestController
public class CourtController {

  @Autowired private CourtService courtService;

  @GetMapping("/courts")
  public ResponseEntity<Page<Court>> getCourts(
      @RequestParam(required = false) CourtType courtType,
      @RequestParam(required = false) CourtStatus status,
      @RequestParam(required = false) String search,
      @RequestParam(defaultValue = "created_date") String orderBy,
      @RequestParam(defaultValue = "desc") String sort,
      @RequestParam(defaultValue = "10") @Max(100) @Min(1) Integer limit,
      @RequestParam(defaultValue = "0") @Min(0) Integer offset) {

    CourtQueryParams params = new CourtQueryParams();
    params.setCourtType(courtType);
    params.setStatus(status);
    params.setSearch(search);
    params.setOrderBy(orderBy);
    params.setSort(sort);
    params.setLimit(limit);
    params.setOffset(offset);

    List<Court> courtList = courtService.getCourts(params);
    Integer total = courtService.countCourt(params);

    Page<Court> page = new Page<>();
    page.setLimit(limit);
    page.setOffset(offset);
    page.setTotal(total);
    page.setResults(courtList);

    return ResponseEntity.status(HttpStatus.OK).body(page);
  }

  @GetMapping("/courts/{courtId}")
  public ResponseEntity<Court> getCourt(@PathVariable Integer courtId) {
    Court court = courtService.getCourtById(courtId);
    return ResponseEntity.status(HttpStatus.OK).body(court);
  }

  // POST / PUT / DELETE 需要 ROLE_ADMIN，由 Security config 控管

  @PostMapping("/courts")
  public ResponseEntity<Court> createCourt(@RequestBody @Valid CourtRequest courtRequest) {
    Integer courtId = courtService.createCourt(courtRequest);
    Court court = courtService.getCourtById(courtId);
    return ResponseEntity.status(HttpStatus.CREATED).body(court);
  }

  @PutMapping("/courts/{courtId}")
  public ResponseEntity<Court> updateCourt(
      @PathVariable Integer courtId, @RequestBody @Valid CourtRequest courtRequest) {
    courtService.updateCourt(courtId, courtRequest);
    Court court = courtService.getCourtById(courtId);
    return ResponseEntity.status(HttpStatus.OK).body(court);
  }

  @DeleteMapping("/courts/{courtId}")
  public ResponseEntity<?> deleteCourt(@PathVariable Integer courtId) {
    courtService.deleteCourtById(courtId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
