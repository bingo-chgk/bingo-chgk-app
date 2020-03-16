DROP VIEW IF EXISTS TopicPercentage;
DROP TABLE IF EXISTS SavedQuestion;
DROP TABLE IF EXISTS CollectionTopic;
DROP TABLE IF EXISTS Collection;
DROP TABLE IF EXISTS SeenQuestion;
DROP TABLE IF EXISTS SearchInfo;
DROP TABLE IF EXISTS Question;
DROP TABLE IF EXISTS Handout;
DROP TABLE IF EXISTS Topic;

CREATE TABLE Topic(
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	text TEXT NOT NULL,
	name TEXT NOT NULL,
	read BOOLEAN NOT NULL
);

CREATE TABLE Handout(
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	text_handout_path TEXT,
	image_handout_path TEXT,
	audio_handout_path TEXT
);

CREATE TABLE Question(
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	topic_id INTEGER NOT NULL REFERENCES Topic(id),
	dbchgkinfo_id TEXT NOT NULL UNIQUE,
	text TEXT NOT NULL,
	handout_id INT REFERENCES Handout(id),
	comment_text TEXT,
	author TEXT,
	sources TEXT,
	additional_answers TEXT,
	wrong_answers TEXT,
	answer TEXT NOT NULL
);

CREATE TABLE SearchInfo(
	topic_id INTEGER NOT NULL REFERENCES Topic(id),
	tag TEXT NOT NULL,
	UNIQUE(topic_id, tag)
);

CREATE TABLE SeenQuestion(
	question_id INTEGER PRIMARY KEY REFERENCES Question(id),
	answered_right BOOLEAN NOT NULL
);

CREATE TABLE Collection(
	id INTEGER SERIAL KEY,
	name TEXT NOT NULL
);

CREATE TABLE CollectionTopic(
	collection_id INTEGER NOT NULL REFERENCES Collection(id),
	topic_id INTEGER NOT NULL REFERENCES Topic(id),
	UNIQUE(collection_id, topic_id)
);

CREATE TABLE SavedQuestion(
	question_id INTEGER PRIMARY KEY REFERENCES Question(id)
);

CREATE VIEW TopicPercentage 
AS
	SELECT Topic.name AS name, 
		CAST (100.0 * (SUM(CASE WHEN SeenQuestion.answered_right = 1 THEN 1 ELSE 0 END)) / MAX(1, COUNT(Question.id)) AS INTEGER) AS Percentage,
                Topic.id AS id,
                Topic.read AS read 
	FROM Topic
	LEFT JOIN Question ON Topic.id = Question.topic_id
	LEFT JOIN SeenQuestion ON Question.id = SeenQuestion.question_id 
	GROUP BY Topic.id, Topic.name, Topic.read;

