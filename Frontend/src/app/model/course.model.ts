
export class Course {
    id: number | undefined;
    name: string | undefined;
    description: string | undefined;
    category: string | undefined;
    maximum_students: number | undefined;
    faculty_section: string | undefined;
    year: number | undefined;
    teacher_name: string | undefined;

    constructor(id: number, name: string, description: string, category: string, maximum_students: number, faculty_section: string, year: number, teacher_name: string) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.maximum_students = maximum_students;
        this.faculty_section = faculty_section;
        this.year = year;
        this.teacher_name = teacher_name;
    }
}