package uoc.ds.pr.model;

import java.time.LocalDate;

public class Worker {

    private String id;
    private String name;
    private String surname;
    private LocalDate birthday;
    private String roleId;
    private Role role;

    public Worker(String id, String name, String surname, LocalDate birthday, String roleId) {
        this.setWorkerId(id);
        this.setName(name);
        this.setSurname(surname);
        this.setBirthday(birthday);
        this.setRoleId(roleId);

    }

    public String getWorkerId() {
        return id;
    }

    public void setWorkerId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public Role getRole(){return role;}

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
