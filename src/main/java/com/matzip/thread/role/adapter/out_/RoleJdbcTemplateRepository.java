package com.matzip.thread.role.adapter.out_;

import com.matzip.thread.common.exception.UpdateFailureException;
import com.matzip.thread.role.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class RoleJdbcTemplateRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    List<RoleJdbcDto> findAll() {
        String sql = """
                       WITH recursive r (ROLE_ID, ROLE_NAME, DESCRIPTION, PARENT_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATED_BY, LAST_MODIFIED_BY) AS (
                     SELECT ROLE_ID, ROLE_NAME, DESCRIPTION, PARENT_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATED_BY, LAST_MODIFIED_BY
                       FROM role_
                      WHERE PARENT_ID IS NULL
                      UNION ALL
                     SELECT c.ROLE_ID, c.ROLE_NAME, c.DESCRIPTION, c.PARENT_ID, c.CREATED_DATE, c.LAST_MODIFIED_DATE, c.CREATED_BY, c.LAST_MODIFIED_BY
                       FROM role_ c
                       JOIN r p
                         ON c.PARENT_ID = p.ROLE_ID)
                     SELECT ROLE_ID, ROLE_NAME, DESCRIPTION, PARENT_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATED_BY, LAST_MODIFIED_BY FROM r;
                     """;
        return jdbcTemplate.query(sql, roleRowMapper());
    }

    List<RoleJdbcDto> findByRoleWithChildren(Role role) {
        String sql = """
                       WITH recursive r (ROLE_ID, ROLE_NAME, DESCRIPTION, PARENT_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATED_BY, LAST_MODIFIED_BY) AS (
                     SELECT ROLE_ID, ROLE_NAME, DESCRIPTION, PARENT_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATED_BY, LAST_MODIFIED_BY
                       FROM role_
                      WHERE ROLE_NAME = :roleName
                      UNION ALL
                     SELECT c.ROLE_ID, c.ROLE_NAME, c.DESCRIPTION, c.PARENT_ID, c.CREATED_DATE, c.LAST_MODIFIED_DATE, c.CREATED_BY, c.LAST_MODIFIED_BY
                       FROM role_ c
                       JOIN r p
                         ON c.PARENT_ID = p.ROLE_ID)
                     SELECT ROLE_ID, ROLE_NAME, DESCRIPTION, PARENT_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATED_BY, LAST_MODIFIED_BY FROM r;
                     """;


        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("roleName", role.name());

        return jdbcTemplate.query(sql, param, roleRowMapper());
    }

    public void save(List<RoleJdbcDto> roleDtoList) {
        String sql = """
                     INSERT INTO role_ (ROLE_NAME, DESCRIPTION, PARENT_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATED_BY, LAST_MODIFIED_BY)
                     VALUES (:roleName, :description, (select r.ROLE_ID from role_ r where ROLE_NAME = :parentRoleName), now(), now(), :createdBy, :lastModifiedBy);
                     """;

        int size = roleDtoList.size();
        BeanPropertySqlParameterSource[] parameterSources = new BeanPropertySqlParameterSource[size];

        for (int i = 0; i < size; i++) {
            RoleJdbcDto param = roleDtoList.get(i);
            BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(param);
            parameterSources[i] = parameterSource;
        }

        jdbcTemplate.batchUpdate(sql, parameterSources);
    }

    public void update(Role role, List<RoleJdbcDto> roleDtoList) throws UpdateFailureException {
        List<RoleJdbcDto> findDtoList = findByRoleWithChildren(role);
        Map<String, RoleJdbcDto> dtoMap = new HashMap<>();
        Map<String, Boolean> checkMap = new HashMap<>();
        ArrayList<RoleJdbcDto> params = new ArrayList<>();
        ArrayList<RoleJdbcDto> deleteList = new ArrayList<>();
        ArrayList<RoleJdbcDto> saveList = new ArrayList<>();

        roleDtoList.forEach(dto -> {
            String roleName = dto.getRoleName();
            dtoMap.put(roleName, dto);
            checkMap.put(roleName, false);
        });

        for (RoleJdbcDto dto : findDtoList) {
            String roleName = dto.getRoleName();
            if (!dtoMap.containsKey(roleName)) {
                deleteList.add(dto);
                continue;
            }

            checkMap.put(roleName, true);
            RoleJdbcDto updateDto = dtoMap.get(roleName);

            if (dto.equals(updateDto)) continue;

            dto.setDescription(updateDto.getDescription());
            dto.setParentId(updateDto.getParentId());
            dto.setLastModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            params.add(dto);
        }

        checkMap.keySet().stream()
                .filter(k -> !checkMap.get(k))
                .forEach(k -> saveList.add(dtoMap.get(k)));


        String sql = """
                     UPDATE role_
                        SET DESCRIPTION = :description,
                            PARENT_ID = :parentId,
                            LAST_MODIFIED_DATE = now(),
                            LAST_MODIFIED_BY = :lastModifiedBy
                      WHERE ROLE_ID = :roleId
                        AND LAST_MODIFIED_DATE = :lastModifiedBy;
                     """;

        int size = params.size();
        BeanPropertySqlParameterSource[] parameterSources = new BeanPropertySqlParameterSource[size];

        for (int i = 0; i < size; i++) {
            RoleJdbcDto param = params.get(i);
            BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(param);
            parameterSources[i] = parameterSource;
        }

        int[] result = jdbcTemplate.batchUpdate(sql, parameterSources);
        int sum = Arrays.stream(result).sum();
        if (sum < size) throw new UpdateFailureException("Successful update of " + sum + " out of " + size);


    }

    public void delete(List<RoleJdbcDto> roleDtoList) {

    }

    RoleJdbcDto findByRole(Role role) {
        String sql = """
                     SELECT ROLE_ID, ROLE_NAME, DESCRIPTION, PARENT_ID
                       FROM role_
                      WHERE ROLE_NAME = :roleName; 
                     """;
        return jdbcTemplate.queryForObject(sql, Map.of("roleName", role.name()), RoleJdbcDto.class);
    }

    List<RoleJdbcDto> findChildrenById(Long roleId) {
        String sql = """
                     SELECT ROLE_ID, ROLE_NAME, DESCRIPTION, PARENT_ID
                       FROM role_
                      WHERE PARENT_ID = :roleId;
                     """;
        return jdbcTemplate.query(sql, Map.of("roleId", roleId), roleRowMapper());
    }

    private static BeanPropertyRowMapper<RoleJdbcDto> roleRowMapper() {
        return BeanPropertyRowMapper.newInstance(RoleJdbcDto.class);
    }
}
