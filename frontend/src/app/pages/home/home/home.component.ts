import { Component } from '@angular/core';
import { CommonModule } from '@angular/common'; // Import CommonModule

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule], // Add CommonModule to imports
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  // Define dummy data for the tour packages, which can be replaced with backend data later
  tours = [
    { title: 'Lorem Ipsum', days: 3, rating: 4.8, imageUrl: '/assets/img/tour1.png' },
    { title: 'Lorem Ipsum', days: 3, rating: 4.8, imageUrl: '/assets/img/tour2.png' },
    { title: 'Lorem Ipsum', days: 3, rating: 4.8, imageUrl: '/assets/img/tour3.png' },
    { title: 'Lorem Ipsum', days: 3, rating: 4.8, imageUrl: '/assets/img/tour4.png' }
  ];

  // Define dummy data for why book icons and reasons
  reasons = [
    {
      title: 'Effortless Booking',
      description: 'Quickly find and book your ideal tour package with just a few taps.',
      icon: '/assets/icons/style1.png',
    },
    {
      title: 'Personalized Experiences',
      description: 'Tailor your trips to your preferences with flexible options and rescheduling.',
      icon: '/assets/icons/style2.png',
    },
    {
      title: 'Seamless Management',
      description: 'Manage bookings, payments, and e-tickets all in one place with ease.',
      icon: '/assets/icons/style3.png',
    },
  ];
}
