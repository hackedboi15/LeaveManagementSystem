package com.LeaveManagementSystem.dto;


public class LeaveBalanceDTO {
    private Long employeeId;
    private String employeeName;
    private Integer totalAllowedLeaves;
    private Integer usedLeaves;
    private Integer remainingBalance;

    // Constructors
    public LeaveBalanceDTO() {}

    public LeaveBalanceDTO(Long employeeId, String employeeName, Integer totalAllowedLeaves,
                           Integer usedLeaves, Integer remainingBalance) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.totalAllowedLeaves = totalAllowedLeaves;
        this.usedLeaves = usedLeaves;
        this.remainingBalance = remainingBalance;
    }

    // Getters and Setters
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public Integer getTotalAllowedLeaves() { return totalAllowedLeaves; }
    public void setTotalAllowedLeaves(Integer totalAllowedLeaves) {
        this.totalAllowedLeaves = totalAllowedLeaves;
    }

    public Integer getUsedLeaves() { return usedLeaves; }
    public void setUsedLeaves(Integer usedLeaves) { this.usedLeaves = usedLeaves; }

    public Integer getRemainingBalance() { return remainingBalance; }
    public void setRemainingBalance(Integer remainingBalance) {
        this.remainingBalance = remainingBalance;
    }
}
