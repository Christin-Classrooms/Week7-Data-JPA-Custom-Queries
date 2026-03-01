package com.example.Thymeleaf.Demo.Service;

import com.example.Thymeleaf.Demo.Model.Player;
import com.example.Thymeleaf.Demo.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private PlayerRepository repo;

    public PlayerService(PlayerRepository repo) {

        this.repo = repo;
    }

    public List<Player> getAllPlayers() {
        
        return repo.findAll();
    }

    public void addPlayer(Player player) {

        repo.save(player);
    }

    public Player getPlayerById(int id) {
        return repo.findById(id).orElse(null);
    }

}
