package com.Spring.application.dto;
public class EnrollmentExporter extends StudentExporter {
    private String courseName;
    private String category;
    private String teacher;
    private Long numberOfStudents;
    private Double avgGrade;

    public EnrollmentExporter(EnrollmentBuilder enrollmentBuilder) {
        super(enrollmentBuilder); // This calls the StudentExporter constructor
        this.courseName = enrollmentBuilder.courseName;
        this.category = enrollmentBuilder.category;
        this.teacher = enrollmentBuilder.teacher;
        this.numberOfStudents = enrollmentBuilder.numberOfStudents;
        this.avgGrade = enrollmentBuilder.avgGrade;
    }

    public static class EnrollmentBuilder extends StudentBuilder {
        private String courseName = null;
        private String category = null;
        private String teacher = null;
        private Long numberOfStudents = null;
        private Double avgGrade = null;

        public EnrollmentBuilder courseName(String courseName) {
            this.courseName = courseName;
            return this;
        }

        public EnrollmentBuilder category(String category) {
            this.category = category;
            return this;
        }

        public EnrollmentBuilder teacher(String teacher) {
            this.teacher = teacher;
            return this;
        }

        public EnrollmentBuilder numberOfStudents(Long numberOfStudents) {
            this.numberOfStudents = numberOfStudents;
            return this;
        }

        public EnrollmentBuilder avgGrade(Double avgGrade) {
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
