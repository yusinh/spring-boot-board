package com.example.tboard.domain;

import com.example.tboard.base.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
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


    public void search() {
        // 검색어를 입력
//        System.out.println("검색 키워드를 입력해주세요 :");
//        String keyword = scan.nextLine();
//        ArrayList<Article> searchedList = articleRepository.findArticleByKeyword(keyword);
//
//        articleView.printArticleList(searchedList);
    }

@RequestMapping("/detail")
@ResponseBody
    public String detail(@RequestParam("articleId") int articleId) {
        Article article = articleRepository.findArticleById(articleId);

        if (article == null) {
            return "없는 게시물입니다.";
        }

        article.increaseHit();

        // 객체를 -> json 문자열로 변환 -> jackson 라이브러리 사용
        String jsonString = "";

        try {
            // ObjectMapper 인스턴스 생성
            ObjectMapper objectMapper = new ObjectMapper();

            // 객체를 JSON 문자열로 변환
            jsonString = objectMapper.writeValueAsString(article);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonString;
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
    @ResponseBody
    public ArrayList<Article> list() {
        ArrayList<Article> articleList = articleRepository.findAll();

        return articleList;
    }

    @RequestMapping("/add")
    @ResponseBody
    public String add(@RequestParam("title") String title,
                      @RequestParam("body") String body) {

        articleRepository.saveArticle(title, body);
        return "게시물이 등록되었습니다.";

    }
}