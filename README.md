# ML-API
## Trigger the analyze function from the model and get the API prediction endpoint :

### Prerequisites
Make sure you have the following installed on your system:
- Python 3.8+
- pip (Python package installer)
- Google Cloud SDK (for Firebase Firestore)
- Git

### Installation and Setup

Follow these steps to set up and run the application:

1. **Clone the Repository**

    Clone this branch repository to your local machine using:
    ```sh
    git clone -b ml-api https://github.com/uswa193/pan-product-track.git
    cd pan-product-track
    ```

2. **Download the Model Files**

    Download the pre-trained model files and place them in the respective directories:
    - Download the emotion classification model files and place them in the `./Model` directory.
    - Download the tokenizer files and place them in the `./tokenizer` directory.

3. **Set Up Google Cloud Firestore**

    This application uses Firebase Firestore for storing chat history and emotion analysis results. Follow these steps to set up Firestore:
    - Create a Firebase project in the [Firebase Console](https://console.firebase.google.com/).
    - Set up Firestore in your Firebase project.
    - Generate a service account key from your Firebase project settings and download the JSON credentials file.
    - Save the credentials file in the root directory of the project and name it `firebase_credentials.json`.

4. **Set Up Environment Variables**

    Create a `.env` file in the root directory of the project and add the following environment variables:
    ```env
    GOOGLE_APPLICATION_CREDENTIALS=firebase_credentials.json
    SECRET_KEY=your_secret_key
    ```

5. **Install Dependencies**

    Install the required Python packages using pip:
    ```sh
    pip install -r requirements.txt
    ```

6. **Configure the API Key for Google Generative AI**

    In the `main.py` file, replace the placeholder API key with your actual Google Generative AI API key:
    ```python
    GOOGLE_API_KEY = 'your_google_api_key'
    ```

7. **Run the Application**

    Start the FastAPI application using Uvicorn:
    ```sh
    uvicorn main:app --host 0.0.0.0 --port 8000 --reload
    ```

8. **Access the API**

    By default, the server will run on `http://localhost:8000`. You can test the API endpoints using tools like Postman or directly via the browser.
