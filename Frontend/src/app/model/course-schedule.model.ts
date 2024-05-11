
export class CourseSchedule {
    id: number | undefined;
    start_time: string | undefined;
    end_time: string | undefined;
    day: string | undefined;

    constructor(id: number, start_time: string, end_time: string, day: string) {
        this.id = id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.day = day;
    }
}