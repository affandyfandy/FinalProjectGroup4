import { jwtDecode } from "jwt-decode";
import { Component, OnInit, HostListener } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HlmAvatarImageDirective, HlmAvatarComponent, HlmAvatarFallbackDirective } from '@spartan-ng/ui-avatar-helm';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { AuthService } from '../../../services/auth/auth.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    CommonModule, 
    HlmAvatarImageDirective, HlmAvatarComponent, HlmAvatarFallbackDirective,
    HlmButtonDirective
  ],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  username = '';
  role = '';
  isHome = true;
  isMenuOpen = false;
  showLogout = false;
  isOpen = false;
  isScrolled = false; // Track whether the page is scrolled

  constructor(private router: Router, private authService: AuthService) {}

  ngOnInit(): void {
    const token = this.authService.getToken();
    
    if (token != null) {
      const decodedToken: any = jwtDecode(token);
      this.username = decodedToken.sub as string; // Get the 'sub' value for the username
      // Capitalize the first letter of the role
      if (decodedToken.roles && decodedToken.roles.length > 0) {
        this.role = decodedToken.roles[0].toLowerCase();
        this.role = this.role.charAt(0).toUpperCase() + this.role.slice(1);
      }
    } else {
      this.username = "Mr. Lorem Ipsum";
      this.role = "Guest"; // Default role if no token
    }

    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.isHome = event.url == '/';
        this.isScrolled = !this.isHome;
      }
    });
  }

  @HostListener('window:scroll', [])
  onScroll(): void {
    const scrollPosition = window.scrollY;
    
    // If the user is at the top of the page and it's the home page, set isScrolled to false
    if (scrollPosition <= 50 && this.isHome) {
      this.isScrolled = false;
    } 
    // Otherwise, set isScrolled to true when scroll position is greater than 50
    else if (scrollPosition > 50 || !this.isHome) {
      this.isScrolled = true;
    }
  }

  toggleMenu() {
    this.isOpen = !this.isOpen;
  }

  isLoggedIn(): boolean {
    return this.authService.getToken() != null;
  }

  handleLogout() {
    this.authService.logout();
    this.router.navigate(['/login']); // Redirect to login page after logout
  }
}