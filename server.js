const express = require("express");
const bcrypt = require("bcrypt");
const session = require("express-session");
const jwt = require("jsonwebtoken");
const app = express();
const fetch = require("node-fetch");
require('dotenv').config();
const PORT = process.env.PORT || 3000;

const admin = require("firebase-admin");
const credential =require("./config/serviceAccountKey.json");

admin.initializeApp({
    credential: admin.credential.cert(credential)
});

const db = admin.firestore();

app.use(express.json());
app.use(express.urlencoded({extended: true}));
app.use(session({
    secret: process.env.SESSION_SECRET,
    resave: false,
    saveUninitialized: true,
    cookie: {secure: process.env.NODE_ENV === 'production'} // Set to true if using HTTPS
}));

const revokedTokens = new Set();

// Middleware untuk memverifikasi token JWT
function authenticateToken(req, res, next) {
    const authHeader = req.headers['authorization'];
    const token = authHeader && authHeader.split(' ')[1];
    if (token == null) return res.status(401).json({ error: 'Anda belum login' });
        // Check if the token is revoked
        if (revokedTokens.has(token)) {
            return res.status(403).json({ error: 'Token tidak valid atau sudah kedaluwarsa' });
        }

    jwt.verify(token, process.env.ACCESS_TOKEN_SECRET, (err, user) => {
        if (err) return res.status(403).json({ error: 'Token tidak valid atau sudah kedaluwarsa' });
        req.user = user;
        next();
    });
}

// Middleware untuk memastikan user tidak bisa akses fitur setelah logout
function ensureLoggedOut(req, res, next) {
    if (req.session.userId) {
        return res.status(401).json({ error: 'Anda harus login untuk mengakses fitur ini' });
    }
    next();
}


