import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { ChartConfiguration, ChartOptions } from 'chart.js';
import { CoursesCategory } from '../../model/courses-category.model';


@Component({
  selector: 'app-course-chart',
  templateUrl: './course-chart.component.html',
  styleUrls: ['./course-chart.component.css']
})
export class CourseChartComponent implements OnChanges {

  @Input() categoryData: CoursesCategory[] = [];

  public barChartData: ChartConfiguration<'bar'>['data'] = {
    labels: [],
    datasets: [
      {
        label: 'Number of courses per category',
        data: [],
        backgroundColor: 'rgba(255, 99, 132, 0.2)',
        borderColor: 'rgba(255, 99, 132, 1)',
        borderWidth: 1
      }
    ]
  };

  public barChartOptions: ChartOptions<'bar'> = {
    responsive: true,
    maintainAspectRatio: false,
    scales: {
      x: {
        beginAtZero: true
      },
      y: {
        beginAtZero: true
      }
    }
  };

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['categoryData'] && this.categoryData) {
      this.updateChart();
    }
  }
  
  private updateChart(): void {
    this.barChartData.labels = this.categoryData.map((category) => category.category || ''); 
    this.barChartData.datasets[0].data = this.categoryData.map((category) => category.numberOfCoursesPerCategory || 0);
  }
}
