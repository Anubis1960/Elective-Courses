package com.Spring.application.dto;

public class StudentExporter {
    protected String studentName;
    protected String email;
    protected Float grade;
    protected String facultySection;
    protected Integer year;

    public StudentExporter(StudentBuilder studentBuilder) {
        this.studentName = studentBuilder.name;
        this.email = studentBuilder.email;
        this.grade = studentBuilder.grade;
        this.facultySection = studentBuilder.section;
        this.year = studentBuilder.year;
    }

    public static class StudentBuilder {
        protected String name = null;
        protected String email = null;
        protected Float grade = null;
        protected String section = null;
        protected Integer year = null;

        public StudentBuilder name(String name) {
            this.name = name;
            return this;
        }

        public StudentBuilder email(String email) {
            this.email = email;
            return this;
        }

        public StudentBuilder grade(Float grade) {
            this.grade = grade;
            return this;
        }

        public StudentBuilder section(String section) {
            this.section = section;
            return this;
        }

        public StudentBuilder year(Integer year) {
            this.year = year;
            return this;
        }

        public StudentExporter build() {
            return new StudentExporter(this);
        }
    }
    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacultySection() {
        return facultySection;
    }

    public void setFacultySection(String facultySection) {
        this.facultySection = facultySection;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Float getGrade() {
        return grade;
    }

    public void setGrade(Float grade) {
        this.grade = grade;
    }
}