// Signup endpoint
app.post('/signup', async (req, res) => {
    console.log(req.body);
    const { username, email, password } = req.body;
    
    // Validasi Data
    if (!username || !email || !password) {
        return res.status(400).json({ error: 'Tolong masukkan username, email, dan password' });
    }

    // Validasi Email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        return res.status(400).json({ error: 'Tolong masukkan email yang valid!' });
    }

    try {
        // Check if the user already exists
        const userRef = db.collection('users').where('email', '==', email);
        const userSnapshot = await userRef.get();
        if (!userSnapshot.empty) {
            return res.status(400).json({ error: 'Pengguna telah terdaftar!' });
        }

        // Hash the password
        const hashedPassword = await bcrypt.hash(password, 10);

        // Create user document in Firestore
        const userDoc = {
            username: username,
            email: email,
            password: hashedPassword,
            createdAt: admin.firestore.FieldValue.serverTimestamp()
        };
        const newUserRef = await db.collection('users').add(userDoc);
        const userId = newUserRef.id;

        // Update user document with userId
        await newUserRef.update({ userId: userId });

        res.status(201).json({ message: 'Pengguna berhasil terdaftar!', userId: newUserRef.id });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// Login endpoint
app.post('/login', async (req, res) => {
    console.log(req.body);
    const { email, password } = req.body;

    // Validasi Data
    if (!email || !password) {
        return res.status(400).json({ error: 'Masukkan email dan password' });
    }

    // Validasi Email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        return res.status(400).json({ error: 'Masukkan alamat email yang valid!' });
    }

    try {
        // Find the user by email
        const userRef = db.collection('users').where('email', '==', email);
        const userSnapshot = await userRef.get();
        if (userSnapshot.empty) {
            return res.status(400).json({ error: 'Invalid email or password' });
        }

        // Get user data
        let userData;
        let userId;
        userSnapshot.forEach(doc => {
            userData = doc.data();
            userId = doc.id;
        });

        const isPasswordValid = await bcrypt.compare(password, userData.password);
        if (!isPasswordValid) {
            return res.status(400).json({ error: 'Email/password tidak valid' });
        }

         // Buat token JWT
         const token = jwt.sign({ userId: userId, email: email }, process.env.ACCESS_TOKEN_SECRET, { expiresIn: '30d' });

        // Simpan userId di sesi (session)
        req.session.userId = userId;

        res.json({ message: 'Login berhasil!', token: token });
     } catch (error) {
         res.status(500).json({ error: error.message });
     }
 });

// Logout endpoint
app.post('/logout', (req, res) => {
    const authHeader = req.headers['authorization'];
    const token = authHeader && authHeader.split(' ')[1];
    if (token) {
        revokedTokens.add(token); // Tambahkan token ke daftar pembatalan
    }
    req.session.destroy(err => {
        if (err) {
            return res.status(500).json({ error: 'Logout failed' });
        }
        res.json({ message: 'Logout successful' });
    });
});

// Secure endpoints
app.get('/secure-endpoint', authenticateToken, (req, res) => {
    res.json({ message: 'Anda terautentikasi!' });
});

// Add Journal endpoint
app.post('/journal/add', authenticateToken, async (req, res) => {
    const { note, date, emotion } = req.body;
    const userId = req.user.userId;

    try {
        const journalDoc = {
            note: note,
            date: new Date(date),
            emotion: emotion,
            userId: userId,
            createdAt: admin.firestore.FieldValue.serverTimestamp()
        };
        await db.collection('journals').add(journalDoc);

        res.status(201).json({ message: 'Journal berhasil ditambahkan' });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});


// Get Journals endpoint
app.get('/journal/:userId',authenticateToken, async (req, res) => {
    const { userId } = req.params;

    try {
        // Retrieve journal documents from Firestore
        const journalRef = db.collection('journals').where('userId', '==', userId).orderBy('date', 'desc');
        const journalSnapshot = await journalRef.get();
        if (journalSnapshot.empty) {
            return res.status(404).json({ error: 'No journals found for this user' });
        }

        let journals = [];
        journalSnapshot.forEach(doc => {
            const data = doc.data();
            // Convert Firestore timestamp to JavaScript Date object
            const jsDate = data.date.toDate();
            // Format the date as MM-DD-YYYY
            const formattedDate = `${jsDate.getMonth() + 1}-${jsDate.getDate()}-${jsDate.getFullYear()}`;


            journals.push({
                id: doc.id,
                date: formattedDate,
                note: data.note,
                emotion: data.emotion,
                userId: data.userId,
                createdAt: data.createdAt
            });
        });

        res.status(200).json({ journals: journals });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// send chat
app.post('/chat', authenticateToken, async (req, res) => {
    const { message } = req.body;
    const userId = req.user.userId;


    try {
        const response = await fetch(`https://pan-app-bjukyhsk7q-et.a.run.app/chat/${userId}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ text: message })
        });

        if (!response.ok) {
            throw new Error(`Server error: ${response.statusText}`);
        }

        const data = await response.json();

        // Send the same response back to the client
        res.status(201).json(data);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

app.get('/recommend-activity', authenticateToken, async (req, res) => {
    const userId = req.session.userId;

    try {
        const response = await fetch(`https://pan-app-bjukyhsk7q-et.a.run.app/recommend_activity/${userId}`)
        const result = await response.json();

        res.status(201).json(result);
    } catch (error) {
        res.status(500).json({ error: error.message });
        
    }
});

// Endpoint to get the last classified emotion
app.get('/classify_emotion', authenticateToken, async (req, res) => {
    const userId = req.session.userId;

    try {
        const response = await fetch(`https://pan-app-bjukyhsk7q-et.a.run.app/classify_emotion/${userId}`)
        const result = await response.json();

        res.status(201).json(result);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});


// Edit Profile endpoint
app.post('/profile/edit', authenticateToken, async (req, res) => {
    const { username, email, password } = req.body;
    const userId = req.session.userId;

    try {
        // Validasi data yang diterima
        if (!username || !email || !password) {
            return res.status(400).json({ error: 'Harap masukkan username, email, dan password' });
        }

        // Validasi email
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            return res.status(400).json({ error: 'Harap masukkan email yang valid!' });
        }

        // Cek apakah email sudah digunakan oleh pengguna lain
        const userRef = db.collection('users').where('email', '==', email);
        const userSnapshot = await userRef.get();
        if (!userSnapshot.empty) {
            // Jika email sudah ada, pastikan tidak sama dengan pengguna saat ini
            let emailExists = false;
            userSnapshot.forEach(doc => {
                if (doc.id !== userId) {
                    emailExists = true;
                }
            });
            if (emailExists) {
                return res.status(400).json({ error: 'Email sudah digunakan oleh pengguna lain' });
            }
        }

        // Hash password baru jika ada perubahan password
        const hashedPassword = await bcrypt.hash(password, 10);

        // Update data pengguna di Firestore
        const userDocRef = db.collection('users').doc(userId);
        await userDocRef.update({
            username: username,
            email: email,
            password: hashedPassword,  // Update password yang ter-hash
        });

        res.status(200).json({ message: 'Profil berhasil diperbarui' });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// Middleware untuk menangani kesalahan 404
app.use((req, res, next) => {
    res.status(404).json({ error: 'Tidak ditemukan' });
});

// Middleware untuk menangani kesalahan server (500)
app.use((err, req, res, next) => {
    console.error(err.stack);
    res.status(500).json({ error: 'Terjadi kesalahan pada server' });
});

console.log(`PORT environment variable: ${process.env.PORT}`);
console.log(`Server will listen on PORT: ${PORT}`);

app.listen(PORT,() => {
    console.log(`Server is running on PORT ${PORT}.`);
});
