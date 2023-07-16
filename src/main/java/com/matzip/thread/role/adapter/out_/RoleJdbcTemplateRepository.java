package com.matzip.thread.role.adapter.out_;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoleJdbcTemplateRepository {
    private final JdbcTemplate jdbcTemplate;

    List<RoleJdbcDto> findAll() {
        String sql =
                "  WITH r (ROLE_ID, ROLE_NAME, DESCRIPTION, PARENT_ID) AS ( \n" +
                "SELECT ROLE_ID, ROLE_NAME, DESCRIPTION, PARENT_ID \n" +
                "  FROM ROLE \n" +
                " WHERE PARENT_ID IS NULL \n" +
                " UNION ALL \n" +
                "SELECT c.ROLE_ID, c.ROLE_NAME, c.DESCRIPTION, c.PARENT_ID \n" +
                "  FROM ROLE c \n" +
                "  JOIN r p \n" +
                "    ON c.PARENT_ID = p.ROLE_ID) \n" +
                "SELECT * FROM r";

        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(RoleJdbcDto.class));
    }
}
