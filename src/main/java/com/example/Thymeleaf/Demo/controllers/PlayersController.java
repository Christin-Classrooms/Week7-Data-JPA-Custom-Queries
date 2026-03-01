package com.example.Thymeleaf.Demo.controllers;

import com.example.Thymeleaf.Demo.Model.Player;
import com.example.Thymeleaf.Demo.Service.PlayerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PlayersController {

    private final PlayerService playerService;

    public PlayersController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/players")
    public String getPlayers(@RequestParam(name = "id", required = false) Integer id, Model model){
        if(id != null){
            Player player = playerService.getPlayerById(id);
            List<Player> players = new ArrayList<>();
            if(player != null){
                players.add(player);
            }
            model.addAttribute("players", players);
            model.addAttribute("total", players.size());
            return "Players";
        }

        List<Player> players = playerService.getAllPlayers();
        model.addAttribute("players", players);
        model.addAttribute("total", players.size());
        return "Players";
    }

}
