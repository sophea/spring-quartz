package com.sma.quartz.controller;

import com.sma.quartz.entity.Field;
import com.sma.quartz.entity.PublicHoliday;
import com.sma.quartz.repository.PublicHolidayRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<PublicHoliday> findById(HttpServletRequest request, @PathVariable Long id) {
        return new ResponseEntity<>(dao.getOne(id), HttpStatus.OK);
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
        PublicHoliday domain = dao.getOne(id);
        if (domain == null) {
            // throw
        }
        domain.setName(body.getName());
        domain.setState(body.getState());
        domain.setNameKh(body.getNameKh());
        domain.setDateValue(body.getDateValue());
        dao.save(domain);
        return new ResponseEntity<>(domain, HttpStatus.OK);
    }
//
//    /**
//     * Get nodes with pagination
//     *
//     * @param request
//     * @param limit
//     * @param offset
//     * @return
//     */
//    @RequestMapping(value = "v1", method = RequestMethod.GET)
//    public ResponseEntity<ResponseList<PublicHoliday>> getPage(HttpServletRequest request,
//                                                          @RequestParam(value = "limit", defaultValue = "10") int limit,
//                                                          @RequestParam(value = "offset", defaultValue = "0") String offset) {
//        log.info("============= getPage ==========");
//        return new ResponseEntity<ResponseList<PublicHoliday>>(dao.getPage(limit, offset), HttpStatus.OK);
//    }

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

//    @GetMapping(value = "/v1", produces = { MediaType.APPLICATION_JSON_VALUE })
//    @ResponseBody
//    public ResponseList<PublicHoliday> getPage(@RequestParam(value="pagesize", defaultValue="10") int pagesize,
//            @RequestParam(value = "cursorkey", required = false) String cursorkey) {
//        log.info("====get page {} , {} ====", pagesize, cursorkey);
//        return dao..getPage(pagesize, cursorkey);
//    }
}
