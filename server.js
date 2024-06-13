const express = require("express");
const bcrypt = require("bcrypt");
const session = require("express-session");
const jwt = require("jsonwebtoken");
const app = express();
require('dotenv').config();
const PORT = process.env.PORT || 8080;

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
    cookie: { secure: false } // Set to true if using HTTPS
}));

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

        req.session.userId = userId;

        res.json({ message: 'Login berhasil!', userId: userId });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// Logout endpoint
app.post('/logout', (req, res) => {
    req.session.destroy(err => {
        if (err) {
            return res.status(500).json({ error: 'Logout failed' });
        }
        res.json({ message: 'Logout successful' });
    });
});


// Add Journal endpoint
app.post('/journal/add', async (req, res) => {
    const { note, date, emotion } = req.body;

    if (!req.session.userId) {
        return res.status(401).json({ error: 'Anda belum Login' });
    }

    const userId = req.session.userId;

    try {
        // Create journal document in Firestore
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
app.get('/journal/:userId', async (req, res) => {
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
            journals.push({ id: doc.id, ...doc.data() });
        });

        res.status(200).json({ journals: journals });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});


app.listen(PORT,() => {
    console.log(`Server is running on PORT ${PORT}.`)
});
