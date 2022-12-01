package com.example.excelapp.bean;

import java.io.Serializable;
import java.util.Objects;

public class ContextBean implements Serializable {

    public String customer;
    public String BusinessDepartment;
    public int Id;
    public String customerNum;
    public String customerName;
    public String serviceName;
    public String ReturnTaskName;
    public String AllocationMethod;
    public String TaskStatus;
    public String ReturnChannel;
    public String WayVisit;
    public String RevisitDays;
    public String RevisitNum;
    public String RevisitNameNum;
    public String TaskRemarks;
    public String RevisitDetails;
    public String isPrize;

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getBusinessDepartment() {
        return BusinessDepartment;
    }

    public void setBusinessDepartment(String businessDepartment) {
        BusinessDepartment = businessDepartment;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCustomerNum() {
        return customerNum;
    }

    public void setCustomerNum(String customerNum) {
        this.customerNum = customerNum;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getReturnTaskName() {
        return ReturnTaskName;
    }

    public void setReturnTaskName(String returnTaskName) {
        ReturnTaskName = returnTaskName;
    }

    public String getAllocationMethod() {
        return AllocationMethod;
    }

    public void setAllocationMethod(String allocationMethod) {
        AllocationMethod = allocationMethod;
    }

    public String getTaskStatus() {
        return TaskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        TaskStatus = taskStatus;
    }

    public String getReturnChannel() {
        return ReturnChannel;
    }

    public void setReturnChannel(String returnChannel) {
        ReturnChannel = returnChannel;
    }

    public String getWayVisit() {
        return WayVisit;
    }

    public void setWayVisit(String wayVisit) {
        WayVisit = wayVisit;
    }

    public String getRevisitDays() {
        return RevisitDays;
    }

    public void setRevisitDays(String revisitDays) {
        RevisitDays = revisitDays;
    }

    public String getRevisitNum() {
        return RevisitNum;
    }

    public void setRevisitNum(String revisitNum) {
        RevisitNum = revisitNum;
    }

    public String getRevisitNameNum() {
        return RevisitNameNum;
    }

    public void setRevisitNameNum(String revisitNameNum) {
        RevisitNameNum = revisitNameNum;
    }

    public String getTaskRemarks() {
        return TaskRemarks;
    }

    public void setTaskRemarks(String taskRemarks) {
        TaskRemarks = taskRemarks;
    }

    public String getRevisitDetails() {
        return RevisitDetails;
    }

    public void setRevisitDetails(String revisitDetails) {
        RevisitDetails = revisitDetails;
    }

    public String getIsPrize() {
        return isPrize;
    }

    public void setIsPrize(String isPrize) {
        this.isPrize = isPrize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContextBean that = (ContextBean) o;
        return Objects.equals(customer, that.customer) &&
                Objects.equals(BusinessDepartment, that.BusinessDepartment) &&
                Objects.equals(Id, that.Id) &&
                Objects.equals(customerNum, that.customerNum) &&
                Objects.equals(customerName, that.customerName) &&
                Objects.equals(serviceName, that.serviceName) &&
                Objects.equals(ReturnTaskName, that.ReturnTaskName) &&
                Objects.equals(AllocationMethod, that.AllocationMethod) &&
                Objects.equals(TaskStatus, that.TaskStatus) &&
                Objects.equals(ReturnChannel, that.ReturnChannel) &&
                Objects.equals(WayVisit, that.WayVisit) &&
                Objects.equals(RevisitDays, that.RevisitDays) &&
                Objects.equals(RevisitNum, that.RevisitNum) &&
                Objects.equals(RevisitNameNum, that.RevisitNameNum) &&
                Objects.equals(TaskRemarks, that.TaskRemarks) &&
                Objects.equals(RevisitDetails, that.RevisitDetails) &&
                Objects.equals(isPrize, that.isPrize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, BusinessDepartment, Id, customerNum, customerName, serviceName, ReturnTaskName, AllocationMethod, TaskStatus, ReturnChannel, WayVisit, RevisitDays, RevisitNum, RevisitNameNum, TaskRemarks, RevisitDetails, isPrize);
    }

    @Override
    public String toString() {
        return "ContextBean{" +
                "customer='" + customer + '\'' +
                ", BusinessDepartment='" + BusinessDepartment + '\'' +
                ", Id=" + Id +
                ", customerNum='" + customerNum + '\'' +
                ", customerName='" + customerName + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", ReturnTaskName='" + ReturnTaskName + '\'' +
                ", AllocationMethod='" + AllocationMethod + '\'' +
                ", TaskStatus='" + TaskStatus + '\'' +
                ", ReturnChannel='" + ReturnChannel + '\'' +
                ", WayVisit='" + WayVisit + '\'' +
                ", RevisitDays='" + RevisitDays + '\'' +
                ", RevisitNum='" + RevisitNum + '\'' +
                ", RevisitNameNum='" + RevisitNameNum + '\'' +
                ", TaskRemarks='" + TaskRemarks + '\'' +
                ", RevisitDetails='" + RevisitDetails + '\'' +
                ", isPrize='" + isPrize + '\'' +
                '}';
    }
}
