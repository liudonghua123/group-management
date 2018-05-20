package com.lch.cas.extras.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lch.cas.extras.model.UserGroup.UserGroupCompositeId;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "user_group_mappings")
@IdClass(UserGroupCompositeId.class)
public class UserGroup {

    @Id
    private int groupId;
    @Id
    private int userId;

//    @JsonIgnore
//    @Transient
//    private UserGroupCompositeId id;

    private boolean isAdmin;

    public UserGroup() {
    }

    public UserGroup(int userId, int groupId, boolean isAdmin) {
        this.userId = userId;
        this.groupId = groupId;
        this.isAdmin = isAdmin;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
//    public UserGroupCompositeId getId() {
//        return id;
//    }
//
//    public void setId(UserGroupCompositeId id) {
//        this.id = id;
//    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public static class UserGroupCompositeId implements Serializable {
        private int groupId;
        private int userId;

        public UserGroupCompositeId() {
        }

        public UserGroupCompositeId(int groupId, int userId) {
            this.groupId = groupId;
            this.userId = userId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getGroupId() {
            return groupId;
        }

        public void setGroupId(int groupId) {
            this.groupId = groupId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserGroupCompositeId that = (UserGroupCompositeId) o;
            return groupId == that.groupId &&
                    userId == that.userId;
        }

        @Override
        public int hashCode() {

            return Objects.hash(groupId, userId);
        }
    }
}
