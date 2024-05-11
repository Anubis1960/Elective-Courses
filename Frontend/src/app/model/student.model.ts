
export class Student {
    id: number | undefined;
    name: string | undefined;
    email: string | undefined;
    role: string | undefined;
    faculty_section: string | undefined;
    year: number | undefined;
    grade: number | undefined;

    constructor(id: number, name: string, email: string, role: string, faculty_section: string, year: number, grade: number) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.faculty_section = faculty_section;
        this.year = year;
        this.grade = grade;
    }
}