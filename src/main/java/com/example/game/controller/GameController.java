package com.example.game.controller;

import com.example.game.constant.Constants;
import com.example.game.dto.CategoryDTO;
import com.example.game.dto.GameDTO;
import com.example.game.service.CategoryService;
import com.example.game.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;
    private final CategoryService categoryService;

    @Autowired
    public GameController(GameService gameService, CategoryService categoryService) {
        this.gameService = gameService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public ModelAndView displayGameList(Model model, @RequestParam(value = "page", defaultValue = "1") Integer page,
                                        @RequestParam(value = "keyword", required = false) String keyword,
                                        @RequestParam(value = "category", required = false) Integer category) {
        ModelAndView modelAndView = new ModelAndView("gamelist.html");
        int pageSize = Constants.PAGE_LIMIT;

        List<CategoryDTO> categoryDTOS = categoryService.findAll();
        Page<GameDTO> gameDTOS = gameService.getGames(page, pageSize, keyword, category);

        modelAndView.addObject("categories", categoryDTOS);
        modelAndView.addObject("games", gameDTOS);
        modelAndView.addObject("page", page);
        modelAndView.addObject("keyword", keyword);
        modelAndView.addObject("category", category);

        return modelAndView;
    }
}
