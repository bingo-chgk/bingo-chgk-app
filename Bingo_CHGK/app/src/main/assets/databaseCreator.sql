DROP TABLE IF EXISTS CollectionArticle;
DROP TABLE IF EXISTS Collection;
DROP TABLE IF EXISTS SeenQuestion;
DROP TABLE IF EXISTS SearchInfo;
DROP TABLE IF EXISTS Question;
DROP TABLE IF EXISTS Handout;
DROP TABLE IF EXISTS Articles;

CREATE TABLE Articles(
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
	article_id INTEGER NOT NULL REFERENCES Articles(id),
	dbchgkinfo_id TEXT NOT NULL UNIQUE,
	handout_id INT REFERENCES Handout(id),
	comment_text TEXT,
	author TEXT,
	sources TEXT,
	additional_answers TEXT,
	answer TEXT NOT NULL
);

CREATE TABLE SearchInfo(
	article_id INTEGER NOT NULL REFERENCES Article(id),
	tag TEXT NOT NULL,
	UNIQUE(article_id, tag)
);

CREATE TABLE SeenQuestion(
	question_id INTEGER PRIMARY KEY REFERENCES Question(id),
	seen BOOLEAN NOT NULL
);

CREATE TABLE Collection(
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	name TEXT NOT NULL
);

CREATE TABLE CollectionArticle(
	collection_id INTEGER REFERENCES Collection(id),
	article_id INTEGER REFERENCES Article(id)
);



