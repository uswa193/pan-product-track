from flask import Flask, request, jsonify
from models.healChat_model import HealChatModel
from models.emotion_classification_model import EmotionClassificationModel
from models.activity_recommendation_model import ActivityRecommendationModel

app = Flask(__name__)

heal_chat_model = HealChatModel()
emotion_classification_model = EmotionClassificationModel()
activity_recommendation_model = ActivityRecommendationModel()

@app.route('/healchat', methods=['POST'])
def heal_chat():
    text = request.json.get('text')
    prediction = heal_chat_model.predict(text)
    return jsonify({'prediction': prediction})

@app.route('/emotion', methods=['POST'])
def classify_emotion():
    text = request.json.get('text')
    emotion = emotion_classification_model.predict(text)
    return jsonify({'emotion': emotion})

@app.route('/recommend', methods=['POST'])
def recommend_activity():
    user_data = request.json
    recommendation = activity_recommendation_model.recommend(user_data)
    return jsonify({'recommendation': recommendation})

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')

