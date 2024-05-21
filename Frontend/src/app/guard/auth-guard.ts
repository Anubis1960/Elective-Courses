import { Injectable, inject } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot } from "@angular/router";

export const canActivateGuard: CanActivateFn = (
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
) => {
    if (typeof sessionStorage !== 'undefined') {
        console.error(sessionStorage.getItem('user'));
        if (sessionStorage.getItem('user')) {
            return true;
        } else {
            inject(Router).navigateByUrl('/login');
            return false;
        }
    } else {
        console.error('sessionStorage is not defined');
        inject(Router).navigateByUrl('/login');
        console.error('returning false')
        return false;
    }
};

export const canActivateStudentGuard: CanActivateFn = (
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
) => {
    if (typeof sessionStorage !== 'undefined') {
        const user = sessionStorage.getItem('user');
        const role = JSON.parse(user || '{}').role;
        if (role === 'STUDENT') {
            return true;
        }
    }
    return false;
};

export const canActivateAdminGuard: CanActivateFn = (
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
) => {
    if (typeof sessionStorage !== 'undefined') {
        const user = JSON.parse(sessionStorage.getItem('user') || '{}');
        console.error(user.role);
        if (user.role === 'ADMIN') {
            return true;
        }
    }
    //inject(Router).navigateByUrl('/login');
    return false;
};
