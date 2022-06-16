package com.abatalev.pgnch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestService {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public RestService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/add")
    public ActionResponse add(@RequestBody ActionRequest request) {
        ActionResponse response = new ActionResponse();
        try {
            System.out.println("post id:" + request.getId() + " delta:" + request.getDelta());
            jdbcTemplate.update("insert into ACTIONS (id,delta) values(?,?)", request.getId(), request.getDelta());
            response.setStatus("success");
        } catch (Exception e) {
            response.setStatus("error");
        }
        return response;
    }
}
