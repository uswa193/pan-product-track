# Emotion Classification 

This model is a fine-tuned version of IndoBERT model and designed for emotion classification. in Indonesian text It classifies input text into five emotion categories: Happy, Sad, Angry, Fear, and Love
This modelcard aims to be a base template for new models. It has been generated using [this raw template](https://github.com/huggingface/huggingface_hub/blob/main/src/huggingface_hub/templates/modelcard_template.md?plain=1).

## Model Details

### Model Description

<!-- Provide a longer summary of what this model is. -->


- **Model type:** BERT
- **Language(s) (NLP):** Indonesisa
- **Finetuned from model :** [IndoBERT base p2](https://huggingface.co/indobenchmark/indobert-base-p2)


### Direct Use

<!-- This section is for the model use without fine-tuning or plugging into a larger ecosystem/app. -->

You can use this model directly using Hugging Face pipeline. Here is the code to run in directly using hugging face

```python
# Use a pipeline as a high-level helper
from transformers import pipeline

pipe = pipeline("feature-extraction", model="indobenchmark/indobert-base-p2")

```

### Limitations

- The model only works well for Indonesian language




## How to Get Started with the Model

Use the code below to get started with the model.

```python
# Use a pipeline as a high-level helper
from transformers import pipeline

pipe = pipeline("feature-extraction", model="indobenchmark/indobert-base-p2")

```


## Training Details

### Training Data

The model was trained on a dataset specifically designed for emotion classification in Indonesian. The dataset includes various texts labeled with one of the five emotions.

### Training Procedure


#### Training Hyperparameters

- **Optimizers:** Adam
- **Learning rate:** 3e-5 
- **Epoch:** 2


## Evaluation

#### Metrics

-Accuracy


### Results

-**Loss:** 0.9135 
-**Accuracy:** 0.6930 

