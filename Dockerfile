# Gunakan image Node.js resmi
FROM node:14

# Buat direktori kerja di dalam container
WORKDIR /usr/src/app

# Salin file package.json dan package-lock.json ke direktori kerja
COPY package*.json ./

# Install dependensi aplikasi
RUN npm install

# Salin semua file proyek ke direktori kerja
COPY . .

# Ekspos port aplikasi
EXPOSE 3000

# Define environment variable
ENV PORT 3000

# Jalankan aplikasi
CMD [ "npm", "start" ]
