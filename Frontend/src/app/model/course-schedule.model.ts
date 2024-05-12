
export class CourseSchedule {
    id: number | undefined;
    startTime: string | undefined;
    endTime: string | undefined;
    day: string | undefined;

    constructor(id: number, start_time: string, end_time: string, day: string) {
        this.id = id;
        this.startTime = start_time;
        this.endTime = end_time;
        this.day = day;
    }
}