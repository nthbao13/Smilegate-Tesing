package com.example.game.controller;

import com.detectlanguage.errors.APIError;
import com.example.game.constant.Constants;
import com.example.game.dto.request.InputGameRequest;
import com.example.game.dto.response.CategoryResponse;
import com.example.game.dto.response.GameResponse;
import com.example.game.dto.response.LanguageResponse;
import com.example.game.exception.InputException;
import com.example.game.service.CategoryService;
import com.example.game.service.GameService;
import com.example.game.service.LanguageService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Slf4j
@Controller
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;
    private final CategoryService categoryService;
    private final LanguageService languageService;

    @Autowired
    public GameController(GameService gameService, CategoryService categoryService,
                          LanguageService languageService) {
        this.gameService = gameService;
        this.categoryService = categoryService;
        this.languageService = languageService;
    }

    @GetMapping
    public ModelAndView displayGameList(Model model, @RequestParam(value = "page", defaultValue = "1") Integer page,
                                        @RequestParam(value = "keyword", required = false) String keyword,
                                        @RequestParam(value = "category", required = false) Integer category) {
        ModelAndView modelAndView = new ModelAndView("game_list.html");
        int pageSize = Constants.PAGE_LIMIT;

        List<CategoryResponse> categoryResponses = categoryService.getFullCategories();
        Page<GameResponse> gameDTOS = gameService.getGames(page, pageSize, keyword, category);

        modelAndView.addObject("categories", categoryResponses);
        modelAndView.addObject("games", gameDTOS);
        modelAndView.addObject("page", page);
        modelAndView.addObject("keyword", keyword);
        modelAndView.addObject("category", category);

        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView displayRegisterPage(Model model) {
        ModelAndView modelAndView = new ModelAndView("register_game_form.html");
        List<CategoryResponse> categoryResponses = categoryService.getFullCategories();
        List<LanguageResponse> languageResponses = languageService.getFullLanguages();
        modelAndView.addObject("languages", languageResponses);
        modelAndView.addObject("categories", categoryResponses);
        modelAndView.addObject("inputGameRequest", new InputGameRequest());
        return modelAndView;
    }

    @PostMapping("/register")
    public String registerGame(
            @Valid @ModelAttribute("inputGameRequest") InputGameRequest inputGameRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) throws APIError {

        if (bindingResult.hasErrors()) {
            // Add data back to model for form re-display
            List<CategoryResponse> categoryResponses = categoryService.getFullCategories();
            List<LanguageResponse> languageResponses = languageService.getFullLanguages();
            model.addAttribute("languages", languageResponses);
            model.addAttribute("categories", categoryResponses);
            return "register_game_form";
        }

        try {
            gameService.registerGame(inputGameRequest);
            redirectAttributes.addFlashAttribute("toastSuccessMessage", "Game registered successfully!");
            return "redirect:/games";
        } catch (InputException ex) {
            redirectAttributes.addFlashAttribute("toastErrorMessage", ex.getMessage());
            return "redirect:/games/register";
        }
    }
}
