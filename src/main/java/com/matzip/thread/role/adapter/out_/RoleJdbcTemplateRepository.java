package com.matzip.thread.role.adapter.out_;

import com.matzip.thread.common.exception.InfiniteLoopException;
import com.matzip.thread.common.exception.NotFoundDataException;
import com.matzip.thread.common.exception.UpdateFailureException;
import com.matzip.thread.role.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Repository
@RequiredArgsConstructor
class RoleJdbcTemplateRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    List<RoleJdbcDto> findAll() {
        String sql = """
                     WITH RECURSIVE r (ROLE_ID, ROLE_NAME, DESCRIPTION, PARENT_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATED_BY,
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
        return jdbcTemplate.query(sql, rowMapper());
    }

    List<RoleJdbcDto> findByRoleWithChildren(Role role) {
        String sql = """
                     WITH RECURSIVE r (ROLE_ID, ROLE_NAME, DESCRIPTION, PARENT_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATED_BY,
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

        return jdbcTemplate.query(sql, param, rowMapper());
    }

    void save(List<RoleJdbcDto> roleDtoList) {
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

        roleDtoList.forEach(RoleJdbcDto::fillUserName);
        BeanPropertySqlParameterSource[] parameterSources = parametersMapper(roleDtoList);

        jdbcTemplate.batchUpdate(sql, parameterSources);
    }

    void update(Role role, List<RoleJdbcDto> roleDtoList) throws UpdateFailureException {
        ArrayList<RoleJdbcDto> params = new ArrayList<>();

        List<RoleJdbcDto> findRoleDto = findByRoleWithChildren(role);

        if (findRoleDto.isEmpty()) {
            throw new NotFoundDataException(role.name());
        }

        Long ancestorId = findRoleDto.get(0).getParentId();

        Set<String> roleSet = findRoleDto.stream()
                .map(RoleJdbcDto::getRoleName)
                .collect(Collectors.toSet());

        List<String> toFind = roleDtoList.stream()
                .map(RoleJdbcDto::getRoleName)
                .filter(n -> !roleSet.contains(n))
                .toList();

        if (toFind.size() != 0) {
            List<RoleJdbcDto> inRoles = findInRoles(toFind);

            int notFoundCnt = toFind.size() - inRoles.size();
            if (notFoundCnt > 0) {
                throw new NotFoundDataException(notFoundCnt + " data");
            }

            findRoleDto.addAll(inRoles);
        }

        Map<String, Long> nameIdMap = findRoleDto.stream()
                .collect(Collectors.toMap(RoleJdbcDto::getRoleName, RoleJdbcDto::getRoleId));

        Map<String, RoleJdbcDto> dtoMap = new HashMap<>();
        for (RoleJdbcDto dto : roleDtoList) {
            String parentRoleName = dto.getParentRoleName();
            if (nonNull(parentRoleName)) {
                Long parentId = nameIdMap.get(parentRoleName);
                dto.setParentId(parentId);
            }
            dtoMap.put(dto.getRoleName(), dto);
        }

        // 업데이트 데이터 생성
        for (RoleJdbcDto findDto : findRoleDto) {
            String roleName = findDto.getRoleName();
            if (dtoMap.containsKey(roleName)) {
                RoleJdbcDto dto = dtoMap.get(roleName);

                if (findDto.sameAs(dto)) continue;

                if (!roleName.equals(role.name())) {
                    findDto.setParentId(dto.getParentId());
                }

                findDto.setDescription(dto.getDescription());
            } else {
                findDto.setParentId(null);
            }

            findDto.fillUserName();
            params.add(findDto);
        }

        // 순환 참조 방지
        if (params.stream()
                .map(RoleJdbcDto::getRoleId)
                .anyMatch(id -> id.equals(ancestorId))) {
            throw new InfiniteLoopException();
        }

        String sql = """
                     UPDATE ROLE_
                     SET DESCRIPTION        = :description,
                         PARENT_ID          = :parentId,
                         LAST_MODIFIED_DATE = now(),
                         LAST_MODIFIED_BY   = :lastModifiedBy
                     WHERE ROLE_ID = :roleId
                     AND LAST_MODIFIED_DATE = :lastModifiedDate
                     """;

        BeanPropertySqlParameterSource[] parameterSources = parametersMapper(params);

        int[] result = jdbcTemplate.batchUpdate(sql, parameterSources);
        int count = Arrays.stream(result).sum();
        int size = params.size();
        if (count < size) throw new UpdateFailureException();
    }

    void delete(Role role) {
        String sql = """
                     DELETE FROM ROLE_
                     WHERE ROLE_NAME = :roleName
                     """;

        Map<String, String> paramMap = Map.of("roleName", role.name());
        jdbcTemplate.update(sql, paramMap);
    }

    private List<RoleJdbcDto> findInRoles(Collection<String> roleNames) {
        String sql = """
                     SELECT ROLE_ID,
                            ROLE_NAME,
                            DESCRIPTION,
                            PARENT_ID,
                            CREATED_DATE,
                            LAST_MODIFIED_DATE,
                            CREATED_BY,
                            LAST_MODIFIED_BY
                     FROM ROLE_
                     WHERE ROLE_NAME IN (:roleNames)
                     """;

        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("roleNames", roleNames);

        return jdbcTemplate.query(sql, param, rowMapper());
    }

    private BeanPropertySqlParameterSource[] parametersMapper(List<RoleJdbcDto> params) {
        int size = params.size();
        BeanPropertySqlParameterSource[] parameterSources = new BeanPropertySqlParameterSource[size];

        for (int i = 0; i < size; i++) {
            RoleJdbcDto param = params.get(i);
            BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(param);
            parameterSources[i] = parameterSource;
        }
        return parameterSources;
    }

    private BeanPropertyRowMapper<RoleJdbcDto> rowMapper() {
        return BeanPropertyRowMapper.newInstance(RoleJdbcDto.class);
    }
}
