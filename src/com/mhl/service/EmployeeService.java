package com.mhl.service;

import com.mhl.dao.EmployeeDAO;
import com.mhl.domain.Employee;

/**
 * @author 程柯嘉
 * @version 1.0
 */
@SuppressWarnings({"all"})
public class EmployeeService {
    private static EmployeeDAO employeeDAO = new EmployeeDAO();

    //根据empId和pwd返回一个Employee对象,如果没有则返回null
    public static Employee getEmployeeByIdAndPwd(String empId, String pwd) {
        String sql = "select * from employee where empId=? and pwd=md5(?)";
        Employee employee = employeeDAO.querySingle(sql, Employee.class, empId, pwd);
        return employee;
    }
}
