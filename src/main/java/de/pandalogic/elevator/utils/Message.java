package de.pandalogic.elevator.utils;

import de.pandalogic.elevator.ElevatorPlugin;

public enum Message {

    NO_CONSOLE("&cDieser Befehl ist nur für Spieler!"),
    NO_PERMISSION("&cDafür hast du keine Rechte!"),
    NO_COMMAND("&cDieser Befehl existiert nicht!"),
    USAGE("&cBitte benutze: &a<String1>"),
    TARGET_OFFLINE("&cDer Spieler wurde nicht gefunden!"),
    TARGET_EQUALS_SENDER("&cDu kannst diesen Befehl nicht bei dir selber anwenden!"),
    ON_JOIN("&6<String1> &ahat das Spiel betreten."),
    ON_QUIT("&6<String1> &chat das Spiel verlassen."),
    QUERY_EXCEPTION("&cEin Fehler ist bei der Datenbankabfrage aufgetreten!"),
    INVALID_NUMBER("&cBitte gebe eine valide Zahl an!"),
    INVALID_BLOCKFACE("&cBitte gebe einen validen Richtung an (NORTH, EAST, SOUTH, WEST)."),
    INVALID_FLOOR_NUMBER("&cDiese Etagennummer ist bereits vergeben!"),
    INTEGER_PARAMETER("&cAlle Parameter benötigen eine ganze Zahl!"),

    ELEVATOR_CREATED("&aDer Aufzug &7(&6<Integer1>&7) &awurde erstellt."),
    ELEVATOR_REMOVED("&aDer Aufzug &7(&6<Integer1>&7) &awurde entfernt."),
    ELEVATOR_FLOOR_CREATED("&aDie Etage &6<Integer1> &awurde dem Aufzug &6<Integer2> &ahinzugefügt."),
    ELEVATOR_FLOOR_REMOVED("&aDie Etage &6<Integer1> &awurde von dem Aufzug &6<Integer2> &aentfernt."),
    ELEVATOR_TOO_HEAVY("&cAchtung Überlast! Bitte verringern Sie das Gewicht!"),
    ELEVATOR_ID("&aDu befindest dich im Aufzug &6<Integer1>&a."),
    ELEVATOR_NOT_EXISTS("&cEs gibt keinen Aufzug mit der ID &6<Integer1>&c."),
    ELEVATOR_FLOOR_NOT_EXISTS("&cDer Aufzug &6<Integer1> &cbesitzt keine Etage &6<Integer2>&c."),

    ELEVATOR_INFO("&aDer Aufzug &7(&6<Integer1>&7) &abefindet sich auf der " +
            "Höhe &6<Integer2> &aund besitzt &6<Integer3> &aEtagen."),
    ELEVATOR_FLOORS("&7- &aDie Etage &6<Integer1> &abefindet sich auf der Höhe &6<Integer2>&a."),
    ELEVATOR_SETTINGS("&aFolgende Einstellungen sind definiert:"),
    ELEVATOR_SETTING("&7- &a<String1> = &6<Integer1>"),
    ELEVATOR_INVENTORY_CLOSED("&aDas Inventar wurde geschlossen, da eine neue Etage registriert wurde. " +
            "Du kannst es nun erneut öffnen. &6/elevator inventory"),
    ELEVATOR_CALL_CANCELLED("&cDu darfst den Aufzug nicht rufen!"),
    ELEVATOR_UNKNOWN_SETTING("&cDiese Einstellung existiert nicht!"),
    ELEVATOR_INVALID_VALUE("&cDer Wert der Einstellung muss größer als 0 sein!"),
    ELEVATOR_SETTING_UPDATED("&aDie Einstellung wurde aktualisiert."),

    PLAYER_NOT_IN_ELEVATOR("&cDu befindest dich nicht in einem Aufzug!");

    private final MessageUtil util;

    Message(String message) {
        this(message, true);
    }

    Message(String message, boolean addPrefix) {
        this.util = new MessageUtil(ElevatorPlugin.PREFIX, message, addPrefix);
    }

    public <T> Message replace(T object) {
        this.util.replace(object);
        return this;
    }

    public String getMessage() {
        return this.util.getMessage();
    }

}
