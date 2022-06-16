package com.abatalev.pgnch.reporter;

import com.abatalev.pgnch.reporter.model.Action;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActionRepository {

    private JdbcTemplate mainTemplate;
    private JdbcTemplate archiveTemplate;

    @Autowired
    public ActionRepository(@Qualifier("main") JdbcTemplate mainTemplate, @Qualifier("archive") JdbcTemplate archiveTemplate) {
        this.mainTemplate = mainTemplate;
        this.archiveTemplate = archiveTemplate;
    }

    public JRDataSource getDataSource() {
        List<Action> dataList = new ArrayList<>();
        for (Action action : mainTemplate.query("select id,delta from Actions where is_archive = 0", BeanPropertyRowMapper.newInstance(Action.class))) {
            dataList.add(action);
        }
        for (Action action : archiveTemplate.query("select id,delta from default.Actions", BeanPropertyRowMapper.newInstance(Action.class))) {
            dataList.add(action);
        }
        return new JRBeanCollectionDataSource(dataList);
    }
}
