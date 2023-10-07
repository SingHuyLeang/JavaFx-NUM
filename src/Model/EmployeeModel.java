package Model;

import java.util.Date;

public class EmployeeModel {    
    private Integer employeeId;
    private String firstName;
    private String lastName;
    private String gender;
    private String phone;
    private String position;
    private String image;
    private Double salary;
    private Date date;
    

    public EmployeeModel(Integer employeeId, String firstName, String lastName, String gender, String phone, String position, String image,Date date) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phone = phone;
        this.position = position;
        this.image = image;
        this.date = date;
    }

    public EmployeeModel(Integer employeeId, String firstName, String lastName, String position, double salary) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.salary = salary;
    }
    

    public Integer getEmployeeId() {
        return employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getPosition() {
        return position;
    }

    public String getImage() {
        return image;
    }

    public Date getDate() {
        return date;
    }

    public Double getSalary() {
        return salary;
    }
    
    
    
}
