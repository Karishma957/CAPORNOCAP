# Stage 1: Build Angular
FROM node:22.12.0 AS build
WORKDIR /app

# Install deps
COPY package*.json ./
RUN npm install

# Copy source and build
COPY . .
RUN npm run build -- frontend --configuration production
RUN ls -la /app/dist/frontend/browser

# Stage 2: Serve with Nginx
FROM nginx:alpine

# Remove the default nginx page
RUN rm -rf /usr/share/nginx/html/*

# Copy Angular build output to nginx's html folder
COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html
RUN chmod -R 755 /usr/share/nginx/html

# Copy custom nginx config
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
