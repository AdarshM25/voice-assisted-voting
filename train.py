import cv2
import os
import numpy as np
import pickle

dataset_path = "dataset"

faces = []
labels = []
label_map = {}
current_label = 0

face_cascade = cv2.CascadeClassifier(
    cv2.data.haarcascades + 'haarcascade_frontalface_default.xml'
)

for person in os.listdir(dataset_path):
    person_path = os.path.join(dataset_path, person)

    if not os.path.isdir(person_path):
        continue

    label_map[current_label] = person

    for img_name in os.listdir(person_path):
        img_path = os.path.join(person_path, img_name)

        img = cv2.imread(img_path)
        if img is None:
            continue

        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

        faces_detected = face_cascade.detectMultiScale(gray, 1.3, 5)

        for (x, y, w, h) in faces_detected:
            faces.append(gray[y:y+h, x:x+w])
            labels.append(current_label)

    current_label += 1

# Train model
recognizer = cv2.face.LBPHFaceRecognizer_create()
recognizer.train(faces, np.array(labels))

# Save files
recognizer.save("trainer.yml")

with open("labels.pkl", "wb") as f:
    pickle.dump(label_map, f)

print("✅ Training complete")
print("📁 Files saved at:", os.getcwd())