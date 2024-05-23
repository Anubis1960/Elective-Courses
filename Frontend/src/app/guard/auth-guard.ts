import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot } from "@angular/router";

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
