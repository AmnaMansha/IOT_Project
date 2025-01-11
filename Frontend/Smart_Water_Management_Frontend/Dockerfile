# Frontend Dockerfile
## File: frontend/Dockerfile
FROM node:20-alpine

WORKDIR /app

# Install dependencies
COPY package*.json ./
RUN npm install

# Copy project files
COPY tsconfig.json tsconfig.node.json ./
COPY . .


# Build the app
RUN npm run build

# Install serve to serve the built app
EXPOSE 5173
CMD ["npm", "run", "dev"]
