# Stage 1: Build the Angular application
FROM node:20.13-alpine as build
WORKDIR /app
COPY package*.json ./
RUN npm ci
RUN npm install -g @angular/cli
COPY . .
RUN npm run build --configuration=production

# Stage 2: Serve the application from Nginx
FROM nginx:alpine
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html
EXPOSE 4200

# docker build -t frontend .
# docker run -d -p 4200:4200 frontend
