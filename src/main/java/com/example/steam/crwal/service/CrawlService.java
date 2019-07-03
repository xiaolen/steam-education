package com.example.steam.crwal.service;

import com.example.steam.crwal.entity.CrawlState;

import java.util.LinkedList;
import java.util.Set;

/**
 * @Author: mly
 * @Date: Created in 2019/6/10
 */
public interface CrawlService {
    CrawlState choiceHomePage(LinkedList target_website) throws Exception;
}
