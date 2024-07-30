import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { ChartConfiguration, ChartOptions } from 'chart.js';

interface CategoryData {
  labels: string[];
  data: number[];
}

@Component({
  selector: 'app-course-chart',
  templateUrl: './course-chart.component.html',
  styleUrls: ['./course-chart.component.css']
})
export class CourseChartComponent implements OnChanges {

  @Input() categoryData: CategoryData = { labels: [], data: [] };

  public barChartData: ChartConfiguration<'bar'>['data'] = {
    labels: [],
    datasets: [
      {
        label: 'Number of courses',
        data: [],
        backgroundColor: 'rgba(255, 99, 132, 0.2)',
        borderColor: 'rgba(255, 99, 132, 1)',
        borderWidth: 1
      }
    ]
  };

  public barChartOptions: ChartOptions<'bar'> = {
    responsive: true
  };

  public barChartType: ChartConfiguration<'bar'>['type'] = 'bar';

  constructor() {}
  ngOnInit(): void {
    this.barChartData.labels = this.categoryData.labels;
    this.barChartData.datasets[0].data = this.categoryData.data;
  }
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['categoryData'] && this.categoryData) {
      this.barChartData.labels = this.categoryData.labels;
      this.barChartData.datasets[0].data = this.categoryData.data;
    }
  }
}
