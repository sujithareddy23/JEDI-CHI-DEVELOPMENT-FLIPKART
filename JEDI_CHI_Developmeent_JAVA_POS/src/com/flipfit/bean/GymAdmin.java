package com.flipfit.bean;

import javax.persistence.*;

@Entity
@Table(name = "gym_admins")
public class GymAdmin {
    @Id
    @Column(name = "admin_id")
    private String adminId;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "password")
    private String password;

    public String getAdminId() { return adminId; }
    public void setAdminId(String adminId) { this.adminId = adminId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
