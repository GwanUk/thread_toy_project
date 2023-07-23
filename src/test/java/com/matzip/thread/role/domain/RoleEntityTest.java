package com.matzip.thread.role.domain;

class RoleEntityTest {

//    @Test
//    @DisplayName("롤이 널인 생성하면 예외 발생")
//    void construct_role_null() {
//        // expected
//        assertThatThrownBy(() -> new RoleEntity(null, null, null, List.of()))
//                .isInstanceOf(NullArgumentException.class)
//                .hasMessage("Argument is empty: role");
//    }
//
//    @Test
//    @DisplayName("자식이 널인 생성하면 예외 발생")
//    void construct_children_null() {
//        // expected
//        assertThatThrownBy(() -> new RoleEntity(ROLE_USER, null, null, null))
//                .isInstanceOf(NullPointerException.class);
//    }
//
//    @Test
//    @DisplayName("롤 계층 문자열")
//    void getHierarchyString() {
//        // given
//        RoleEntity roleEntity = new RoleEntity(ROLE_USER, null, ROLE_ADMIN, List.of());
//
//        // when
//        String hierarchyString = roleEntity.getHierarchyString();
//
//        // then
//        assertThat(hierarchyString).isEqualTo("ROLE_ADMIN > ROLE_USER\n");
//    }
//
//    @Test
//    @DisplayName("롤 계층 문자열 부모가 널이면 예외 발생")
//    void getHierarchyString_parent_null() {
//        // given
//        RoleEntity roleEntity = new RoleEntity(ROLE_USER, null, null, List.of());
//
//        // expected
//        assertThatThrownBy(roleEntity::getHierarchyString)
//                .isInstanceOf(NullPointerException.class);
//    }
}