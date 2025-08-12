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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;


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
    public ModelAndView showRegisterForm(Model model) {
        ModelAndView modelAndView = new ModelAndView("register_game_form.html");

        List<CategoryResponse> categoryResponses = categoryService.getFullCategories();
        List<LanguageResponse> languageResponses = languageService.getFullLanguages();

        model.addAttribute("mode", "register");
        model.addAttribute("formAction", "/games/register");

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

            model.addAttribute("mode", "register");
            model.addAttribute("formAction", "/games/register");
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
            model.addAttribute("mode", "register");
            model.addAttribute("formAction", "/games/register");
            return "redirect:/games/register";
        }
    }

    @GetMapping("/{id}/edit")
    public ModelAndView showEditForm(Model model, @PathVariable("id") Integer id) {
        InputGameRequest inputGameRequest = gameService.getGameRequestById(id);
        ModelAndView modelAndView = new ModelAndView("register_game_form.html");

        model.addAttribute("mode", "edit");
        model.addAttribute("formAction", "/games/" + id + "/edit");

        List<CategoryResponse> categoryResponses = categoryService.getFullCategories();
        List<LanguageResponse> languageResponses = languageService.getFullLanguages();

        modelAndView.addObject("languages", languageResponses);
        modelAndView.addObject("categories", categoryResponses);
        modelAndView.addObject("inputGameRequest", inputGameRequest);

        return modelAndView;
    }

    @PostMapping("/{id}/edit")
    public String editGame(
            @PathVariable("id") int id,
            @Valid @ModelAttribute("inputGameRequest") InputGameRequest inputGameRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) throws APIError {
        if (bindingResult.hasErrors()) {
            List<CategoryResponse> categoryResponses = categoryService.getFullCategories();
            List<LanguageResponse> languageResponses = languageService.getFullLanguages();

            model.addAttribute("languages", languageResponses);
            model.addAttribute("categories", categoryResponses);
            model.addAttribute("mode", "edit");
            model.addAttribute("formAction", "/games/" + id + "/edit");

            return "register_game_form";
        }

        try {
            gameService.editGame(inputGameRequest);
            redirectAttributes.addFlashAttribute("toastSuccessMessage", "Game edited successfully!");
            return "redirect:/games";
        } catch (InputException ex) {
            redirectAttributes.addFlashAttribute("toastErrorMessage", ex.getMessage());

            model.addAttribute("mode", "edit");
            model.addAttribute("formAction", "/games/" + inputGameRequest.getId() + "/edit");
            return "redirect:/games/" + inputGameRequest.getId() + "/edit";
        }
    }

    @PostMapping("/bulk-delete")
    @ResponseBody
    public ResponseEntity<?> deleteListOfGame(@RequestBody int[] ids) {
        try {
            if (ids == null || ids.length == 0) {
                return ResponseEntity.badRequest().body("No IDs provided");
            }
            gameService.deleteGamesByIds(ids);
            return ResponseEntity.ok(Map.of("message", "Deleted " + ids.length + " games successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting games: " + e.getMessage());
        }
    }
}
