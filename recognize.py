import cv2
import pickle
import hashlib
import os

# ---------------------------
# CHECK FILES
# ---------------------------
if not os.path.exists("trainer.yml") or not os.path.exists("labels.pkl"):
    print("❌ Model files not found. Run train.py first.")
    exit()

# ---------------------------
# LOAD MODEL
# ---------------------------
recognizer = cv2.face.LBPHFaceRecognizer_create()
recognizer.read("trainer.yml")

with open("labels.pkl", "rb") as f:
    label_map = pickle.load(f)

# ---------------------------
# LOAD PASSWORDS
# ---------------------------
def load_passwords():
    db = {}
    with open("passwords.txt", "r") as f:
        for line in f:
            user, pwd = line.strip().split(":")
            db[user] = pwd
    return db

password_db = load_passwords()

# ---------------------------
# FACE RECOGNITION
# ---------------------------
def face_auth():
    face_cascade = cv2.CascadeClassifier(
        cv2.data.haarcascades + 'haarcascade_frontalface_default.xml'
    )

    cap = cv2.VideoCapture(0)

    print("🔍 Looking for authorized officer...")

    while True:
        ret, frame = cap.read()
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

        faces = face_cascade.detectMultiScale(gray, 1.3, 5)

        for (x, y, w, h) in faces:
            roi = gray[y:y+h, x:x+w]

            label, confidence = recognizer.predict(roi)

            if confidence < 50:
                name = label_map[label]

                # Draw rectangle
                cv2.rectangle(frame, (x, y), (x+w, y+h), (0, 255, 0), 2)

                # Show name
                cv2.putText(frame, f"{name}", (x, y-10),
                            cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)

                cv2.imshow("Recognition", frame)

                print(f"✅ Authorized: {name}")

                cv2.waitKey(2000)  # wait 2 seconds
                

                cap.release()
                cv2.destroyAllWindows()
                return name

            else:
                cv2.rectangle(frame, (x, y), (x+w, y+h), (0, 0, 255), 2)
                cv2.putText(frame, "Unknown", (x, y-10),
                            cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 0, 255), 2)

        cv2.imshow("Recognition", frame)

        if cv2.waitKey(1) == 27:
            break

    cap.release()
    cv2.destroyAllWindows()
    return None
def password_auth(username):
    if username not in password_db:
        print("❌ User not found")
        return False

    for _ in range(3):
        password = input("Enter password: ")
        hashed = hashlib.sha256(password.encode()).hexdigest()

        if hashed == password_db[username]:
            print("✅ Password correct")
            return True
        else:
            print("❌ Wrong password")

    return False

# ---------------------------
# MAIN 2FA SYSTEM
# ---------------------------
def authenticate():
    user = face_auth()

    if user:
        if password_auth(user):
            print("🔓 ACCESS GRANTED")
            return True

    print("🚫 ACCESS DENIED")
    return False


if __name__ == "__main__":
    authenticate()