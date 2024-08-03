import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { ChartConfiguration, ChartOptions } from 'chart.js';
import { CategoryCount } from '../../model/category-count.model';

@Component({
  selector: 'app-course-chart',
  templateUrl: './course-chart.component.html',
  styleUrls: ['./course-chart.component.css']
})
export class CourseChartComponent implements OnChanges {

  @Input() categoryCount: CategoryCount[] = [];

  public barChartData: ChartConfiguration<'bar'>['data'] = {
    labels: [],
    datasets: [
      {
        label: 'Number of courses',
        data: [],
        backgroundColor: 'rgba(54, 162, 235, 0.2)',
        borderColor: 'rgba(54, 162, 235, 1)',
        borderWidth: 1,
        hoverBackgroundColor: 'rgba(54, 162, 235, 0.4)',
        hoverBorderColor: 'rgba(54, 162, 235, 1)'
      }
    ]
  };

  public barChartOptions: ChartOptions<'bar'> = {
    responsive: true,
    maintainAspectRatio: false,
    scales: {
      x: {
        beginAtZero: true,
        grid: {
          display: false 
        },
        ticks: {
          font: {
            size: 14 
          }
        }
      },
      y: {
        beginAtZero: true,
        grid: {
          display: true,
          color: 'rgba(0, 0, 0, 0.1)' 
        },
        ticks: {
          font: {
            size: 14
          }
        }
      }
    },
    plugins: {
      legend: {
        display: true,
        position: 'top',
        labels: {
          font: {
            size: 16
          }
        }
      },
      tooltip: {
        callbacks: {
          label: function(context) {
            return `${context.dataset.label}: ${context.raw} courses`;
          }
        }
      },
    }
  };

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['categoryCount']) {
      this.updateChart();
    }
  }
  
  private updateChart(): void {
    this.barChartData.labels = this.categoryCount.map((data) => data.category || ''); 
    this.barChartData.datasets[0].data = this.categoryCount.map((data) => data.count || 0);
  }
}
