from fastapi import FastAPI, HTTPException, Depends
from pydantic import BaseModel
import google.generativeai as genai
from transformers import pipeline, TFBertForSequenceClassification, BertTokenizer
from typing import List
from google.cloud import firestore
import random
import textwrap
import uvicorn
import os
from jose import JWTError, jwt
from datetime import datetime, timedelta
from dotenv import load_dotenv
import json
import logging

app = FastAPI()

# Load environment variables from .env file
load_dotenv()

# Configure Google Generative AI
GOOGLE_API_KEY = 'AIzaSyD1K8V2IPAg6W53Bl9mY-dAgTZVdXXh0Ys'
genai.configure(api_key=GOOGLE_API_KEY)
model_gemini = genai.GenerativeModel('gemini-1.5-pro')

# Firebase Firestore Configuration
db = firestore.Client()

# JWT Configuration
SECRET_KEY = "your_secret_key"
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES = 30

# Load Emotion Classification Model and Tokenizer
model_path = ('./Model')
tokenizer_path = ('./tokenizer')

# Load the model and tokenizer
model = TFBertForSequenceClassification.from_pretrained(model_path)
tokenizer = BertTokenizer.from_pretrained(tokenizer_path)

# Initialize the pipeline
nlp = pipeline(
    "sentiment-analysis",
    model=model_path,
    tokenizer=tokenizer_path,
    return_all_scores=True
)

# Load Recommended Activities
with open('./recommended_activities.json', 'r') as json_file:
    recommended_activities = json.load(json_file)

label_mapping = {'Marah': 'marah', 'Takut': 'takut', 'Senang': 'senang', 'Cinta': 'cinta', 'Sedih': 'sedih'}

class UserInput(BaseModel):
    text: str

@app.post("/chat/{user_id}")
async def chat(user_id: str, user_input: UserInput):
    try:
        text = user_input.text

        # Save chat input to Firestore under user_id
        user_ref = db.collection('users').document(user_id)
        chat_history_ref = user_ref.collection('chat_history').document()
        chat_history_ref.set({
            'timestamp': datetime.utcnow(),
            'text': text
        })
        logging.debug(f"Chat input saved to Firestore for user {user_id}")

        # Generate response using Generative AI
        response = model_gemini.generate_content(f"Anggap dia temanmu yang sedang bercerita. Dia bercerita/membalas kalau {text}. Respon dia sebagai teman. Jangan gunakan bahasa yang terlalu kaku dan jangan terlalu panjang.")
        response_text = response.text

        # Emotion Analysis
        probabilities = predict_text_probabilities(nlp(text), text)
        max_emotion = max(probabilities, key=probabilities.get)
        max_emotion_prob = probabilities[max_emotion]

        # Save emotion analysis result to Firestore
        emotion_data = {
            'timestamp': datetime.utcnow(),
            'text': text,
            'emotion': max_emotion,
            'emotion_probability': max_emotion_prob
        }

        # Ensure the user document exists before updating emotions
        if not user_ref.get().exists:
            user_ref.set({})  # Create the user document if it doesn't exist

        emotion_ref = chat_history_ref.collection('emotion').document()
        emotion_ref.set(emotion_data)
        # chat_history_ref.collection('emotions').document("emotion_history").update({
        #     'history': firestore.ArrayUnion([emotion_data])
        # })

        logging.debug("Emotion analysis result saved to Firestore")

        return {
            "chat_response": response_text
        }
    except Exception as e:
        logging.error(f"chat Error: {e}")
        raise HTTPException(status_code=500, detail="Internal Server Error")


@app.get("/classify_emotion")
async def classify_emotion():
    try:
        # Get the latest emotion analysis result from Firestore
        emotion_doc = db.collection('emotions').document("emotion_history").get()
        if emotion_doc.exists:
            emotion_data = emotion_doc.to_dict()
            return {
                "emotion": emotion_data['emotion'],
                "emotion_probability": emotion_data['probability']
            }
        else:
            raise HTTPException(status_code=404, detail="No emotion data found")
    except Exception as e:
        logging.error(f"classify_emotion Error: {e}")
        raise HTTPException(status_code=500, detail="Internal Server Error")

@app.get("/recommend_activity/{user_id}") 
async def recommend_activity(user_id: str):
    try:
        # Get the latest emotion analysis result from Firestore
        user_ref = db.collection('users').document(user_id)
        last_chat_docs = user_ref.collection('chat_history').order_by("timestamp", direction=firestore.Query.DESCENDING).limit(1).get()
        emotion_doc = next(last_chat_docs[0].reference.collection('emotion').stream())

        if emotion_doc.exists:
            emotion_data = emotion_doc.to_dict()
            max_emotion = emotion_data['emotion']
            recommended_activities = get_recommended_activities(max_emotion)
            return {
                "recommended_activities": recommended_activities
            }
        else:
            raise HTTPException(status_code=404, detail="No emotion data found")
    except Exception as e:
        logging.error(f"recommend_activity Error: {e}")
        raise HTTPException(status_code=500, detail="Internal Server Error")

def get_recommended_activities(emotion):
    activities = {
        "sedih": ["Olahraga", "Mendengarkan Musik", "Bicara dengan Seseorang"],
        "senang": ["Merayakan", "Olahraga", "Berkegiatan Sosial"],
        "takut": ["Latihan Pernapasan Dalam", "Meditasi Kesadaran", "Hadapi Ketakutan Anda"],
        "cinta": ["Menghabiskan Waktu Berkualitas", "Ekspresikan Rasa Syukur", "Lakukan Sesuatu yang Baik"],
        "marah": ["Aktivitas Fisik", "Pernapasan Dalam", "Saluran Kreatif"]
    }
    if emotion in activities:
        return activities[emotion]
    else:
        return []  # Return empty list if no recommendations for the emotion

def predict_text_probabilities(hasil_nlp, text):
    mapped_probabilities = {}
    for item in hasil_nlp[0]:
        label = item['label']
        score = item['score']
        mapped_probabilities[label_mapping[label]] = score
    return mapped_probabilities

def to_markdown(text):
    text = text.replace('•', '  *')
    return textwrap.indent(text, '> ', predicate=lambda _: True)

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000, log_level="debug")
