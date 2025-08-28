from flask import Flask, request, jsonify
from model import RecommenderModel, ScoreEvent, load_training_data

app = Flask(__name__)
model = None

def initialize_model():
    global model
    if model is None:
        model = RecommenderModel()
        dummy_data = load_training_data()
        for event in dummy_data:
            model.add_score_event(event)
        print("Loaded dummy data")

initialize_model()

@app.route('/score_update', methods=['POST'])
def score_update():
    data = request.get_json()
    try:
        event = ScoreEvent(
            player_id=int(data['playerId']),
            quiz_session_id=int(data['quizSessionId']),
            score=int(data['score']),
            total_questions=int(data['totalQuestions']),
            correct_answers=int(data['correctAnswers']),
            difficulty=data['difficulty'].upper(),
            genre_name=data['genreName'].upper())
        model.add_score_event(event)
        return jsonify({"status": "success"}), 200
    except Exception as e:
        import traceback
        traceback.print_exc()
        return jsonify({"status": "error", "message": f"Invalid input: {e}"}), 400


@app.route('/recommend', methods=['POST'])
def recommend():
    data = request.get_json()
    try:
        player_id = int(data['playerId'])
        limit = int(data.get('limit', 3))
    except Exception as e:
        return jsonify({"status": "error", "message": f"Invalid input: {e}"}), 400

    response = model.recommend(player_id, limit)
    return jsonify(response.to_dict())

@app.route('/health', methods=['GET'])
def health():
    return jsonify({"status": "OK"})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8000)
