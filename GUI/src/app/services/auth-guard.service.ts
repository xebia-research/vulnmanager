import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { VulnApiService } from './vuln-api.service';

@Injectable()
export class AuthGuardService implements CanActivate {
  constructor(public auth: VulnApiService, public router: Router) {}
  canActivate(): boolean {
    if (!this.auth.isLoggedIn()) {
      this.router.navigate(['login']);
      return false;
    }
    return true;
  }
}
