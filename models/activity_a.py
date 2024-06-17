import google.generativeai as genai
from IPython.display import Markdown

class ActivityA:
    def __init__(self, api_key):
        genai.configure(api_key=api_key)
        self.model_gemini = genai.GenerativeModel('gemini-1.5-pro')

    def to_markdown(self, text):
        text = text.replace('•', '  *')
        return Markdown(textwrap.indent(text, '> ', predicate=lambda _: True))

    def recommend_activity(self, emotion, emotion_prob):
        prompt = f"User kita sedang dalam keadaan {emotion} dengan probabilisitas {emotion_prob}. Berikan 3 rekomendasi aktivitas untuknya tanpa kata kata penyemangat output hanya 3 aktivitas saja"
        response = self.model_gemini.generate_content(prompt)
        return self.to_markdown(response.text)
