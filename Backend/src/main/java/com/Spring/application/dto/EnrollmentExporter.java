package com.Spring.application.dto;
public class EnrollmentExporter extends StudentExporter {
    private String courseName;
    private String category;
    private String teacher;
    private Long numberOfStudents;
    private Double avgGrade;

    public EnrollmentExporter(Builder builder) {
        super(builder); // This calls the StudentExporter constructor
        this.courseName = builder.courseName;
        this.category = builder.category;
        this.teacher = builder.teacher;
        this.numberOfStudents = builder.numberOfStudents;
        this.avgGrade = builder.avgGrade;
    }

    public static class Builder extends StudentExporter.Builder {
        private String courseName = null;
        private String category = null;
        private String teacher = null;
        private Long numberOfStudents = null;
        private Double avgGrade = null;

        public Builder courseName(String courseName) {
            this.courseName = courseName;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder teacher(String teacher) {
            this.teacher = teacher;
            return this;
        }

        public Builder numberOfStudents(Long numberOfStudents) {
            this.numberOfStudents = numberOfStudents;
            return this;
        }

        public Builder avgGrade(Double avgGrade) {
            this.avgGrade = avgGrade;
            return this;
        }

        public EnrollmentExporter build() {
            return new EnrollmentExporter(this);
        }
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public Long getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(Long numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public Double getAvgGrade() {
        return avgGrade;
    }

    public void setAvgGrade(Double avgGrade) {
        this.avgGrade = avgGrade;
    }
}
