CREATE TABLE member (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    public_code VARCHAR(100) NOT NULL UNIQUE,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE professional (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE exercise (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    video_url TEXT,
    thumbnail_url TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE machine (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    number INTEGER NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE workout_plan (
    id BIGSERIAL PRIMARY KEY,
    member_id BIGINT NOT NULL,
    professional_id BIGINT NOT NULL,
    start_date DATE,
    review_date DATE,
    reassessment_date DATE,
    sheet_number INTEGER,
    rest_seconds INTEGER,
    intensity VARCHAR(50),
    status VARCHAR(20) NOT NULL,
    notes TEXT,

    CONSTRAINT fk_workout_plan_member FOREIGN KEY (member_id) REFERENCES member(id),
    CONSTRAINT fk_workout_plan_professional FOREIGN KEY (professional_id) REFERENCES professional(id)
);

CREATE TABLE workout (
    id BIGSERIAL PRIMARY KEY,
    workout_plan_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    position INTEGER NOT NULL,

    CONSTRAINT fk_workout_plan FOREIGN KEY (workout_plan_id) REFERENCES workout_plan(id)
);

CREATE TABLE workout_item (
    id BIGSERIAL PRIMARY KEY,
    workout_id BIGINT NOT NULL,
    exercise_id BIGINT NOT NULL,
    machine_id BIGINT,
    sets INTEGER,
    reps INTEGER,
    position INTEGER NOT NULL,

    CONSTRAINT fk_workout_item_workout FOREIGN KEY (workout_id) REFERENCES workout(id),
    CONSTRAINT fk_workout_item_exercise FOREIGN KEY (exercise_id) REFERENCES exercise(id),
    CONSTRAINT fk_workout_item_machine FOREIGN KEY (machine_id) REFERENCES machine(id)
);
