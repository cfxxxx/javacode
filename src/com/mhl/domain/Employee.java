package com.mhl.domain;

/**
 * @author 程柯嘉
 * @version 1.0
 * 这是一个javabean，和employee表对应
 */
@SuppressWarnings({"all"})
public class Employee {
    private Integer id;
    private String empId;
    private String name;
    private String pwd;
    private String job;

    public Employee() {
    }

    public Employee(Integer id, String empId, String name, String pwd, String job) {
        this.id = id;
        this.empId = empId;
        this.name = name;
        this.pwd = pwd;
        this.job = job;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
