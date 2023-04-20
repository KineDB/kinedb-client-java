package com.itenebris.kinedb.sdk.demo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Scheduled(fixedDelay = 10000)
    public void demo() {
        {
            jdbcTemplate.execute("SELECT * FROM mysql01.test2");
        }

        {
            User user = jdbcTemplate.queryForObject("SELECT * FROM mysql01.test2 limit 1",
                    (rs, rowNum) -> new User(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("age")));
            log.info("user :" + user);
        }

        {
            List<User> list = jdbcTemplate.query("SELECT * FROM mysql01.test2 where id = ? ",
                    (ps) -> {
                        ps.setInt(1, 1);
                    },
                    (rs, rowNum) -> new User(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("age")));
            log.info("user :" + list);
        }
    }
}
