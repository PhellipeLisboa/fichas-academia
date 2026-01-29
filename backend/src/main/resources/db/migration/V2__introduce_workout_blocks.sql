CREATE TABLE workout_block (
    id BIGSERIAL PRIMARY KEY,
    workout_id BIGINT NOT NULL,
    execution_type VARCHAR(20) NOT NULL,
    position INTEGER NOT NULL,

    CONSTRAINT fk_workout_block_workout
        FOREIGN KEY (workout_id)
        REFERENCES workout(id)
        ON DELETE CASCADE,

    CONSTRAINT uq_workout_block_order
        UNIQUE (workout_id, position)
);

CREATE INDEX idx_workout_block_workout ON workout_block(workout_id);

CREATE TABLE block_item (
    id BIGSERIAL PRIMARY KEY,
    workout_block_id BIGINT NOT NULL,
    exercise_id BIGINT NOT NULL,
    machine_id BIGINT NOT NULL,
    sets INTEGER NOT NULL,
    reps INTEGER NOT NULL,
    position INTEGER NOT NULL,

    CONSTRAINT fk_block_item_block
        FOREIGN KEY (workout_block_id)
        REFERENCES workout_block(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_block_item_exercise
        FOREIGN KEY (exercise_id)
        REFERENCES exercise(id),

    CONSTRAINT fk_block_item_machine
        FOREIGN KEY (machine_id)
        REFERENCES machine(id),

    CONSTRAINT uq_block_item_order
        UNIQUE (workout_block_id, position)

);

CREATE INDEX idx_block_item_block ON block_item(workout_block_id);

DROP TABLE IF EXISTS workout_item;