import random
import json

class ActivityB:
    def __init__(self, activities_file):
        with open(activities_file, 'r') as json_file:
            self.recommended_activities = json.load(json_file)

    def recommend_activities(self, emotion):
        if emotion in self.recommended_activities:
            selected_activities = random.sample(self.recommended_activities[emotion], 3)
            recommendations = []
            for num, activity in enumerate(selected_activities, 1):
                recommendations.append(f"{num}. {activity['title']}\n{activity['description']}\n")
            return "\n".join(recommendations)
        else:
            return "No recommendations available for the given emotion."
