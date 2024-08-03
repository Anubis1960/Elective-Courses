export class CategoryCount{
    category: string | undefined;
    count: number | undefined;

    constructor(category: string, count:number){
        this.category = category;
        this.count = count;
    }

}