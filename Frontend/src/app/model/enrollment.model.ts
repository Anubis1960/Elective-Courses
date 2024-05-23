
export class Enrollment {
    id: number | undefined;
    studentName: string | undefined;
    courseName: string | undefined;
    priority: number | undefined;
    status: string | undefined;

    constructor(id: number, studentName: string, courseName: string, priority: number, status: string) {
        this.id = id;
        this.studentName = studentName;
        this.courseName = courseName;
        this.priority = priority;
        this.status = status;
    }
}