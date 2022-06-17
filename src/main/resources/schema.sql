CREATE TABLE IF NOT EXISTS elevator
(
    elevator_id   int AUTO_INCREMENT PRIMARY KEY,
    world_name    varchar(255) NOT NULL,
    current_y     int          NOT NULL,
    bottom_left_x int          NOT NULL,
    bottom_left_y int          NOT NULL,
    bottom_left_z int          NOT NULL,
    top_right_x   int          NOT NULL,
    top_right_y   int          NOT NULL,
    top_right_z   int          NOT NULL
);

CREATE TABLE IF NOT EXISTS elevator_floor
(
    elevator_floor_id  int AUTO_INCREMENT PRIMARY KEY,
    elevator_id        int          NOT NULL,
    floor_number       int          NOT NULL,
    base_y             int          NOT NULL,
    button_x           int          NOT NULL,
    button_y           int          NOT NULL,
    button_z           int          NOT NULL,
    gate_bottom_left_x int          NOT NULL,
    gate_bottom_left_y int          NOT NULL,
    gate_bottom_left_z int          NOT NULL,
    gate_top_right_x   int          NOT NULL,
    gate_top_right_y   int          NOT NULL,
    gate_top_right_z   int          NOT NULL,
    output_direction   varchar(255) NOT NULL,
    UNIQUE (elevator_id, floor_number),
    FOREIGN KEY (elevator_id) REFERENCES elevator (elevator_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS elevator_settings
(
    elevator_settings_id   int AUTO_INCREMENT PRIMARY KEY,
    elevator_id            int UNIQUE NOT NULL,
    player_position_check  int        NOT NULL default 5,
    max_player             int        NOT NULL default 5,
    next_stop_check        int        NOT NULL default 60,
    start_delay            int        NOT NULL default 40,
    speed                  int        NOT NULL default 15,
    FOREIGN KEY (elevator_id) REFERENCES elevator (elevator_id) ON DELETE CASCADE
);

DELIMITER $$

CREATE TRIGGER after_elevator_insert
    AFTER INSERT
    ON elevator
    FOR EACH ROW
BEGIN
    INSERT INTO elevator_settings(elevator_id) VALUES (new.elevator_id);
END$$

DELIMITER ;