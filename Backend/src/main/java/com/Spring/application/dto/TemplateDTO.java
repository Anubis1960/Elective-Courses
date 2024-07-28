package com.Spring.application.dto;

public class TemplateDTO {
    private Long templateId;
    private String templateName;
    private Integer year;
    private String facultySection;
    private String classFlag;
    private int options;

    public TemplateDTO() {
    }

    public TemplateDTO(Long templateId, String templateName, Integer year, String facultySection, String classFlag, int options) {
        this.templateId = templateId;
        this.templateName = templateName;
        this.year = year;
        this.facultySection = facultySection;
        this.classFlag = classFlag;
        this.options = options;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getFacultySection() {
        return facultySection;
    }

    public void setFacultySection(String facultySection) {
        this.facultySection = facultySection;
    }

    public int getOptions() {
        return options;
    }

    public void setOptions(int options) {
        this.options = options;
    }

    public String getClassFlag() {
        return classFlag;
    }

    public void setClassFlag(String classFlag) {
        this.classFlag = classFlag;
    }
}
