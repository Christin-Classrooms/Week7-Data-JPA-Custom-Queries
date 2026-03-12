package com.example.Thymeleaf.Demo.controllers;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.repository.FighterRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Controller
public class FighterController {

    private final FighterRepository fighterRepository;

    public FighterController(FighterRepository fighterRepository) {
        this.fighterRepository = fighterRepository;
    }

    @GetMapping("/fighters")
    public String getFighters(
        Model model,
        @RequestParam(defaultValue= "0") int page,
        @RequestParam(defaultValue= "10") int size,
        @RequestParam(defaultValue= "id") String sort,
        @RequestParam(defaultValue= "ASC") String direction,
        @RequestParam(required =false) String search,
        @RequestParam(required =false) String filterType
    ) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("DESC") ?Sort.Direction.DESC:Sort.Direction.ASC;
        Pageable pageable =PageRequest.of(page,size, Sort.by(sortDirection, sort));
        Page<Fighter> fighterPage;

        if("name".equals(filterType) && search !=null&&!search.isEmpty()){
            fighterPage =fighterRepository.findByNameContainingIgnoreCase(search, pageable);
        
        }else if("health".equals(filterType) && search!= null) {
            int health =Integer.parseInt(search);
            fighterPage=fighterRepository.findByHealthGreaterThan(health,pageable);



        }else if ("strongest".equals(filterType)){
            fighterPage= fighterRepository.findStrongestFighters(pageable);

        } else if("balanced".equals(filterType)) {
            fighterPage= fighterRepository.findBalancedFighters(1200, 60.0, pageable);
        }else {
            fighterPage =fighterRepository.findAll(pageable);
        }

     
        model.addAttribute("fighters", fighterPage.getContent());
        model.addAttribute("totalPages", fighterPage.getTotalPages());
        model.addAttribute("totalElements", fighterPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("hasPrevious", fighterPage.hasPrevious());
        model.addAttribute("hasNext", fighterPage.hasNext());
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("filterType", filterType);

        return "Fighters";
    }
}
