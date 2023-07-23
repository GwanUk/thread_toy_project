package com.matzip.thread.role.adapter.out_;

import com.matzip.thread.common.exception.UpdateFailureException;
import com.matzip.thread.role.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.util.*;

import static java.util.Objects.*;
import static java.util.stream.Collectors.toMap;

@Repository
@RequiredArgsConstructor
public class RoleJdbcTemplateRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    List<RoleJdbcDto> findAll() {
        String sql = """
                     WITH recursive r (ROLE_ID, ROLE_NAME, DESCRIPTION, PARENT_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATED_BY,
                                       LAST_MODIFIED_BY) AS (SELECT ROLE_ID,
                                                                    ROLE_NAME,
                                                                    DESCRIPTION,
                                                                    PARENT_ID,
                                                                    CREATED_DATE,
                                                                    LAST_MODIFIED_DATE,
                                                                    CREATED_BY,
                                                                    LAST_MODIFIED_BY
                                                             FROM ROLE_
                                                             WHERE PARENT_ID IS NULL
                                                             UNION ALL
                                                             SELECT c.ROLE_ID,
                                                                    c.ROLE_NAME,
                                                                    c.DESCRIPTION,
                                                                    c.PARENT_ID,
                                                                    c.CREATED_DATE,
                                                                    c.LAST_MODIFIED_DATE,
                                                                    c.CREATED_BY,
                                                                    c.LAST_MODIFIED_BY
                                                             FROM ROLE_ c
                                                                      JOIN r p
                                                                           ON c.PARENT_ID = p.ROLE_ID)
                     SELECT ROLE_ID,
                            ROLE_NAME,
                            DESCRIPTION,
                            PARENT_ID,
                            CREATED_DATE,
                            LAST_MODIFIED_DATE,
                            CREATED_BY,
                            LAST_MODIFIED_BY
                     FROM r
                     """;
        return jdbcTemplate.query(sql, roleRowMapper());
    }

    List<RoleJdbcDto> findByRoleWithChildren(Role role) {
        String sql = """
                     WITH recursive r (ROLE_ID, ROLE_NAME, DESCRIPTION, PARENT_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATED_BY,
                                       LAST_MODIFIED_BY) AS (SELECT ROLE_ID,
                                                                    ROLE_NAME,
                                                                    DESCRIPTION,
                                                                    PARENT_ID,
                                                                    CREATED_DATE,
                                                                    LAST_MODIFIED_DATE,
                                                                    CREATED_BY,
                                                                    LAST_MODIFIED_BY
                                                             FROM ROLE_
                                                             WHERE ROLE_NAME = :roleName
                                                             UNION ALL
                                                             SELECT c.ROLE_ID,
                                                                    c.ROLE_NAME,
                                                                    c.DESCRIPTION,
                                                                    c.PARENT_ID,
                                                                    c.CREATED_DATE,
                                                                    c.LAST_MODIFIED_DATE,
                                                                    c.CREATED_BY,
                                                                    c.LAST_MODIFIED_BY
                                                             FROM ROLE_ c
                                                                      JOIN r p
                                                                           ON c.PARENT_ID = p.ROLE_ID)
                     SELECT ROLE_ID,
                            ROLE_NAME,
                            DESCRIPTION,
                            PARENT_ID,
                            CREATED_DATE,
                            LAST_MODIFIED_DATE,
                            CREATED_BY,
                            LAST_MODIFIED_BY
                     FROM r
                     """;


        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("roleName", role.name());

        return jdbcTemplate.query(sql, param, roleRowMapper());
    }

    public void save(List<RoleJdbcDto> roleDtoList) {
        String sql = """
                     INSERT INTO ROLE_ (ROLE_NAME,
                                        DESCRIPTION,
                                        PARENT_ID,
                                        CREATED_DATE,
                                        LAST_MODIFIED_DATE,
                                        CREATED_BY,
                                        LAST_MODIFIED_BY)
                     VALUES (:roleName,
                             :description,
                             (SELECT r.ROLE_ID
                              FROM ROLE_ r
                              WHERE r.ROLE_NAME = :parentRoleName),
                             now(),
                             now(),
                             :createdBy,
                             :lastModifiedBy)
                     """;

        BeanPropertySqlParameterSource[] parameterSources = getParameterSources(roleDtoList);

        jdbcTemplate.batchUpdate(sql, parameterSources);
    }

    public void update(Role role, List<RoleJdbcDto> roleDtoList) throws UpdateFailureException {
        List<RoleJdbcDto> findDtoList = findByRoleWithChildren(role);
        fillParentRoleName(findDtoList);

        Map<String, RoleJdbcDto> updateDtoMap = roleDtoList.stream()
                .collect(toMap(RoleJdbcDto::getRoleName, r -> r));
        ArrayList<RoleJdbcDto> params = new ArrayList<>();
        HashSet<RoleJdbcDto> updatedSet = new HashSet<>();

        for (RoleJdbcDto dto : findDtoList) {
            String roleName = dto.getRoleName();
            RoleJdbcDto param = new RoleJdbcDto();

            param.setRoleName(roleName);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (nonNull(authentication)) {
                param.setLastModifiedBy(authentication.getName());
            }

            if (updateDtoMap.containsKey(roleName)) {
                RoleJdbcDto updateDto = updateDtoMap.get(roleName);
                updatedSet.add(updateDto);

                if (dto.equals(updateDto)) continue;

                param.setDescription(updateDto.getDescription());

                if (roleName.equals(role.name())) {
                    param.setParentRoleName(dto.getParentRoleName());
                } else {
                    param.setParentRoleName(updateDto.getParentRoleName());
                }
            } else {
                param.setDescription(dto.getDescription());
                param.setParentRoleName(null);
            }

            params.add(param);
        }

        roleDtoList.stream()
                .filter(dto -> !updatedSet.contains(dto))
                .forEach(params::add);

        String sql = """
                     UPDATE ROLE_
                     SET DESCRIPTION        = :description,
                         PARENT_ID          = CASE
                                                  WHEN :parentRoleName IS NULL THEN NULL
                                                  ELSE (SELECT r.ROLE_ID
                                                        FROM (SELECT ROLE_ID
                                                              FROM ROLE_
                                                              WHERE ROLE_NAME = :parentRoleName) r) END,
                         LAST_MODIFIED_DATE = now(),
                         LAST_MODIFIED_BY   = :lastModifiedBy
                     WHERE ROLE_NAME = :roleName
                     """;

        BeanPropertySqlParameterSource[] parameterSources = getParameterSources(params);

        int[] result = jdbcTemplate.batchUpdate(sql, parameterSources);
        int count = Arrays.stream(result).sum();
        int size = params.size();
        if (count < size) throw new UpdateFailureException();
        //TODO retry aop 만들기
    }

    public void delete(List<RoleJdbcDto> roleDtoList) {
        //TODO
    }

    private static BeanPropertySqlParameterSource[] getParameterSources(List<RoleJdbcDto> params) {
        int size = params.size();
        BeanPropertySqlParameterSource[] parameterSources = new BeanPropertySqlParameterSource[size];

        for (int i = 0; i < size; i++) {
            RoleJdbcDto param = params.get(i);
            BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(param);
            parameterSources[i] = parameterSource;
        }
        return parameterSources;
    }


    private void fillParentRoleName(List<RoleJdbcDto> roleDtoList) {
        Map<Long, String> map = roleDtoList.stream()
                .collect(toMap(RoleJdbcDto::getRoleId, RoleJdbcDto::getRoleName));

        roleDtoList.stream()
                .filter(r -> map.containsKey(r.getParentId()))
                .forEach(r -> r.setParentRoleName(map.get(r.getParentId())));
    }

    private static BeanPropertyRowMapper<RoleJdbcDto> roleRowMapper() {
        return BeanPropertyRowMapper.newInstance(RoleJdbcDto.class);
    }
}
