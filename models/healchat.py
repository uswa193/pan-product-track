
import textwrap
from IPython.display import display, Markdown
import google.generativeai as genai

class HealChat:
    def __init__(self, api_key):
        self.history = []
        genai.configure(api_key=api_key)
        self.model_gemini = genai.GenerativeModel('gemini-1.5-pro')

    def to_markdown(self, text):
        text = text.replace('•', '  *')
        return Markdown(textwrap.indent(text, '> ', predicate=lambda _: True))

    def join_list(self, list1):
        return " ".join(list1)

    def get_response(self, text):
        self.history.append(text)
        prompt = f"Anggap dia temanmu yang sedang bercerita. Dia bercerita/membalas kalau {text}. Respon dia sebagai teman. Jangan gunakan bahasa yang terlalu kaku dan jangan terlalu panjang. List berikut adalah chat dia dengan mu {self.history}"
        response = self.model_gemini.generate_content(prompt)
        return self.to_markdown(response.text)
