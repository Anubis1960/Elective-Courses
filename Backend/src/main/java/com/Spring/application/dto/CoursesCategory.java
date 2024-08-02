package com.Spring.application.dto;

public class CoursesCategory {
    private String category;
    private Integer numberOfCoursesPerCategory;

    public CoursesCategory(){}
    public CoursesCategory(String category, Long numberOfCoursesPerCategory) {
        this.category = category;
        this.numberOfCoursesPerCategory = numberOfCoursesPerCategory.intValue();
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getNumberOfCoursesPerCategory() {
        return numberOfCoursesPerCategory;
    }

    public void setNumberOfCoursesPerCategory(Integer numberOfCoursesPerCategory) {
        this.numberOfCoursesPerCategory = numberOfCoursesPerCategory;
    }


}
