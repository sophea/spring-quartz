package com.sma.quartz.controller;

import com.sma.common.tools.exceptions.ItemNotFoundBusinessException;
import com.sma.common.tools.response.ResponseList;
import com.sma.quartz.entity.Field;
import com.sma.quartz.entity.PublicHoliday;
import com.sma.quartz.repository.PublicHolidayRepository;
import com.sma.quartz.utils.PaginationUtil;
import com.sma.quartz.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    protected PublicHolidayRepository dao;

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
        dao.save(body);
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
        dao.save(domain);
        return new ResponseEntity<>(domain, HttpStatus.OK);
    }

    /**
     * find node by id
     *
     * @param request
     * @param id
     * @return
     */
    @RequestMapping(value = "v1/{id}", method = RequestMethod.GET)
    @ResponseBody
    public PublicHoliday findById(HttpServletRequest request, @PathVariable Long id) {
       // return ResponseUtil.wrapOrNotFound(dao.findById(id));
        Optional<PublicHoliday> record = dao.findById(id);
        if( ! record.isPresent()) {
            throw new ItemNotFoundBusinessException("object not found");
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
        final PublicHoliday domain = dao.findById(id).get();
        if (domain == null) {
             throw new ItemNotFoundBusinessException("not found");
        }
        domain.setName(body.getName());
        domain.setState(body.getState());
        domain.setNameKh(body.getNameKh());
        domain.setDateValue(body.getDateValue());
        dao.save(domain);
        return new ResponseEntity<>(domain, HttpStatus.OK);
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
        Pageable pageable = PageRequest.of(pageNumber, limit);

        final Page<PublicHoliday> page = dao.findAll(pageable);

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
        return new ResponseEntity<>(dao.findAll(), HttpStatus.OK);
    }

    /**
     * {@code GET /users} : get all users.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping("v2")
    public ResponseEntity<Page<PublicHoliday> > getAllWithPagination(Pageable pageable) {
        final Page<PublicHoliday> page = dao.findAll(pageable);
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
     *
     * @param request
     */
    @RequestMapping(value = "v1/schema", method = {RequestMethod.GET}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, Object>> getschma(HttpServletRequest request) {
        final Map<String, Object> body = new HashMap<String, Object>();


        final List<Field> columns = new ArrayList<>();
        columns.add(new Field("name", "text"));
        columns.add(new Field("nameKh", "text"));
        columns.add(new Field("dateValue", "text"));
        columns.add(new Field("createdDate", "datetime"));
        columns.add(new Field("updatedDate", "datetime"));
        // test
        // columns.add(new Field("gender", "select", Arrays.asList(new Data("1", "Female"), new Data("2", "Male"))));
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
//
//    @GetMapping(value = "/v1", produces = { MediaType.APPLICATION_JSON_VALUE })
//    @ResponseBody
//    public ResponseList<PublicHoliday> getPage(@RequestParam(value="pagesize", defaultValue="10") int pagesize,
//                                               @RequestParam(value = "cursorkey", required = false) String cursorkey) {
//        log.info("====get page {} , {} ====", pagesize, cursorkey);
//        int offset = Integer.parseInt(StringUtils.isEmpty(cursorkey) ? "0" : cursorkey);
//        Pageable pageable = PageRequest.of(offset, pagesize);
//
//        final Page<PublicHoliday> page = dao.findAll(pageable);
//
//        ResponseList<PublicHoliday> responseList = new ResponseList<>(page.getContent(), page.getTotalElements(),page.hasNext(), offset +1, pagesize,null);
//
//        return responseList;//dao.getPage(pagesize, cursorkey);
//    }
}
