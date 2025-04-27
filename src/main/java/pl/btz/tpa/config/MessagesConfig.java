package pl.btz.tpa.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

public class MessagesConfig extends OkaeriConfig {

    @Comment("Wiadomosci pokazywane gracza")
    private String only_players = "§cTylko gracze mogą używać tej komendy!";
    private String usage_tpa = "§cUżycie: /tpa <gracz>";
    private String player_not_found = "§cGracz nie został znaleziony!";
    private String tpa_sent = "§aWysłano prośbę o teleportację do {player}.";
    private String tpa_received = "§aOtrzymano prośbę o teleportację od {player}.";
    private String no_request = "§cNie masz żadnej prośby o teleportację!";
    private String tpa_accepted = "§aZaakceptowałeś prośbę o teleportację od {player}.";
    private String tpa_accepted_by = "§a{player} zaakceptował twoją prośbę o teleportację!";
    private String cannot_tpa_self = "§cNie możesz wysłać prośby o teleportację do samego siebie!";
    private String already_requested = "§cJuż wysłałeś prośbę o teleportację do {player}.";
    private String already_requested_by = "§cJuż otrzymałeś prośbę o teleportację od {player}.";

    public String getMessage(String key) {
        return this.get(key).toString();
    }
}