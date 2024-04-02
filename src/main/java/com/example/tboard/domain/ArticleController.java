package com.example.tboard.domain;

import com.example.tboard.base.CommonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Scanner;

// Model - Controller - View
@Controller
public class ArticleController { // Model + Controller

    CommonUtil commonUtil = new CommonUtil();
    ArticleView articleView = new ArticleView();
    ArticleRepository articleRepository = new ArticleRepository();

    Scanner scan = commonUtil.getScanner();
    int WRONG_VALUE = -1;


    @RequestMapping("/search")
    public String search(@RequestParam(value="keyword", defaultValue = "") String keyword,
                         Model model) {
    ArrayList<Article> searchedList = articleRepository.findArticleByKeyword(keyword);

    model.addAttribute("articleList", searchedList);

    return "list";
    }

    @RequestMapping("/detail/{articleId}")
    public String detail(@PathVariable("articleId") int articleId, Model model) {
        Article article = articleRepository.findArticleById(articleId);

        if (article == null) {
            return "없는 게시물입니다.";
        }

        article.increaseHit();

        model.addAttribute("article", article);

        return "detail";
    }

    @RequestMapping("/delete/{articleId}")
    public String delete(@PathVariable("articleId") int articleId) {

        Article article = articleRepository.findArticleById(articleId);

        if (article == null) {
            return "없는 게시물입니다.";
        }

        articleRepository.deleteArticle(article);
        return "redirect:/list";
    }

    @GetMapping("/update/{articleId}")
    public String updateForm(@PathVariable("articleId") int articleId , Model model) {

        Article article = articleRepository.findArticleById(articleId);

        if (article == null) {
            throw new RuntimeException("없는 게시물입니다.");
        }
        model.addAttribute("article", article);

        return "updateForm";
    }

@PostMapping("/update/{articleId}")
    public String update(@PathVariable("articleId") int articleId,
                       @RequestParam("title") String title,
                       @RequestParam("body") String body
                        ) {

        Article article = articleRepository.findArticleById(articleId);

        if (article == null) {
            throw new RuntimeException("없는 게시물입니다.");
        }

        articleRepository.updateArticle(article, title, body);
            return "redirect:/detail/%d".formatted(articleId);
    }

    @RequestMapping("/list")
    public String list(Model model) {
        ArrayList<Article> articleList = articleRepository.findAll();
        model.addAttribute("articleList",articleList);

        return "list";
    }


    // 입력 화면 보여주기
    @GetMapping("/add")
    public String form() {
        return "form";
    }

    // 실제 데이터 저장 처리 부분
    @PostMapping("/add")
    public String add(@RequestParam("title") String title,
                      @RequestParam("body") String body) {

        articleRepository.saveArticle(title, body);

        return "redirect:/list"; // 브라우저의 주소가 /list로 바뀜
    }


}