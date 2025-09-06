import { HttpInterceptorFn } from "@angular/common/http";

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  console.log("🔎 Interceptor called for:", req.url);

  const token = localStorage.getItem('capnocap_auth_token');
  console.log("Interceptor token:", token);

  if (token) {
    const cloned = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
    return next(cloned);
  }

  return next(req);
};
