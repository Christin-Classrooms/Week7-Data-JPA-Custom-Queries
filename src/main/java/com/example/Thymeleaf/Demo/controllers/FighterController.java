package com.example.Thymeleaf.Demo.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Thymeleaf.Demo.Model.Fighter;
import com.example.Thymeleaf.Demo.Service.FighterService;

@Controller
public class FighterController {

    private final FighterService fighterService;

    public FighterController(FighterService fighterService) {
        this.fighterService = fighterService;
    }

    @GetMapping("/fighters")
    public String getFighters(
            @RequestParam(name="page", required=false, defaultValue="0") int page,
            @RequestParam(name="size", required=false, defaultValue="10") int size,
            @RequestParam(name="sort", required=false, defaultValue="id") String sort,
            @RequestParam(name="search", required=false, defaultValue="") String search,
            @RequestParam(name="direction", required=false, defaultValue="ASC") String direction,
            @RequestParam(name="filterType", required=false, defaultValue="name") String filterType,
            Model model) {
        
        Sort.Direction sortedDirection = direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sortby = Sort.by(sortedDirection, sort);

        Pageable pageable = PageRequest.of(page, size, sortby);
        Page<Fighter> fighterPage;

        switch(filterType) {
            case "name" :
                if(search!= null && !search.trim().isEmpty()){
                    fighterPage = fighterService.findFighterByName(search, pageable);
                } else {
                    fighterPage = fighterService.getAllFightersPageable(pageable);
                }
                break;
            case "balanced":
                 try {
                    String[] values = search.split(",");
                    double minHealth = Double.parseDouble(values[0]);
                    double maxDamage = Double.parseDouble(values[1]);
                    fighterPage = fighterService.getBalancedFighters(minHealth, maxDamage, pageable);
                } catch (Exception e){
                    fighterPage = fighterService.getAllFightersPageable(pageable);
                }
                break;
            case "health":
                try{
                    fighterPage = fighterService.getFightersByHealthGreaterThan(Integer.parseInt(search), pageable);
                } catch (Exception e){
                    fighterPage = fighterService.getAllFightersPageable(pageable);
                }    
                break;
            case "strongest":
                fighterPage = fighterService.getStrongestFighters(pageable);
                break;
            default: fighterPage = fighterService.getAllFightersPageable(pageable);
        } 
        

        model.addAttribute("fighters", fighterPage.getContent());
        model.addAttribute("total", fighterPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages", fighterPage.getTotalPages());
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        model.addAttribute("filterType", filterType);
        model.addAttribute("direction", direction);
        //Switching the Pagination
        model.addAttribute("hasPrevious", fighterPage.hasPrevious());
        model.addAttribute("hasNext", fighterPage.hasNext());
        model.addAttribute("startIndex", page * size +1);
        model.addAttribute("endIndex", Math.min((page+1)*size,(int)fighterPage.getTotalElements()));

        return "Fighters";
    }

}
