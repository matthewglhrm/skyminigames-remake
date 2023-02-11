package io.github.vooiid.skyminigames.bukkit.packets.system;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ScoreboardAPI {

    private Scoreboard Scoreboard;
    private String Titulo;
    private Map<String, Integer> Scores;
    private List<Team> Times;
    private Objective Objetivo;

    public ScoreboardAPI(String title) {
        this.Scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.Titulo = title;
        this.Scores = Maps.newLinkedHashMap();
        this.Times = Lists.newArrayList();
    }

    public void addLine(String message, Integer Score) {
        Preconditions.checkArgument(message.length() < 70, "Sua Scoreboard nao pode passar de 48 caracteres");
        message = substituir(message);
        this.Scores.put(message, Score);
    }

    private String substituir(String message) {
        while (this.Scores.containsKey(message)) {
            message = message + "§r";
        }
        if (message.length() > 60)
            message = message.substring(0, 69);
        return message;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Map.Entry<Team, String> createTeam(String message) {
        String result = "";
        if (message.length() <= 16) {
            return new AbstractMap.SimpleEntry(null, message);
        }
        Team Time = this.Scoreboard.registerNewTeam("text-" + this.Scoreboard.getTeams().size());
        Iterator MensagemSplit = Splitter.fixedLength(16).split(message).iterator();
        Time.setPrefix((String)MensagemSplit.next());
        result = (String)MensagemSplit.next();

        if (message.length() > 32)
            Time.setSuffix((String)MensagemSplit.next());
        this.Times.add(Time);
        return new AbstractMap.SimpleEntry(Time, result);
    }

    @SuppressWarnings("rawtypes")
    public void setScoreboard() {
        this.Objetivo = this.Scoreboard.registerNewObjective("a", "dummy");
        this.Objetivo.setDisplayName(this.Titulo);
        this.Objetivo.setDisplaySlot(DisplaySlot.SIDEBAR);
        int index = this.Scores.size();
        for (Map.Entry Mensagem : this.Scores.entrySet()) {
            Map.Entry Time = createTeam((String)Mensagem.getKey());
            Integer Score = Integer.valueOf(Mensagem.getValue() != null ? ((Integer)Mensagem.getValue()).intValue() : index);
            String jogador = (String)Time.getValue();

            if (Time.getKey() != null) ((Team)Time.getKey()).addEntry(jogador);
            this.Objetivo.getScore(jogador).setScore(Score.intValue());
            index--;
        }
    }

    public void resetScoreboard() {
        if (this.Objetivo != null) {
            this.Objetivo.unregister();
        }
        this.Scores.clear();
        for (Team Times : this.Times)
            Times.unregister();
        this.Times.clear();
    }

    public Scoreboard getScoreboard() {
        return this.Scoreboard;
    }
}
