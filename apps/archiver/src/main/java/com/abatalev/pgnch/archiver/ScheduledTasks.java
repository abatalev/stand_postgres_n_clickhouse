package com.abatalev.pgnch.archiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ScheduledTasks {

    private JdbcTemplate jdbcTemplate;
    private JdbcTemplate archiveTemplate;

    @Autowired
    public ScheduledTasks(@Qualifier("main") JdbcTemplate jdbcTemplate,
                          @Qualifier("archive") JdbcTemplate archiveTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.archiveTemplate = archiveTemplate;
    }

    @Scheduled(fixedDelayString = "1000")
//    @Transactional
    public void moveToArchive() {
        System.out.println("archive");
        List<Action> list = jdbcTemplate.query("select id,delta from Actions" +
                        " where is_archive = 0",
                BeanPropertyRowMapper.newInstance(Action.class));
        for (Action action : list) {
            System.out.println(">> update " + action.getId());
            jdbcTemplate.update("update Actions set is_archive = 1 where id = ?", action.getId());
            archiveTemplate.update("insert into Actions(id,delta) values(?,?)", action.getId(), action.getDelta());
        }
    }
}
