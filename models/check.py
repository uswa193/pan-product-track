import numpy as np
from transformers import pipeline

class Check:
    def __init__(self, model_path, tokenizer_path, label_mapping):
        self.nlp = pipeline(
            "sentiment-analysis",
            model=model_path,
            tokenizer=tokenizer_path,
            return_all_scores=True
        )
        self.label_mapping = label_mapping

    def predict_text_probabilities(self, hasil_nlp, text):
        mapped_probabilities = {}
        for item in hasil_nlp[0]:
            label = item['label']
            score = item['score']
            mapped_probabilities[self.label_mapping[label]] = score
        return mapped_probabilities

    def check_emotion(self, text):
        probabilities = self.predict_text_probabilities(self.nlp(text), text)
        for label, prob in probabilities.items():
            print(f"{label}: {prob:.4f}")
        max_emotion = max(probabilities, key=probabilities.get)
        max_emotion_prob = probabilities[max_emotion]
        print(f"\nEmosi pengguna: {max_emotion} ({max_emotion_prob:.4f})")
        return max_emotion, max_emotion_prob
