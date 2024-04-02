package com.example.tboard.domain;

import com.example.tboard.base.CommonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @ResponseBody
    public ArrayList<Article> search(@RequestParam(value="keyword", defaultValue = "") String keyword) {
    ArrayList<Article> searchedList = articleRepository.findArticleByKeyword(keyword);
        return searchedList;
    }

@RequestMapping("/detail")
    public String detail(@RequestParam("articleId") int articleId, Model model) {
        Article article = articleRepository.findArticleById(articleId);

        if (article == null) {
            return "없는 게시물입니다.";
        }

        article.increaseHit();

        model.addAttribute("article", article);

        return "detail";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam("articleId") int articleId) {

        Article article = articleRepository.findArticleById(articleId);

        if (article == null) {
            return "없는 게시물입니다.";
        }

        articleRepository.deleteArticle(article);
        return "%d 게시물이 삭제되었습니다.".formatted(articleId);
    }

@RequestMapping("/update")
@ResponseBody
    public String update(@RequestParam("articleId") int inputId,
                       @RequestParam("newTitle") String newTitle,
                       @RequestParam("newBody") String newBody
                        ) {

        Article article = articleRepository.findArticleById(inputId);

        if (article == null) {
            return "없는 게시물입니다.";
        }

        articleRepository.updateArticle(article, newTitle, newBody);
            return "%d번 게시물이 수정되었습니다.".formatted(inputId);
    }

    @RequestMapping("/list")
    public String list(Model model) {
        ArrayList<Article> articleList = articleRepository.findAll();
        model.addAttribute("articleList",articleList);

        return "list";
    }

    // 실제 데이터 저장 처리 부분
    @RequestMapping("/add")
    public String add(@RequestParam("title") String title,
                      @RequestParam("body") String body) {

        articleRepository.saveArticle(title, body);

        return "redirect:/list"; // 브라우저의 주소가 /list로 바뀜
    }


    // 입력 화면 보여주기
    @RequestMapping("/form")
    public String form() {
        return "form";
    }
}