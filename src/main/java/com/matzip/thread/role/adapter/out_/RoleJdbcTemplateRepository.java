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
        String rootName = role.name();
        List<RoleJdbcDto> updateParams = new ArrayList<>();
        List<RoleJdbcDto> removeChildren = new ArrayList<>();

        Map<String, RoleJdbcDto> originDtoMap = findByRoleWithChildren(role).stream()
                .collect(Collectors.toMap(RoleJdbcDto::getRoleName, dto -> dto));

        if (originDtoMap.isEmpty()) {
            throw new NotFoundDataException(rootName);
        }

        // 업데이트 목록중 기존에 없는 데이터 목록
        List<String> willFindList = roleDtoList.stream()
                .map(RoleJdbcDto::getRoleName)
                .filter(n -> !originDtoMap.containsKey(n))
                .toList();

        // 기존에 없는 데이터 원본 찾아서 원본맵에 추가
        if (willFindList.size() != 0) {
            Map<String, RoleJdbcDto> dtoMap = getDtoMap(willFindList);
            originDtoMap.putAll(dtoMap);
        }

        // 업데이트 데이터의 부모 키 세팅
        Map<String, RoleJdbcDto> updateDtoMap = new HashMap<>();
        for (RoleJdbcDto dto : roleDtoList) {
            String parentRoleName = dto.getParentRoleName();
            if (nonNull(parentRoleName)) {
                RoleJdbcDto parentDto = originDtoMap.get(parentRoleName);
                Long parentId = parentDto.getRoleId();
                dto.setParentId(parentId);
            }
            updateDtoMap.put(dto.getRoleName(), dto);
        }

        // 원본 데이터를 업데이트 데이터로 변경
        for (RoleJdbcDto origin : originDtoMap.values()) {
            origin.fillUserName();

            String roleName = origin.getRoleName();
            if (!updateDtoMap.containsKey(roleName)) {
                removeChildren.add(origin);
                continue;
            }

            RoleJdbcDto dto = updateDtoMap.get(roleName);

            if (origin.sameAs(dto)) continue;

            if (!roleName.equals(rootName)) {
                origin.setParentId(dto.getParentId());
            }

            origin.setDescription(dto.getDescription());

            updateParams.add(origin);
        }

        // 계층에서 빠진 자식들 중에서 서브 노드의 루트만 삭제
        List<RoleJdbcDto> removeDtoList = getRemoveDto(removeChildren);
        updateParams.addAll(removeDtoList);


        // 순환 참조 체크
        Long rootParentId = originDtoMap.get(rootName).getParentId();
        if (updateParams.stream()
                .map(RoleJdbcDto::getRoleId)
                .anyMatch(id -> id.equals(rootParentId))) {
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

        BeanPropertySqlParameterSource[] parameterSources = parametersMapper(updateParams);

        int[] result = jdbcTemplate.batchUpdate(sql, parameterSources);
        int count = Arrays.stream(result).sum();
        int size = updateParams.size();
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

    private Map<String, RoleJdbcDto> getDtoMap(List<String> willFindList) {
        List<RoleJdbcDto> findList = findInRoles(willFindList);

        int notFoundCnt = willFindList.size() - findList.size();
        if (notFoundCnt > 0) {
            throw new NotFoundDataException(notFoundCnt + " data");
        }

        return findList.stream()
                .collect(Collectors.toMap(RoleJdbcDto::getRoleName, dto -> dto));
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

    private List<RoleJdbcDto> getRemoveDto(List<RoleJdbcDto> removeChildren) {
        Set<Long> dtoSet = removeChildren.stream()
                .map(RoleJdbcDto::getRoleId)
                .collect(Collectors.toSet());

        return removeChildren.stream()
                .filter(child -> {
                    Long parentId = child.getParentId();
                    boolean isRemoving = !dtoSet.contains(parentId);
                    if (isRemoving) {
                        child.setParentId(null);
                    }
                    return isRemoving;
                }).toList();
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
