package com.matzip.thread.role.adapter.out_;

import com.matzip.thread.role.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoleJdbcTemplateRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    List<RoleJdbcDto> findByRoleWithChildren(Role role) {
        String sql =
                "  WITH r (ROLE_ID, ROLE_NAME, DESCRIPTION, PARENT_ID) AS ( \n" +
                        "SELECT ROLE_ID, ROLE_NAME, DESCRIPTION, PARENT_ID \n" +
                        "  FROM ROLE_ \n" +
                        " WHERE ROLE_NAME = :roleName \n" +
                        " UNION ALL \n" +
                        "SELECT c.ROLE_ID, c.ROLE_NAME, c.DESCRIPTION, c.PARENT_ID \n" +
                        "  FROM ROLE_ c \n" +
                        "  JOIN r p \n" +
                        "    ON c.PARENT_ID = p.ROLE_ID) \n" +
                        "SELECT * FROM r";


        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("roleName", role.name());

        return jdbcTemplate.query(sql, param, roleRowMapper());
    }

    List<RoleJdbcDto> findAll() {
        String sql =
                "  WITH r (ROLE_ID, ROLE_NAME, DESCRIPTION, PARENT_ID) AS ( \n" +
                "SELECT ROLE_ID, ROLE_NAME, DESCRIPTION, PARENT_ID \n" +
                "  FROM ROLE_ \n" +
                " WHERE PARENT_ID IS NULL \n" +
                " UNION ALL \n" +
                "SELECT c.ROLE_ID, c.ROLE_NAME, c.DESCRIPTION, c.PARENT_ID \n" +
                "  FROM ROLE_ c \n" +
                "  JOIN r p \n" +
                "    ON c.PARENT_ID = p.ROLE_ID) \n" +
                "SELECT * FROM r";

        return jdbcTemplate.query(sql, roleRowMapper());
    }

    private static BeanPropertyRowMapper<RoleJdbcDto> roleRowMapper() {
        return BeanPropertyRowMapper.newInstance(RoleJdbcDto.class);
    }
}
