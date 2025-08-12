package com.LeaveManagementSystem.model;



import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Department is required")
    private String department;

    @NotNull(message = "Joining date is required")
    private LocalDate joiningDate;

    private Integer annualLeaveBalance = 30; // Random value

    public Employee() {}

    public Employee(String name, String email, String department, LocalDate joiningDate) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.joiningDate = joiningDate;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public LocalDate getJoiningDate() { return joiningDate; }
    public void setJoiningDate(LocalDate joiningDate) { this.joiningDate = joiningDate; }

    public Integer getAnnualLeaveBalance() { return annualLeaveBalance; }
    public void setAnnualLeaveBalance(Integer annualLeaveBalance) {
        this.annualLeaveBalance = annualLeaveBalance;
    }
}

