package com.medicare.smarthealthcare.dto;

import com.medicare.smarthealthcare.entity.AppUser;
import com.medicare.smarthealthcare.entity.Role;

public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private boolean enabled;

    public UserResponse(AppUser user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.enabled = user.isEnabled();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
    public boolean isEnabled() { return enabled; }
}
