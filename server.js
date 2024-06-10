const express = require("express");
const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");
const app = express();

const admin = require("firebase-admin");
const credential =require("./config/serviceAccountKey.json");

admin.initializeApp({
    credential: admin.credential.cert(credential)
});

const db = admin.firestore();

app.use(express.json());
app.use(express.urlencoded({extended: true}));

const JWT_SECRET = ""; 
const tokenBlacklist = new Set();

// Middleware to check if token is blacklisted
const checkBlacklist = (req, res, next) => {
    const token = req.headers["authorization"];
    if (token && tokenBlacklist.has(token)) {
        return res.status(401).json({ error: "Token is blacklisted" });
    }
    next();
};

// Middleware to verify JWT
const verifyToken = (req, res, next) => {
    const token = req.headers["authorization"];
    if (!token) {
        return res.status(403).json({ error: "No token provided" });
    }
    jwt.verify(token, JWT_SECRET, (err, decoded) => {
        if (err) {
            return res.status(401).json({ error: "Unauthorized" });
        }
        req.userId = decoded.id;
        next();
    });
};

// Signup endpoint
app.post('/signup', async (req, res) => {
    console.log(req.body);
    const user = { username, email, password } = req.body;
    
    try {
        // Check if the user already exists
        const userRef = db.collection('users').where('email', '==', email);
        const userSnapshot = await userRef.get();
        if (!userSnapshot.empty) {
            return res.status(400).json({ error: 'User already exists' });
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
        await db.collection('users').add(userDoc);

        res.status(201).json({ message: 'User registered successfully' });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// Login endpoint
app.post('/login', async (req, res) => {
    console.log(req.body);
    const { email, password } = req.body;

    try {
        // Find the user by email
        const userRef = db.collection('users').where('email', '==', email);
        const userSnapshot = await userRef.get();
        if (userSnapshot.empty) {
            return res.status(400).json({ error: 'Invalid email or password' });
        }

        // Get user data
        let userData;
        userSnapshot.forEach(doc => {
            userData = doc.data();
        });

        // Compare the password
        const isPasswordValid = await bcrypt.compare(password, userData.password);
        if (!isPasswordValid) {
            return res.status(400).json({ error: 'Invalid email or password' });
        }

        res.json({ message: 'Login successful' });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// Logout endpoint
app.post('/logout', verifyToken, checkBlacklist, (req, res) => {
    const token = req.headers["authorization"];
    if (token) {
        tokenBlacklist.add(token);
    }
    res.json({ message: 'Logout successful' });
});

const PORT = process.env.PORT || 8080;
app.listen(PORT,() => {
    console.log(`Server is running on PORT ${PORT}.`)
});
