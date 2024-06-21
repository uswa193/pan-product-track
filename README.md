# SERVER-API
## This is a Node.js server API with user authentication, journal, chat functionalities, and more.
You need to done the steps from the [ml-api](https://github.com/uswa193/pan-product-track/tree/ml-api) repository first.

## Features
- User signup and login with JWT authentication
- User profile editing
- Journal management (add and see journals)
- Chat functionality
- Emotion classification and activity recommendation
  
### Prerequisites
Make sure you have the following installed on your system:
- Python (3.8+)
- Node.js (>=14.x)
- npm (>=6.x)
- Firebase project with Firestore enabled
- Service account key for Firebase
- Docker (optional, if using Docker)

## Getting Started

Follow these instructions to set up and run the server on your local machine.

### Installation

1. **Clone the Repository**

    Clone this branch repository to your local machine using:
    ```sh
    git clone -b backend https://github.com/uswa193/pan-product-track.git
    cd pan-product-track
    ```
2. Install the dependencies:

    ```bash
    npm install
    ```
    
### Configuration

1. Create a `.env` file in the root of the project and add the following environment variables:

    ```env
    export FIREBASE_TOKEN=your_firebase_token_here
    export SESSION_SECRET=your_session_secret_here 
    export ACCESS_TOKEN_SECRET=your_access_token_secret_here
    ```
    Note: Make sure to replace `your_firebase_token_here`, `your_session_secret_here`, and `your_access_token_secret_here` with your actual secrets.

2. Add your Firebase service account key file:
   
    Place your `serviceAccountKey.json` file in the `config` folder.

3. Update the Firebase configuration in `server.js` if needed:

    ```javascript
    const credential = require("./config/serviceAccountKey.json");
    admin.initializeApp({
        credential: admin.credential.cert(credential)
    });
    ```

### Running the Server

Start the server:

    ```bash
    npm run start
    ```

    The server will run in the localhost with 3000 port (by default), open `http://localhost:3000` in your browser and you can test the API endpoints using tools like Postman.

You can see the full API documentation that we use for the mobile app [here](https://documenter.getpostman.com/view/35179868/2sA3XTf1JC).
