package com.matzip.thread.role.adapter.out_;

import com.matzip.thread.role.domain.Role;
import com.matzip.thread.role.domain.RoleEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.Objects.*;

@Getter
@Setter
public class RoleJdbcDto{
    private Long roleId;
    private String roleName;
    private String description;
    private Long parentId;
    private String parentRoleName;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String createdBy;
    private String lastModifiedBy;

    public static List<RoleJdbcDto> from(RoleEntity roleEntity) {
        if (isNull(roleEntity) || isNull(roleEntity.getRole())) return null;

        ArrayList<RoleJdbcDto> dtoList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        Queue<RoleEntity> queue = new LinkedList<>();
        queue.offer(roleEntity);

        while (!queue.isEmpty()) {
            RoleEntity entity = queue.poll();
            RoleJdbcDto dto = new RoleJdbcDto();
            String roleName = entity.getName();

            dto.setRoleName(roleName);
            dto.setDescription(entity.getDescription());

            if (map.containsKey(roleName)) {
                dto.setParentRoleName(map.get(roleName));
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (nonNull(authentication)) {
                String username = authentication.getName();
                dto.setCreatedBy(username);
                dto.setLastModifiedBy(username);
            }

            entity.getChildren().forEach(c -> {
                map.put(c.getName(), roleName);
                queue.offer(c);
            });

            dtoList.add(dto);
        }
        return dtoList;
    }

    public RoleEntity toEntity() {
        return new RoleEntity(Role.valueOf(roleName), description, List.of());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleJdbcDto that = (RoleJdbcDto) o;
        return Objects.equals(roleName, that.roleName) && Objects.equals(description, that.description) && Objects.equals(parentRoleName, that.parentRoleName);
    }

    @Override
    public int hashCode() {
        return hash(roleName, description, parentRoleName);
    }
}
