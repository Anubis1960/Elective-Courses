package com.Spring.application.dto;

import com.Spring.application.entity.Template;

import java.util.ArrayList;
import java.util.List;

public class TemplateDTO {
    private Long id;
    private String name;
    private Integer year;
    private String facultySection;
    private String classFlag;
    private int options;

    public TemplateDTO() {
    }

    public TemplateDTO(Long id, String templateName, Integer year, String facultySection, String classFlag, int options) {
        this.id = id;
        this.name = templateName;
        this.year = year;
        this.facultySection = facultySection;
        this.classFlag = classFlag;
        this.options = options;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public static List<TemplateDTO> convertToDTO(List<Template> templateList) {
        List<TemplateDTO> templateDTOList = new ArrayList<>();
        templateList.forEach(template -> templateDTOList.add(
                new TemplateDTO(template.getId(), template.getName(), template.getYear(),
                        template.getFacultySection(), template.getClassFlag().toString(), template.getOptions())
        ));

        return templateDTOList;
    }
}
