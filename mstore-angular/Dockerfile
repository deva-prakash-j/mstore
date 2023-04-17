FROM node:16-alpine as build

WORKDIR /usr/src/app
COPY package.json ./
RUN npm install
COPY ./ ./
RUN npm run build

FROM nginx
EXPOSE 80
COPY --from=build /usr/src/app/dist/mstore-angular /usr/share/nginx/html