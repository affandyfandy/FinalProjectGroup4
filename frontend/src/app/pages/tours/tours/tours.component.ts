import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TourService } from '../../../services/tour/tour.service';
import { Tour } from '../../../models/tour.model';

@Component({
  selector: 'app-tours',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './tours.component.html',
  styleUrl: './tours.component.css'
})
export class ToursComponent implements OnInit {
  tours: Tour[] = [];

  title: string = '';
  minPrice?: number;
  maxPrice?: number;
  location: string = '';
  sortPrice: string = '';

  constructor(private route: ActivatedRoute, private router: Router, private tourService: TourService) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.location = params['location'] || '';
      this.minPrice = params['minPrice'] ? +params['minPrice'] : undefined;
      this.maxPrice = params['maxPrice'] ? +params['maxPrice'] : undefined;
      this.sortPrice = params['sortPrice'] || '';
      
      this.searchTours();
      this.clearQueryParams();
    });
  }

  searchTours() {
    this.tourService.getTours(this.title, this.minPrice, this.maxPrice, this.location, this.sortPrice).subscribe(
      (result) => {
        this.tours = result;
      },
      (error) => {
        console.error('Error fetching tours:', error);
      }
    );
  }

  clearQueryParams() {
    this.router.navigate([], {
      queryParams: {},
      replaceUrl: true
    });
  }
}