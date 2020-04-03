package com.sma.quartz.controller;

import com.sma.common.tools.exceptions.ItemNotFoundBusinessException;
import com.sma.common.tools.response.ResponseList;
import com.sma.quartz.entity.Data;
import com.sma.quartz.entity.Field;
import com.sma.quartz.entity.PublicHoliday;
import com.sma.quartz.service.PublicHolidayService;
import com.sma.quartz.utils.PaginationUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/public-holiday")
@Slf4j
public class PublicHolidayController {

    @Autowired
    protected PublicHolidayService service;

    /**
     * create node with json
     *
     * @param request
     * @param body
     * @return
     */
    @RequestMapping(value = "v1/json", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<PublicHoliday> create(HttpServletRequest request, @RequestBody PublicHoliday body) {
        log.info("============= create with json ==========");
        service.create(body);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    /**
     * create node with form
     *
     * @param request
     * @param domain
     * @return
     */
    @RequestMapping(value = "v1", method = RequestMethod.POST)
    public ResponseEntity<PublicHoliday> createWithForm(HttpServletRequest request, @ModelAttribute PublicHoliday domain) {
        log.info("============= create ==========");
        domain = service.create(domain);
        return new ResponseEntity<>(domain, HttpStatus.OK);
    }

    /**
     * find node by id
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "v1/{id}", method = RequestMethod.GET)
    @ResponseBody
    public PublicHoliday findById(@PathVariable Long id) {
       // return ResponseUtil.wrapOrNotFound(dao.findById(id));
        Optional<PublicHoliday> record = service.findById(id);
        if( ! record.isPresent()) {
            throw new ItemNotFoundBusinessException("not found");
        }
        return record.get();
    }

    /**
     * update node with id
     *
     * @param id
     * @param body
     * @return
     */
    @RequestMapping(value = "v1/{id}/json", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<PublicHoliday> update(@PathVariable Long id, @RequestBody PublicHoliday body) {

        return new ResponseEntity<>(service.update(id, body), HttpStatus.OK);
    }

    /**
     * Get nodes with pagination
     *
     * @param request
     * @param limit
     * @param offset
     * @return
     */
    @GetMapping(value = "/v1", produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public ResponseList<PublicHoliday> getPage(HttpServletRequest request,
                                                          @RequestParam(value = "limit", defaultValue = "10") int limit,
                                                          @RequestParam(value = "offset", defaultValue = "0") String offset) {
        log.info("====get limit {} , offset {} ====", limit, offset);
        int pageNumber = Integer.parseInt(StringUtils.isEmpty(offset) ? "0" : offset);
        final PageRequest pageable = PageRequest.of(pageNumber, limit);

        final Page<PublicHoliday> page = service.getPage(pageable);

        return new ResponseList<>(page.getContent(), page.getTotalElements(),page.hasNext(), pageNumber + 1, limit,null);
    }

    /**
     * Get all notes
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "v1/all", method = RequestMethod.GET)
    public ResponseEntity<Collection<PublicHoliday>> getAll(HttpServletRequest request) {
        log.info("============= getAll ==========");
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    /**
     * delete item by id
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "v1/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable(value = "id") Long id) {
        log.info("delete id {}", id);
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    /**
     * {@code GET /users} : get all users.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping("v2")
    public ResponseEntity<Page<PublicHoliday> > getAllWithPagination(PageRequest pageable) {
        final Page<PublicHoliday> page = service.getPage(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);



        return new ResponseEntity<>(page, headers, HttpStatus.OK);
    }

    /**
     * schema api : Content-Type: application/x-www-form-urlencoded
     * example json value
     * <p>
     * {
     * primaryKeyName: "id",
     * tableName: "Country",
     * primaryKeyType: "long",
     * columns: {
     * comingSoon: "boolean",
     * flagImageUrl: "text",
     * isoCode: "text",
     * name: "text",
     * state: "long",
     * tcsUrl: "text",
     * price : "number",
     * createdDate: "datetime"
     * }
     * }
     */
    @RequestMapping(value = "v1/schema", method = {RequestMethod.GET}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, Object>> getSchema() {
        final Map<String, Object> body = new HashMap<String, Object>();


        final List<Field> columns = new ArrayList<>();
        columns.add(new Field("name", "text"));
        columns.add(new Field("nameKh", "text"));
        columns.add(new Field("dateValue", "text"));
        columns.add(new Field("createdDate", "datetime"));
        columns.add(new Field("updatedDate", "datetime"));

        columns.add(new Field("numberField", "number"));
        columns.add(new Field("longValue", "long"));
        columns.add(new Field("comingSoon", "boolean"));
     //   columns.add(new Field("xxxx", "text", "defaultValue"));
        // test
         columns.add(new Field("gender", "select", Arrays.asList(new Data("1", "Female"), new Data("2", "Male"))));
        /*
        final Map<String, String> columns = new HashMap<>();
        columns.put("name", "text");
        columns.put("type", "text");
        */
        body.put("columns", columns);
        body.put("tableName", "public_holiday");
        body.put("primaryKeyName", "id");
        body.put("primaryKeyType", "long");

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

}
