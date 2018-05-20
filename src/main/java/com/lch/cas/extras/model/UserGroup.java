package com.lch.cas.extras.model;

import javax.persistence.*;

@Entity
@Table(name = "user_group_mappings")
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private int groupId;
    private int userId;
    private boolean admin;

    public UserGroup() {
    }

    public UserGroup(int userId, int groupId, boolean admin) {
        this.userId = userId;
        this.groupId = groupId;
        this.admin = admin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
